package pl.edu.agh.dsrg.sr.chat;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.Protocol;
import org.jgroups.stack.ProtocolStack;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Channel {

    public static final String NAME = "230.0.0.%s";

    private int number;

    private JChannel channel;

    public Channel(int number) {
        this.number = number;
        try {
            channel = new JChannel("/home/bela/udp.xml");
            channel.setProtocolStack(stack(udp(number)));
            channel.setReceiver(new ReceiverAdapter() {
                public void receive(Message msg) {
                    System.out.println("received msg from " + msg.getSrc() + ": " + msg.getObject());
                }
            });
            channel.connect("MyCluster");
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() throws Exception {
        channel.send(new Message(null, "hello world"));
    }

    private Protocol udp(int number) throws UnknownHostException {
        String name = String.format(NAME,number);
        return new UDP().setValue("mcast_group_addr", InetAddress.getByName(NAME));
    }

    private ProtocolStack stack(Protocol udp) throws Exception {
        ProtocolStack stack= new ProtocolStack();

        stack.addProtocol(udp)
                .addProtocol(new PING())
                .addProtocol(new MERGE2())
                .addProtocol(new FD_SOCK())
                .addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
                .addProtocol(new VERIFY_SUSPECT())
                .addProtocol(new BARRIER())
                .addProtocol(new NAKACK())
                .addProtocol(new UNICAST2())
                .addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new UFC())
                .addProtocol(new MFC())
                .addProtocol(new FRAG2())
                .addProtocol(new STATE_TRANSFER())
                .addProtocol(new FLUSH());
        stack.init();
        return stack;
    }
}
