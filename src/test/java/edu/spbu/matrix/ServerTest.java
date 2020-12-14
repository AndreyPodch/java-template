package edu.spbu.matrix;

import Internet.Client;
import Internet.Server;
import org.junit.Test;



public class ServerTest {
    @Test
    public void serverRun() throws Exception {
        class ServerParallelRunning implements Runnable {

            @Override
            public void run() {
                Server sr1 = new Server(80, "./src/main/");
                sr1.ServerStart();
            }
        }
        Thread th = new Thread(new ServerParallelRunning());
        th.start();
        System.out.println("HEW");
        Client cl1 = new Client("localhost", 80, "m1.txt");
        cl1.GETrequest();
        th.join();
    }
}
