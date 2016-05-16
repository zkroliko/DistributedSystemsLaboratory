package pl.edu.agh.dsrg.sr.chat;

import pl.edu.agh.dsrg.sr.chat.CommChannel;
import pl.edu.agh.dsrg.sr.chat.ManagementChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelManager {

    public static final String CHANNEL_HAVE_MESSAGE = "Dołączasz do kanału: ";

    public static final int MIN_CHANNEL = 1;

    public static final int MAX_CHANNEL = 200;

    private String localName;

    private Map<Integer, CommChannel> channels = new ConcurrentHashMap<>();

    private ManagementChannel management;

    public ChannelManager(String localName) {
        this.localName = localName;

        management = new ManagementChannel(localName, this);
        try {
            management.connect();
            management.synchronize();
        } catch (Exception e) {
            System.err.println("Connecting to management channel failed");
            e.printStackTrace();
        }
    }

    public void sendChatMsg(int channelNumber, String message) throws Exception {
        channels.get(channelNumber).sendMessage(message);
    }

    public void joinChannel(int channelNumber) {
        System.out.println(CHANNEL_HAVE_MESSAGE + channelNumber);
        CommChannel channel = new CommChannel(localName, String.valueOf(channelNumber), management);
        channels.put(channelNumber,channel);
        try {
            channel.connect();
        } catch (Exception e) {
            System.err.println("Error connecting to channel: " + channelNumber);
            e.printStackTrace();
        }
    }

    public String getChannelPrintout() {
        StringBuilder result = new StringBuilder();
        for (CommChannel channel : channels.values()) {
            result.append(channel.getName()).append(": ");
            for (String nick : channel.users) {
                result.append(nick).append(", ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public void leaveChannels() throws Exception {
        for (CommChannel channel : channels.values()) {
            leaveChannel(channel);
        }
    }

    private void leaveChannel(CommChannel channel) throws Exception {
        channel.disconnect();
        channel.close();
        management.sendLeave(channel.getName());
    }

    public static boolean legalChannel(int channel) {
        return channel >= MIN_CHANNEL && channel <= MAX_CHANNEL;
    }

    public Map<Integer, CommChannel> getChannels() {
        return channels;
    }

    public ManagementChannel getManagement() {
        return management;
    }
}
