/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netprojservertest;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Abir
 */
public class NetProjServerTest {
    
    static ServerSocket serverSocket;
    static Socket socket;
    static DataOutputStream out;
    static DataInputStream in;
    static Users[] user = new Users[10];
    
    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        System.out.println("Starting Server");
        serverSocket = new ServerSocket(7777);
        System.out.println("Server Started");  
        while(true) {
            socket = serverSocket.accept();
            
            for(int i=0; i<10; i++) {
            System.out.println("Connection from: " + socket.getInetAddress());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("A test of Java Socket");
            System.out.println("Data sent");
            in = new DataInputStream(socket.getInputStream());
            if(user[i] == null) {
                user[i] = new Users(out, in, user);
                Thread  thread = new Thread(user[i]);
                thread.start();
                break;
            }
            }
        }
    }
    
}


class Users implements Runnable {
    
    DataOutputStream out;
    DataInputStream in;
    Users[] user = new Users[10];
    String name;
    
    public Users(DataOutputStream dos, DataInputStream dis, Users[] users) {
        this.out = dos;
        this.in = dis;
        this.user = users;
    }
    
    public void run() {
        try {
            name = in.readUTF();
        } catch (IOException ex) {
//            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
              ex.printStackTrace();
        }
        while(true) {
            try {
                String msg = in.readUTF();
                for(int i=0; i<10; i++) {
                    if(user[i] != null ) {
                        user[i].out.writeUTF(name + ": " + msg);
                    }
                }
            } catch (IOException ex) {
                this.out = null;
                this.in = null;
            }
        }
    }
}

