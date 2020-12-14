package edu.spbu.matrix;

import Internet.Client;
import org.junit.Test;



public class ClientTest {
    @Test
    public void mulDD() {
        Client cl1=new Client("podvignaroda.ru",80,"index.html");
        cl1.GETrequest();
    }
}
