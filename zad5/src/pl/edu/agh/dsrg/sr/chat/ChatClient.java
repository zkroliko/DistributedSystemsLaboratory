package pl.edu.agh.dsrg.sr.chat;

import org.jgroups.JChannel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ChatClient {

    public static final int MIN_CHANNEL = 1;

    public static final int MAX_CHANNEL = 200;

    public static final String NICK_HAVE_MESSAGE = "Twój nick: ";

    public static final String NICK_ASK_MESSAGE = "Podaj nick";

    public static final String CHANNEL_HAVE_MESSAGE = "Dołączasz do kanału: ";

    public static final String CHANNEL_MESSAGE = "Podaj numer kanału pomiędzy " + MIN_CHANNEL + " a " + MAX_CHANNEL;

    public static final String BAD_CHANNEL_MESSAGE = "Zły numer kanału";

    public static final String RUN_HELP = "Użycie: chatClient | chatClient <nick> | chatClient <nick> <channel>";

    public static final String HELP_COMMAND = "help";

    public static final String USE_HELP = "help";

    public static final String EXIT_COMMAND = "exit";

    public static final String JOIN_COMMAND = "join";

    public static final String MSG_COMMAND = "m";

    public static final String LIST_COMMAND = "list";

    public static final String BAD_USE_MESSAGE = "Wprowadzono błędne polecenie";


    private String nick;

    private List<NormalChannel> channels;

    private  ManagementChannel management;

    public ChatClient() {
        new ChatClient(getNick());
    }

    public ChatClient(String nickname) {
        new ChatClient(nickname, getChannelNumber());
    }

    public ChatClient(String nickname, int channelNumber) {
        channels = new LinkedList<>();

        nick = nickname;

        management = new ManagementChannel();
        try {
            management.connect();
        } catch (Exception e) {
            System.err.println("Connecting to management channel failed");
            e.printStackTrace();
        }

        if (!legalChannel(channelNumber)) {
            channelNumber = getChannelNumber();
        } else {
            joinChannel(channelNumber);
        }

        interfaceLoop();

    }

    public void interfaceLoop() {
        Scanner s = new Scanner(System.in);
        String line = s.nextLine();
        while (!line.toLowerCase().contains("exit")) {
            String[] fields = line.split(" ");
            if (line.startsWith(JOIN_COMMAND) && fields.length == 2) {
                try {
                    int channelNumber = Integer.parseInt(fields[1]);
                    if (legalChannel(channelNumber)) {
                        joinChannel(channelNumber);
                    } else {
                        throw new NumberFormatException(BAD_CHANNEL_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                }
            } else if (line.startsWith(LIST_COMMAND) && fields.length == 1) {
                listChannels();
            } else if (line.startsWith(MSG_COMMAND) && fields.length == 3) {
                try {
                    sendMsg(Integer.parseInt(fields[1]), fields[2]);
                } catch (NumberFormatException e) {
                    System.err.println(BAD_USE_MESSAGE);
                }
            } else {
                System.err.println(BAD_USE_MESSAGE);
            }
            line = s.nextLine();
        }
    }

    private void sendMsg(int channelNumber, String message) {

    }

    private void joinChannel(int channelNumber) {
        System.out.println(CHANNEL_HAVE_MESSAGE + channelNumber);
        NormalChannel channel = new NormalChannel(nick, String.valueOf(channelNumber), management);
        channels.add(channel);
        try {
            channel.connect();
        } catch (Exception e) {
            System.err.println("Error connecting to channel: " + channelNumber);
            e.printStackTrace();
        }
    }

    private String getNick() {
        Scanner s = new Scanner(System.in);
        System.out.println(NICK_ASK_MESSAGE);
        return s.nextLine();
    }

    private void listChannels() {
        for (NormalChannel channel : channels) {
            System.out.print(channel.name+": ");
            for (String nick : channel.users) {
                System.out.print(nick+", ");
            }
            System.out.println(Arrays.toString(Character.toChars(8))+" ");
        }
    }

    private int getChannelNumber() {
        Scanner s = new Scanner(System.in);
        int channel = -1;
        while (!legalChannel(channel)) {
            channel = readChannel(s);
        }
        return channel;
    }

    private int readChannel(Scanner s) {
        int channel;
        System.out.println(CHANNEL_MESSAGE);
        channel = s.nextInt();
        if (!legalChannel(channel)) {
            System.out.println(BAD_CHANNEL_MESSAGE);
        }
        return channel;
    }

    public boolean legalChannel(int channel) {
        return channel >= MIN_CHANNEL && channel <= MAX_CHANNEL;
    }

    public static void main(String[] args) {

        ChatClient client;

        if (args.length == 0) {
            client = new ChatClient();
        } else if (args.length == 1) {
            System.out.println(NICK_HAVE_MESSAGE + args[0]);
            client = new ChatClient(args[0]);
        } else if (args.length == 2) {
            client = new ChatClient(args[0],Integer.parseInt(args[1]));
        } else {
            System.err.println(RUN_HELP);
        }


    }
}
