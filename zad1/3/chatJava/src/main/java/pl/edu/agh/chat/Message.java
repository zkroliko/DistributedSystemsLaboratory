package pl.edu.agh.chat;

import java.util.Calendar;

public class Message {

    private String nickname;
    private String text;
    private Calendar now;
    private int checksum;

    public Message(String nickname, String text) {
        this.nickname = nickname;
        this.text = text;
        now = Calendar.getInstance();
        checksum = (nickname + text + now.get(Calendar.HOUR_OF_DAY) + ":" + Calendar.MINUTE + ":" + Calendar.SECOND).hashCode();
    }

    public byte[] getByteBuffer() {
        byte buffer[] = new byte[48];
        StringBuffer strBuf = new StringBuffer();
        if (nickname.length() < 6) {
            strBuf.append(nickname);
            for (int i = nickname.length(); i < 6; i++) strBuf.append("-");
        } else {
            strBuf.append(nickname, 0, 6);
        }
        if (text.length() < 20) {
            strBuf.append(text);
            for (int i = text.length(); i < 20; i++) strBuf.append("-");
        } else {
            strBuf.append(text, 0, 20);
        }
        String time = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
        strBuf.append(time);
        strBuf.append(String.valueOf(checksum));

        for (int i = strBuf.length(); i < 48; i++) strBuf.append("-");
        buffer = strBuf.toString().getBytes();
        return buffer;
    }

    public static String convertToMsg(String msg) {
        String nick = msg.substring(0, 6).split("-")[0];
        String txt = msg.substring(6, 26).split("-")[0];
        String time = msg.substring(26, 34).split("-")[0];
        return nick +  " - " + time + " : " + txt ;
    }

    public static boolean checkNickname(String nickname, String msg) {
        String nick = msg.substring(0,6).split("-")[0];
        return !nick.equals(nickname);
    }

    public static boolean checkChecksum(String msg) {
        String nick = msg.substring(0, 5).split("-")[0];
        String txt = msg.substring(6, 25).split("-")[0];
        String time = msg.substring(26, 33).split("-")[0];
        String crcStr = msg.substring(34,47);
        int crc;
        if (crcStr.startsWith("-")) crc = Integer.parseInt(crcStr.split("-")[1]);
        else crc = Integer.parseInt(crcStr.split("-")[0]);
        int crcFromMsg = (nick + txt + time.split(":")[0] + ":" + time.split(":")[1] + ":" + time.split(":")[2]).hashCode();
        return crc == crcFromMsg;
    }

    public static String getNickname(String msg) {
        return msg.substring(0, 6).split("-")[0];
    }

    public static String getText(String msg) {
        return msg.substring(6, 26).split("-")[0];
    }

}
