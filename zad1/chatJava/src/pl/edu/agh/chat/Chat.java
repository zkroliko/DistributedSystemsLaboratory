package pl.edu.agh.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chat implements Runnable {

    protected InetAddress group;
    protected int port;
    private String userNick;

    protected Thread listener;

    protected MulticastSocket socket;
    protected DatagramPacket out, in;

    private String leaderNick;
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome "+ userNick);

        while (true) {
            try {
                String toTransmit = reader.readLine();

                Message msg;

                if (toTransmit.equals("elect")) {
                    makeElection();
                    toTransmit = "ELECT";
                    msg = new Message(leaderNick, toTransmit);
                    outBuffer = msg.getByteBuffer();
                    out.setData(outBuffer);
                    out.setLength(outBuffer.length);
                    socket.send(out);

                } else {
                    msg = new Message(userNick, toTransmit);
                    outBuffer = msg.getByteBuffer();
                    out.setData(outBuffer);
                    out.setLength(outBuffer.length);
                    socket.send(out);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void makeElection() {
        Random rand = new Random();
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

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                in.setLength(in.getData().length);
                // Receiving data
                socket.receive(in);
                String msg = new String(in.getData(), 0, in.getLength(), "UTF8");
                if (!userList.contains(Message.getNickname(msg))) userList.add(Message.getNickname(msg));
                if (Message.getText(msg).equals("E")) leaderNick = Message.getNickname(msg);
                if (Message.checkCRC(msg)) {
                    System.out.println("Bad checksum in the datagram.");
                    continue;
                }
                if (Message.checkNickname(userNick, msg)){
                    if (leaderNick != null && Message.getNickname(msg).equals(leaderNick)) {
                        System.out.println("LEAD: \n" + Message.convertToMsg(msg));
                    } else {
                        System.out.println(Message.convertToMsg(msg));
                    }
                }
            }
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
                System.out.println("Username to long, will continue!");
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
