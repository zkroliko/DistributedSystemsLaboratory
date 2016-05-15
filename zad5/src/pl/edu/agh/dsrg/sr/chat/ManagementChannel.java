package pl.edu.agh.dsrg.sr.chat;

import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.ReceiverAdapter;
import org.jgroups.protocols.UDP;
import org.jgroups.stack.Protocol;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ManagementChannel extends Channel {

    public static final String NAME = "ChatManagement321123";

    public ManagementChannel() {
        super(NAME);
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

    private void setReceiver(Receiver receiver) {
        channel.setReceiver(receiver);
    }

    protected Protocol udp() throws UnknownHostException {
        UDP udp = new UDP();
        udp.setValue("mcast_port", 6789);
        return udp;
    }

}
