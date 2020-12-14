package Internet;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private final int port;
    private final String host;
    private final String URL;
    public  Client(String h, int p,String u)
    {
        this.port=p;
        this.host=h;
        this.URL=u;
    }
    public void GETrequest()
    {
        try
        {
            Socket socket = new Socket(this.host, this.port);
            PrintStream request = new PrintStream(socket.getOutputStream());
            Scanner response = new Scanner(socket.getInputStream());
            String Grequest = "GET /" + URL + " HTTP/1.1\r\nHost: " + this.host + "\r\nConnection: close\r\n\r\n\"";
            System.out.println(Grequest);
            request.println(Grequest);{
                while (response.hasNextLine()) {
                    System.out.println(response.nextLine());
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Host not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
