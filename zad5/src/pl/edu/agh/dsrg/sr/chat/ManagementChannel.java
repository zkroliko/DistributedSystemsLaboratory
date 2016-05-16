package pl.edu.agh.dsrg.sr.chat;

import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.UDP;
import org.jgroups.stack.Protocol;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

public class ManagementChannel extends Channel {

    public static final String NAME = "ChatManagement321123";

    private Map<Integer, CommChannel> channels;

    private ChatClient client;

    public ManagementChannel(String ownName, ChatClient client) {
        super(NAME, ownName);
        buildChannel();
        this.channels = client.getChannels();
        this.client = client;
    }

    public void sendJoin(String channelName) throws Exception {
        ChatOperationProtos.ChatAction action;
        action = ChatOperationProtos.ChatAction.newBuilder().
                setAction(ChatOperationProtos.ChatAction.ActionType.JOIN).
                setChannel(channelName).
                setNickname(ownName).build();
        channel.send(new Message(null, null, action.toByteArray()));
    }

    public void sendLeave(String channelName) throws Exception {
        ChatOperationProtos.ChatAction action;
        action = ChatOperationProtos.ChatAction.newBuilder().
                setAction(ChatOperationProtos.ChatAction.ActionType.LEAVE).
                setChannel(channelName).
                setNickname(ownName).build();
        channel.send(new Message(null, null, action.toByteArray()));
    }

    protected void setUpReceiver() {
        channel.setReceiver(new ReceiverAdapter() {

            @Override
            public void getState(OutputStream output) throws Exception {
                System.out.println("Synchronizing new client");
                for (CommChannel c : channels.values()) {
                    List<String> users = c.users;
                    for (String user : users) {
                        ChatOperationProtos.ChatAction action;
                        action = ChatOperationProtos.ChatAction.newBuilder().
                                setAction(ChatOperationProtos.ChatAction.ActionType.JOIN).
                                setChannel(c.name).setNickname(user).build();
                        channel.send(new Message(null, null, action.toByteArray()));
                    }
                }
            }

            @Override
            public void setState(java.io.InputStream input) throws Exception {
                System.out.println("State synchronized");
            }

            public void viewAccepted(View view) {
                super.viewAccepted(view);
            }
            public void receive(Message msg) {
                    try {
                        handleAction(ChatOperationProtos.ChatAction.parseFrom(msg.getBuffer()));
                    } catch (InvalidProtocolBufferException e) {
                        System.err.println("Invalid message received");
                        e.printStackTrace();
                    }
            }

            private int retrieveNumber(String name) {
                String[] split = name.split("\\.");
                return Integer.parseInt(split[split.length-1]);
            }

            private void handleAction(ChatOperationProtos.ChatAction action) {
                int channelNumber = retrieveNumber(action.getChannel());
                CommChannel channel;
                if (!channels.containsKey(channelNumber)) {
                    client.joinChannel(channelNumber);
                }
                channel = channels.get(channelNumber);
                if (action.getAction() == ChatOperationProtos.ChatAction.ActionType.JOIN) {
                    if (!channel.users.contains(action.getNickname())) {
                        System.out.println("Adding user" + action.getNickname());
                        channel.users.add(action.getNickname());
                    }
                } else {
                    if (channel.users.contains(action.getNickname())) {
                        System.out.println("Removing user" + action.getNickname());
                        channel.users.remove(action.getNickname());
                    }
                }
            }
        });
    }

    protected Protocol udp() throws UnknownHostException {
        UDP udp = new UDP();
        udp.setValue("mcast_port", 6789);
        return udp;
    }

}
