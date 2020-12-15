package Internet;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Server {
    private final int port;
    private final String directory;
    public Server(int p, String d)
    {
        this.port=p;
        this.directory=d;
    }
    public void ServerStart()
    {
        try
        {
            ServerSocket server=new ServerSocket(port);
            while (!ServerDisable.need){
                Socket client = server.accept();
                PrintStream responseStream = new PrintStream(client.getOutputStream());
                Scanner requestStream = new Scanner(client.getInputStream());
                if (requestStream.hasNextLine()) {
                    String URL = RequestedFile(requestStream);
                    File f = new File(directory + URL);
                    if (f.exists()) {
                        int SuccessCode = 200;
                        String SuccessMessage = "OK";
                        this.sendResponse(responseStream, SuccessCode, SuccessMessage, "text/html", f.length());
                        Scanner FileScan = new Scanner(f);
                        while (FileScan.hasNextLine()) {
                            String s = FileScan.nextLine();
                            responseStream.println(s);
                        }
                    } else {
                        String nf = "Not Found";
                        int l = 8;
                        this.sendResponse(responseStream, 404, nf, "text/plain", l);
                        responseStream.println("Not Found");
                    }
                    client.close();
                    responseStream.flush();
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Host not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendResponse(PrintStream w, int statusCode, String statusText, String type, long size)
    {
        w.printf("HTTP/1.1 %s %s%n", statusCode, statusText);
        w.printf("Content-Type: %s%n", type);
        w.printf("Content-Length: %s%n%n", size);
    }
    private String RequestedFile(Scanner a)
    {
        String request = a.nextLine();
        return request.split(" ")[1];
    }
}
