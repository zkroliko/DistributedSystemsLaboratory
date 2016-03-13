package pl.edu.agh.liczeniepi;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;

public class Server extends Thread
{
    private static boolean serverContinue = true;
    private Socket clientSocket;

    public static final long MAX_INDEX = 1000000;

    private PiDigitsGenerator generator;

    public static final int PORT = 5000;

    public static final int TIMEOUT = 10000;

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println ("Connection Socket Created");
            try {
                while (serverContinue)
                {
                    serverSocket.setSoTimeout(TIMEOUT);
                    System.out.println ("Waiting for Connection");
                    try {
                        new Server(serverSocket.accept());
                    }
                    catch (SocketTimeoutException ste)
                    {
                        System.out.println ("Timeout Occurred");
                    }
                }
            }
            catch (IOException e)
            {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }
        finally
        {
            try {
                System.out.println ("Closing Server Connection Socket");
                serverSocket.close();
            }
            catch (IOException e)
            {
                System.err.println("Could not close port: " + PORT);
                e.printStackTrace();
            }
        }
    }

    private Server(Socket clientSoc)
    {
        clientSocket = clientSoc;
        generator = new PiDigitsGenerator();
        start();
    }

    public void run()
    {
        System.out.println ("New Communication Thread Started");

        try {
            OutputStream out = clientSocket.getOutputStream();
            InputStream in = clientSocket.getInputStream();

            byte[] input = new byte[9];

            if (in.read(input) > 0)
            {
                long index = parseInput(input);

                System.out.println ("Received: " + index);

                if (index < MAX_INDEX) {

                    byte response = (byte) generator.getDecimalDigit(index);

                    System.out.println(String.format("Responding with %d decimal of pi : %s ", index, String.valueOf(response)));

                    System.out.flush();

                    out.write(response);
                }

            }

            out.close();
            in.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private long parseInput(byte[] bytes) {
        return ByteUtils.bytesToLong(bytes);
    }
}
