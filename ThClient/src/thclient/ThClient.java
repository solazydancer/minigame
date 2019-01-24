/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thclient;

import java.net.Socket;
import java.io.*;

/**
 *
 * @author Enyo
 */
public class ThClient {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Socket client = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        int port = 8887;
        String machine = "localhost";
        try {
            client = new Socket(machine, port);
            oos = new ObjectOutputStream(client.getOutputStream());
            ois = new ObjectInputStream(client.getInputStream());
            Graph g = new Graph(oos, ois);
            g.mainMethod();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
