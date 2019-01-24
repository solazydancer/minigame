/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thclient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Enyo
 */
public class Graph extends JPanel {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final JFrame frame;
    private int x;
    private int y;
    private int[][] targets;
    private int x0;
    private int y0;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int x3;
    private int y3;
    private int score1;
    private int id1;
    private int score2;
    private int id2;

    public Graph(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
        frame = new JFrame("Main Window");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                int[] temp = new int[2];
                temp[0] = x;
                temp[1] = y;
                try {
                    oos.writeObject(temp);
                    oos.flush();
                } catch (Exception exc) {
                    System.out.println(exc);
                }
            }
        });
    }

    public void mainMethod() throws Exception {
        Graph gr = new Graph(oos, ois);
        frame.add(gr);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setVisible(true);
        Object obj = ois.readObject();//инициализация
        while (true) {
            obj = ois.readObject();
            targets = (int[][]) obj;
            gr.x0 = targets[0][0];
            gr.y0 = targets[0][1];
            gr.x1 = targets[1][0];
            gr.y1 = targets[1][1];
            gr.x2 = targets[2][0];
            gr.y2 = targets[2][1];
            gr.x3 = targets[3][0];
            gr.y3 = targets[3][1];
            gr.score1 = targets[4][1];
            gr.id1 = targets[4][0];
            gr.score2 = targets[5][1];
            gr.id2 = targets[5][0];
            //if(score1 <= 30 && score2 <= 30)
            gr.repaint();
            if(gr.score1 >= 30 || gr.score2 >= 30)
                break;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        this.setBackground(Color.WHITE);
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try {
            BufferedImage img = ImageIO.read(new File("img.gif"));
             if (x0 != -1) {
                 g2.drawImage(img, null, x0, y0);
      //      g2.fillOval(x0, y0, 30, 30);
        }
        if (x1 != -1) {
            g2.drawImage(img, null, x1, y1);
     //       g2.fillOval(x1, y1, 30, 30);
        }
        if (x2 != -1) {
            g2.drawImage(img, null, x2, y2);
      //      g2.fillOval(x2, y2, 30, 30);
        }
        if (x3 != -1) {
      //      g2.fillOval(x3, y3, 30, 30);
        
            g2.drawImage(img, null, x3, y3);
        }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
       // if (x0 != -1) {
      //      g2.fillOval(x0, y0, 30, 30);
      //  }
      //  if (x1 != -1) {
     //       g2.fillOval(x1, y1, 30, 30);
      //  }
      //  if (x2 != -1) {
      //      g2.fillOval(x2, y2, 30, 30);
      //  }
      //  if (x3 != -1) {
      //      g2.fillOval(x3, y3, 30, 30);
      //  }
        if(id1 != -1)
            g2.drawString("Player " + id1 + ": " + score1, 50, 50);
        else 
            g2.drawString("Not connected", 50, 50);
        if(id2 != -1)
        g2.drawString("Player " + id2 + ": " + score2, 600, 50);
        else
            g2.drawString("Not connected", 600, 50);
        if(score1 == 30) {
            g2.drawString("Player " + id1 + " wins!", 350, 350);
        }
        if(score2 == 30) {
            g2.drawString("Player " + id2 + " wins!", 350, 350);
        }
        if(id1 == -1 || id2 == -1)
            g2.drawString("Waiting for other player to connect. You can't get any points.", 250, 350);
    }
}
