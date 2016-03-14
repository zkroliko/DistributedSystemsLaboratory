package pl.edu.agh.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chat implements Runnable {

    public static final String ELECT_CODE = "ELECT";

    public static final String CONFIRM_CODE = "CONFIRM";

    public static final int ELECT_MAX_TRY = 5;

    public static final int ELECT_WAIT_TIME = 100;

    protected InetAddress group;
    protected int port;
    private String userNick;

    protected Thread listener;

    protected MulticastSocket socket;
    protected DatagramPacket out, in;

    private String leaderNick;
    private boolean leaderConfirmed;
    private boolean isLeader;

    byte[] outBuffer;

    private List<String> userList;

    public Chat(InetAddress group, int port, String userNick) {
        this.group = group;
        this.port = port;
        this.userNick = userNick;
        userList = new ArrayList<String>();
    }

    private void chat() {
        // Adding local node to the list
        userList.add(userNick);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome "+ userNick);

        while (true) {
            try {
                String toTransmit = reader.readLine();

                Message msg;

                if (toTransmit.equals("elect")) {
                    makeElection();
                    toTransmit = ELECT_CODE;
                    msg = new Message(leaderNick, toTransmit);
                    outBuffer = msg.getByteBuffer();
                    transmit();
                } else {
                    msg = new Message(userNick, toTransmit);
                    outBuffer = msg.getByteBuffer();
                    transmit();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void transmit() throws IOException {
        out.setData(outBuffer);
        out.setLength(outBuffer.length);
        socket.send(out);
    }

    private void makeElection() {
        Random rand = new Random();
        if (userList.size() == 1) {
            System.out.println("Not aware of any nodes except this one - will self-elect");
        }
        int leaderIndex = rand.nextInt(userList.size());
        leaderNick = userList.get(leaderIndex);
    }

    public synchronized void join() throws IOException {
        if (listener == null) {
            socket = new MulticastSocket(port);
            socket.setTimeToLive(5);
            socket.joinGroup(group);

            out = new DatagramPacket(new byte[48], 48, group, port);
            in = new DatagramPacket(new byte[48], 48);

            // Listener thread
            listener = new Thread(this);
            listener.start();

            // Sending
            chat();
        }
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                in.setLength(in.getData().length);
                // Receiving data
                socket.receive(in);
                String msg = new String(in.getData(), 0, in.getLength(), "UTF8");
                // Checking checksum
                if (Message.checkChecksum(msg)) {
                    System.out.println("Bad checksum in the datagram.");
                    continue;
                }
                // Adding to user list
                if (!userList.contains(Message.getNickname(msg)))
                    userList.add(Message.getNickname(msg));
                // Receiving election
                if (Message.getText(msg).equals(ELECT_CODE))
                    leaderNick = Message.getNickname(msg);
                // Checking whether this node hasn't been selected as leader
                if (Message.checkNickname(userNick, msg)){
                    if (leaderNick != null && Message.getNickname(msg).equals(leaderNick)) {
                        if (Message.getText(msg).equals(CONFIRM_CODE)) {
                            // Leader confirmation
                            leaderConfirmed = true;
                            System.out.println("Leader confirmed: " + Message.convertToMsg(msg));
                        } else {
                            System.out.println("LEADER ELECTED: " + Message.convertToMsg(msg));
                        }
                    } else {
                        System.out.println(Message.convertToMsg(msg));
                    }
                } else {
                    if (Message.getText(msg).equals(ELECT_CODE)) {
                        System.out.println("You have been selected as leader: " + Message.convertToMsg(msg));
                        confirmElected();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmElected() {
        try {
            Message msg = new Message(leaderNick, CONFIRM_CODE);
            outBuffer = msg.getByteBuffer();
            transmit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage: Chat <ip> <port> <nickname>");
        }
        try {
            InetAddress group = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);

            String nickname = args[2];
            if (nickname.length() > 6) {
                System.out.println("Username too long, will continue!");
                nickname = nickname.substring(0,5);
            }

            Chat chat = new Chat(group, port, nickname);
            chat.join();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
