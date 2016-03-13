package pl.edu.agh.liczeniepi;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;

public class Server extends Thread
{
    private static boolean serverContinue = true;
    private Socket clientSocket;

    public static final long MAX_INDEX = 5000000;

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
                    serverSocket.setSoTimeout(TIMEOUT*100);
                    System.out.println ("Waiting for Connection");
                    try {
                        new Server(serverSocket.accept());
                    }
                    catch (SocketTimeoutException ste)
                    {
                        System.out.println ("Main thread timeout Occurred");
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
        System.out.println ("New Communication Thread Started with: " + clientSocket.getInetAddress());

        try {
            OutputStream out = clientSocket.getOutputStream();
            InputStream in = clientSocket.getInputStream();

            byte[] input = new byte[9];

            if (in.read(input) > 0)
            {
                long index = parseInput(input);

                System.out.println ("Received: " + index);

                byte response;

                if (index <= MAX_INDEX) {
                    if (index > 0) {
                      response = (byte) generator.getDecimalDigit(index);

                      System.out.println(String.format("Responding with %d decimal of pi : %s ", index, (char)response));

                    } else {
                        response = '-';
                        System.out.println("Responding with - to signal bad argument");
                    }
                } else {
                    response='?';
                    System.out.println("Responding with ? to signal that the index requested is too big");
                }

                out.write(response);

            }

            out.close();
            in.close();
            clientSocket.close();


            System.out.println("Thread " + this.getId() + " shutting down");

            this.join();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private long parseInput(byte[] bytes) {
        return ByteUtils.bytesToLong(bytes);
    }
}
