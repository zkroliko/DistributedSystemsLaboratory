package pl.edu.agh.dsrg.sr.chat;

import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.*;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.Protocol;
import org.jgroups.stack.ProtocolStack;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class CommChannel extends Channel {

    public static final String NAME_FORMAT = "230.0.0.%s";

    private String channelName;

    public List<String> users = new LinkedList<>();

    private ManagementChannel management;

    public CommChannel(String nickname, String number, ManagementChannel management) {
        super(number, nickname);
        this.management = management;
        channelName = String.format(NAME_FORMAT,number);
        buildChannel();
        try {
            management.sendJoin(channelName);
        } catch (Exception e) {
            System.err.println("Joining channel failed: " + channelName);
            e.printStackTrace();
        }
    }

    protected void setUpReceiver() {
        channel.setReceiver(new ReceiverAdapter() {
            public void viewAccepted(View view) {
                super.viewAccepted(view);
            }
            public void receive(Message msg) {
                try {
                    ChatOperationProtos.ChatMessage action = ChatOperationProtos.ChatMessage.parseFrom(msg.getBuffer());
                    System.out.println(String.format("%s: %s", msg.getSrc(), action.getMessage()));
                } catch (InvalidProtocolBufferException e) {
                    System.err.println("Invalid message received");
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendMessage(String message) throws Exception {
        ChatOperationProtos.ChatMessage chatMessage;
        chatMessage = ChatOperationProtos.ChatMessage.newBuilder().setMessage(message).build();

        channel.send(new Message(null, null, chatMessage.toByteArray()));
    }

    protected Protocol udp() throws UnknownHostException {
        UDP udp = new UDP();
        udp.setValue("mcast_group_addr", InetAddress.getByName(channelName));
        udp.setValue("mcast_port", 6789);
        return udp;
    }
}
