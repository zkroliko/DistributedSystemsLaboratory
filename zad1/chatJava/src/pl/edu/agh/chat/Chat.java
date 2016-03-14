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
    private String myNick;

    protected Thread listener;

    protected MulticastSocket sck;
    protected DatagramPacket out, in;

    private String leaderNickname;
    private boolean isElected;

    byte[] outBuf;

    private List<String> users;


    public Chat(InetAddress group, int port, String myNick) {
        this.group = group;
        this.port = port;
        this.myNick = myNick;
        users = new ArrayList<String>();
    }

    private void chat() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Chat started.");

        while (true) {
            try {
                String toSendMsg = br.readLine();
                ChatMessage chtMsg;
                if (toSendMsg.equals("elect")) {
                    makeElection();
                    toSendMsg = "E";
                    chtMsg = new ChatMessage(leaderNickname, toSendMsg);
                    outBuf = chtMsg.getByteBuffer();
                    out.setData(outBuf);
                    out.setLength(outBuf.length);
                    sck.send(out);
                } else {
                    chtMsg = new ChatMessage(myNick, toSendMsg);
                    outBuf = chtMsg.getByteBuffer();
                    out.setData(outBuf);
                    out.setLength(outBuf.length);
                    sck.send(out);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void makeElection() {
        Random rnd = new Random();
        int electedInd = rnd.nextInt(users.size());
        leaderNickname = users.get(electedInd);
    }

    public synchronized void init() throws IOException {
        if (listener == null) {
            sck = new MulticastSocket(port);
            sck.setTimeToLive(5);
            sck.joinGroup(group);

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
                // receiving data
                sck.receive(in);
                String msg = new String(in.getData(), 0, in.getLength(), "UTF8");
                if (!users.contains(ChatMessage.getNickname(msg))) users.add(ChatMessage.getNickname(msg));
                if (ChatMessage.getText(msg).equals("E")) leaderNickname = ChatMessage.getNickname(msg);
                if (ChatMessage.checkCRC(msg)) {
                    System.out.println("Bad checksum in the datagram.");
                    continue;
                }
                if (ChatMessage.checkNickname(myNick, msg)){
                    if (leaderNickname != null && ChatMessage.getNickname(msg).equals(leaderNickname)) {
                        System.out.println("LEAD: \n" + ChatMessage.convertToMsg(msg));
                    } else {
                        System.out.println(ChatMessage.convertToMsg(msg));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: Chat <ip:port> <nickname>");
        }
        try {
            InetAddress group = InetAddress.getByName(args[0].split(":")[0]);
            int port = Integer.parseInt(args[0].split(":")[1]);
            if (args[1].length() > 6) throw new IllegalArgumentException("Username too long!");
            String userName = args[1];
            Chat sc = new Chat(group, port, userName);
            sc.init();
        } catch (UnknownHostException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
