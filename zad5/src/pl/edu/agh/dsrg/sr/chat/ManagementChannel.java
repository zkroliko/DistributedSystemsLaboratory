package pl.edu.agh.dsrg.sr.chat;

import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.UDP;
import org.jgroups.stack.Protocol;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

import java.net.UnknownHostException;
import java.util.Map;

public class ManagementChannel extends Channel {

    public static final String NAME = "ChatManagement321123";

    private Map<Integer, NormalChannel> channels;

    public ManagementChannel(Map<Integer, NormalChannel> channels) {
        super(NAME);
        this.channels = channels;
    }

    public void sendJoin(String nickname, String channelName) throws Exception {
        ChatOperationProtos.ChatAction action;
        action = ChatOperationProtos.ChatAction.newBuilder().
                setAction(ChatOperationProtos.ChatAction.ActionType.JOIN).
                setChannel(channelName).
                setNickname(nickname).build();
        channel.send(new Message(null, null, action.toByteArray()));
    }

    public void sendLeave(String nickname, String channelName) throws Exception {
        ChatOperationProtos.ChatAction action;
        action = ChatOperationProtos.ChatAction.newBuilder().
                setAction(ChatOperationProtos.ChatAction.ActionType.LEAVE).
                setChannel(channelName).
                setNickname(nickname).build();
        channel.send(new Message(null, null, action.toByteArray()));
    }

    private void setUpReceiver() {
        channel.setReceiver(new ReceiverAdapter() {
            public void viewAccepted(View view) {
                super.viewAccepted(view);
            }
            public void receive(Message msg) {

            }
        });
    }

    protected Protocol udp() throws UnknownHostException {
        UDP udp = new UDP();
        udp.setValue("mcast_port", 6789);
        return udp;
    }

}
