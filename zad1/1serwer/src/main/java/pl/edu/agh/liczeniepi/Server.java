package pl.edu.agh.liczeniepi;

import java.net.*;
import java.io.*;

public class Server extends Thread
{
    private static boolean serverContinue = true;
    private Socket clientSocket;

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
                System.exit(1);
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
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader( clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {
                System.out.println ("Received: " + inputLine);

                long index = Long.parseLong(inputLine);

                String response = String.valueOf(generator.getDecimalDigit(index));

                System.out.println(String.format("Responding with %d decimal of pi : %s ", index, response));

                System.out.flush();

                out.write(response);
            }

            out.close();
            in.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            System.err.println("Problem with Communication Server");
            System.exit(1);
        }
    }
}
