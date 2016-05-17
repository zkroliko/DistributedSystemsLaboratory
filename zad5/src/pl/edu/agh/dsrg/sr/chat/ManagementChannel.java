package pl.edu.agh.dsrg.sr.chat;

import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.UDP;
import org.jgroups.stack.Protocol;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatState;

import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.Map;

import static pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.*;

public class ManagementChannel extends Channel {

    public static final String NAME = "ChatManagement321123";

    private Map<Integer, CommChannel> channels;

    private ChannelManager client;

    public ManagementChannel(String ownName, ChannelManager client) {
        super(NAME, ownName);
        buildChannel();
        this.channels = client.getChannels();
        this.client = client;
    }

    public void synchronize() {
        try {
            channel.getState(null,10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendJoin(String channelName) throws Exception {
        ChatAction action;
        action = ChatAction.newBuilder().
                setAction(ChatAction.ActionType.JOIN).
                setChannel(channelName).
                setNickname(ownName).build();
        channel.send(new Message(null, null, action.toByteArray()));
    }

    public void sendLeave(String channelName) throws Exception {
        ChatAction action;
        action = ChatAction.newBuilder().
                setAction(ChatAction.ActionType.LEAVE).
                setChannel(channelName).
                setNickname(ownName).build();
        channel.send(new Message(null, null, action.toByteArray()));
    }

    protected void setUpReceiver() {
        channel.setReceiver(new ReceiverAdapter() {

            @Override
            public void getState(OutputStream output) throws Exception {
                ChatState.Builder builder = ChatState.newBuilder();
                for (CommChannel channel : channels.values()) {
                    buildFromChannel(builder,channel);
                }
                builder.build().writeTo(output);
            }

            private void buildFromChannel(ChatState.Builder builder, CommChannel commChannel) {
                for (String user : commChannel.users) {
                    buildFromUser(builder,user, commChannel);
                }
            }

            private void buildFromUser(ChatState.Builder builder, String user, CommChannel commChannel) {
                builder.addStateBuilder().setAction(ChatAction.ActionType.JOIN).
                        setChannel(commChannel.getName()).setNickname(user);
            }

            @Override
            public void setState(java.io.InputStream input) throws Exception {
                System.out.println("Synchronizing new client");

                ChatState state  = ChatState.parseFrom(input);

                for (ChatAction action : state.getStateList()) {
                    updateChannelFromAction(action);
                }

            }

            private void updateChannelFromAction(ChatAction action) {
                String channel = action.getChannel();
                String nick = action.getNickname();

                int channelNumber = retrieveNumber(channel);
                CommChannel commChannel;
                if (!channels.containsKey(channelNumber)) {
                    client.addChannel(channelNumber);
                }
                commChannel = channels.get(channelNumber);
                if (action.getAction() == ChatAction.ActionType.JOIN) {
                    if (!commChannel.users.contains(action.getNickname())) {
                        commChannel.users.add(action.getNickname());
                    }
                } else {
                    if (commChannel.users.contains(action.getNickname())) {
                        commChannel.users.remove(action.getNickname());
                    }
                }
            }

            public void viewAccepted(View view) {
                super.viewAccepted(view);
            }

            public void receive(Message msg) {
                    try {
                        updateChannelFromAction(ChatAction.parseFrom(msg.getBuffer()));
                    } catch (InvalidProtocolBufferException e) {
                        System.err.println("Invalid message received");
                        e.printStackTrace();
                    }
            }

            private int retrieveNumber(String name) {
                String[] split = name.split("\\.");
                return Integer.parseInt(split[split.length-1]);
            }
        });
    }

    protected Protocol udp() throws UnknownHostException {
        UDP udp = new UDP();
        return udp;
    }

}
