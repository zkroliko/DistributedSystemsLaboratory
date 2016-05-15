package pl.edu.agh.dsrg.sr.chat;

import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.Protocol;
import org.jgroups.stack.ProtocolStack;
import java.net.UnknownHostException;

public abstract class Channel {

    protected JChannel channel;

    protected String name;

    public Channel(String name) {
        this.name = name;
        try {
            channel = new JChannel(false);
            channel.setName(name);
            channel.setProtocolStack(stack(udp()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setReceiver(ReceiverAdapter receiver) {
        channel.setReceiver(receiver);
    }

    public void connect() throws Exception {
        channel.getProtocolStack().init();
        channel.connect("ChatCluster");
    }

    public void close() throws Exception {
        channel.close();
    }

    protected abstract Protocol udp() throws UnknownHostException;

    protected ProtocolStack stack(Protocol udp) throws Exception {
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
        return stack;
    }
}
