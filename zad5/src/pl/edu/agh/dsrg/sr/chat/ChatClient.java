package pl.edu.agh.dsrg.sr.chat;

import java.util.*;

public class ChatClient {

    public static final int MIN_CHANNEL = 1;

    public static final int MAX_CHANNEL = 200;

    public static final String NICK_HAVE_MESSAGE = "Twój nick: ";

    public static final String NICK_ASK_MESSAGE = "Podaj nick";

    public static final String EXIT_MESSAGE = "Wyszedłeś z czatu";

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

    private ChannelManager manager;

    public ChatClient() {
        this(getNick());
    }

    public ChatClient(String nickname) {
        manager = new ChannelManager(nickname);
    }

    public ChatClient(String nickname, int channelNumber) {
        this(nickname);

        if (!ChannelManager.legalChannel(channelNumber)) {
            channelNumber = getChannelNumber();
        } else {
            manager.joinChannel(channelNumber);
        }
    }

    public void start() {
        interfaceLoop();
    }

    public void interfaceLoop() {
        Scanner s = new Scanner(System.in);
        String line = s.nextLine();
        while (!(line.toLowerCase().startsWith("exit"))) {
            String[] fields = line.split(" ");
            if (line.startsWith(JOIN_COMMAND) && fields.length == 2) {
                try {
                    int channelNumber = Integer.parseInt(fields[1]);
                    if (ChannelManager.legalChannel(channelNumber)) {
                        manager.joinChannel(channelNumber);
                    } else {
                        throw new NumberFormatException(BAD_CHANNEL_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                }
            } else if (line.startsWith(LIST_COMMAND) && fields.length == 1) {
                printChannels();
            } else if (line.startsWith(MSG_COMMAND) && fields.length == 3) {
                try {
                    int channelNumber = Integer.parseInt(fields[1]);
                    if (ChannelManager.legalChannel(channelNumber)) {
                        manager.sendChatMsg(Integer.parseInt(fields[1]), fields[2]);
                    } else {
                        throw new NumberFormatException(BAD_CHANNEL_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    System.err.println(BAD_USE_MESSAGE);
                } catch (Exception e) {
                    System.err.println("Error in sending message");
                }
            } else {
                System.err.println(BAD_USE_MESSAGE);
            }
            line = s.nextLine();
        }
        System.out.println(EXIT_MESSAGE);
        // Closing all the channels
        try {
            manager.leaveChannels();
        } catch (Exception e) {
            System.err.println("Error leaving the channel");
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void printChannels() {
        System.out.println(manager.getChannelPrintout());
    }

    private static String getNick() {
        Scanner s = new Scanner(System.in);
        System.out.println(NICK_ASK_MESSAGE);
        return s.nextLine();
    }

    private static int getChannelNumber() {
        Scanner s = new Scanner(System.in);
        int channel = -1;
        while (!ChannelManager.legalChannel(channel)) {
            channel = readChannel(s);
        }
        return channel;
    }

    private static int readChannel(Scanner s) {
        int channel;
        System.out.println(CHANNEL_MESSAGE);
        channel = s.nextInt();
        if (!ChannelManager.legalChannel(channel)) {
            System.out.println(BAD_CHANNEL_MESSAGE);
        }
        return channel;
    }

    public static void main(String[] args) {

        ChatClient client = null;

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
        if (client != null) {
            client.start();
        }
    }
}
