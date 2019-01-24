/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thserv;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Random;

/**
 *
 * @author Enyo
 */
public class ThServ {

    private static ServerSocket server = null;
    private static Socket client = null;
    private static volatile int[][] coords = new int[6][2];
    private static final int MAXCONNS = 2;
    //private static volatile ClientThread[] threads = new ClientThread[MAXCONNS];
    private static ObjectOutputStream[] ooss = new ObjectOutputStream[MAXCONNS];
    private static ObjectInputStream[] oiss = new ObjectInputStream[MAXCONNS];
    private static final int MAX = 500;
    private static final int MIN = 100;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int port = 8887;
        coords[4][0] = -1; //id
        coords[5][0] = -1; //id
        coords[4][1] = 0; //score
        coords[5][1] = 0; //score
        RandPos rp = new RandPos(); //rand position of targets
        rp.start();
        try {
            server = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println(e);
        }
        int id = 0;
        while (true) {
            try {
                client = server.accept();
                for (int i = 0; i < MAXCONNS; i++) {
                    //if (threads[i] == null && ooss[i] == null) {
                    if (ooss[i] == null) {
                        ooss[i] = new ObjectOutputStream(client.getOutputStream());
                        oiss[i] = new ObjectInputStream(client.getInputStream());
                        //threads[i] = new ClientThread(client, ooss, oiss, id, coords);
                        (new ClientThread(client, ooss, oiss, id, coords)).start();
                        if(coords[4][0] == -1) {
                            coords[4][0] = id;
                        } else { 
                            coords[5][0] = id;
                        }
                        //threads[i].start();
                        id++;
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    static class RandPos extends Thread {

        @Override
        public void run() {
            Random rand = new Random();
            while (true) {
                coords[0][0] = rand.nextInt(MAX - MIN + 1) + MIN;
                coords[0][1] = rand.nextInt(MAX - MIN + 1) + MIN;
                coords[1][0] = rand.nextInt(MAX - MIN + 1) + MIN;
                coords[1][1] = rand.nextInt(MAX - MIN + 1) + MIN;
                coords[2][0] = rand.nextInt(MAX - MIN + 1) + MIN;
                coords[2][1] = rand.nextInt(MAX - MIN + 1) + MIN;
                coords[3][0] = rand.nextInt(MAX - MIN + 1) + MIN;
                coords[3][1] = rand.nextInt(MAX - MIN + 1) + MIN;
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }
    }
}
