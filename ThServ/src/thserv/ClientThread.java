/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thserv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Enyo
 */
class ClientThread extends Thread {

    private ObjectInputStream ois = null;
    private Socket client = null;
   // private ClientThread[] threads;
    private final int MAXCONNS = 2;
    private ObjectOutputStream oos = null;
    private ObjectOutputStream[] ooss;
    private ObjectInputStream[] oiss;
    private int id;
    private int[][] coords;

    public ClientThread(Socket client, ObjectOutputStream[] ooss, ObjectInputStream[] oiss, int id, int[][] coords) {
        this.client = client;
     //   this.threads = threads;
     //   this.MAXCONNS = threads.length;
        this.ooss = ooss;
        this.oiss = oiss;
        this.id = id;
        this.coords = coords;
    }

    @Override
    public void run() {
        try {
            oos = ooss[id];
            ois = oiss[id];
            Rcv rcv = new Rcv();
            rcv.start();
            oos.writeObject(coords);//инициализация
            while (true) {
                //if(coords[4][1] >= 30 || coords[5][1] >= 30)
                //    win = true;
                int[][] temp = new int[6][2];
                for (int i = 0; i < 6; i++) {
                    for(int j = 0; j < 2; j++) {
                        temp[i][j] = coords[i][j];
                    }
                }
                
                synchronized (ooss) {
                    for (int i = 0; i < MAXCONNS; i++) {
                        if (ooss[i] != null) {
                            ooss[i].writeObject(temp);
                            ooss[i].flush();
                        }
                    }
                }
            }
        } catch (Exception e) {
            //System.out.println(e);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                    oiss[id].close();
                }
                if (oos != null) {
                    oos.close();
                    ooss[id].close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    class Rcv extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    int[] temp = new int[2];
                    temp[0] = ((int[]) obj)[0];//x click
                    temp[1] = ((int[]) obj)[1]; //y click
                    for (int i = 0; i < 4; i++) {
                        int rad_dot = (temp[0] - coords[i][0]) * (temp[0] - coords[i][0]) + (temp[1] - coords[i][1]) * (temp[1] - coords[i][1]);
                        int rad_tar = 900; //30*30
                        if(coords[4][0] >= 0 && coords[5][0] >= 0) {
                        if (rad_dot < rad_tar) {
                            coords[i][0] = -1;
                            if(id == coords[4][0])
                                coords[4][1] = coords[4][1] + 10;
                            else
                                coords[5][1] = coords[5][1] + 10;
                        }
                        }
                    }
                    //}
                    if(coords[4][1] >= 30 || coords[5][1] >= 30)
                        break;
                      //  win = true;
                } catch (Exception e) {
                   // System.out.println(e);
                }
            }
        }
    }
}
