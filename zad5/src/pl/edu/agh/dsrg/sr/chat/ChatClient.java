package pl.edu.agh.dsrg.sr.chat;

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

    public static final String HELP = "Użycie: chatClient | chatClient <nick> | chatClient <nick> <channel>";


    private String nick;

    private List<Integer> channels;

    public ChatClient() {
        new ChatClient(getNick());
    }

    public ChatClient(String nickname) {
        new ChatClient(nickname, getChannel());
    }

    public ChatClient(String nickname, int channel) {
        channels = new LinkedList<>();

        nick = nickname;

        if (!legalChannel(channel)) {
            channels.add(getChannel());
        } else {
            channels.add(channel);
        }

        joinChannels();

    }

    private String getNick() {
        Scanner s = new Scanner(System.in);
        System.out.println(NICK_ASK_MESSAGE);
        return s.nextLine();
    }

    private int getChannel() {
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

    private void joinChannels() {
        for (int channel : channels) {
            joinChannel(channel);
        }
    }

    private void joinChannel(int channel) {
        System.out.println(CHANNEL_HAVE_MESSAGE + channel);
    }

    public boolean legalChannel(int channel) {
        return channel >= MIN_CHANNEL && channel <= MAX_CHANNEL;
    }

    public static void main(String[] args) {
//        ChatOperationProtos.ChatAction action = ChatOperationProtos.ChatAction.newBuilder().
//                setAction(ChatOperationProtos.ChatAction.ActionType.JOIN).setChannel("2133").setNickname("aaa").build();

        ChatClient client;

        if (args.length == 0) {
            client = new ChatClient();
        } else if (args.length == 1) {
            System.out.println(NICK_HAVE_MESSAGE + args[0]);
            client = new ChatClient(args[0]);
        } else if (args.length == 2) {
            client = new ChatClient(args[0],Integer.parseInt(args[1]));
        } else {
            System.err.println(HELP);
        }


    }
}
