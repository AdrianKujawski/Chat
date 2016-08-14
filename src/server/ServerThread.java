/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class ServerThread extends Thread
{
    private Socket socket = null;
    private ClientOfServer client = null;
    private static ArrayList<ClientOfServer> listOfUser= new ArrayList<ClientOfServer>();
    
    ServerThread(Socket socket)
    {
        this.socket = socket;
    }

    public void run() 
    {
        if (socket.isConnected()) 
        {
            try 
            {
                connectedNewUser(socket);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            try 
            {
                while (true) 
                {
                    // Orzymywanie informacji
                    InputStream in = socket.getInputStream();
                    DataInputStream dataIn = new DataInputStream(in);
                    String text = dataIn.readUTF();
                    System.out.println("Received: " + text);

                    // Wysyłanie informacji
                    String name = compareSocketAndReturnName(socket);
                    sendToAll(text, name);
                }
            }
            catch (Exception ex) 
            {
                try {
                    String name = compareSocketAndReturnName(socket);
                    System.out.println(name + " left server.");
                    sendToAll(name + " left server");
                    listOfUser.remove(client);
                    
                    for (ClientOfServer c : listOfUser) 
                    {
                        System.out.println(c.getName());
                    }
                    
                } catch (Exception ex1) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }
    
    private void connectedNewUser(Socket socket) throws IOException
    {
        
        InputStream in = socket.getInputStream();
        DataInputStream dataIn = new DataInputStream(in);
        
        String text = dataIn.readUTF();
        System.out.println(text + " joined to the chat.");
        
        addNewUser(socket, text);

        OutputStream out = socket.getOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeUTF("You are connected.\r\n");
        
        sendToAll(text + " joined to the chat.");
    }
    
    private void addNewUser(Socket socket, String name)
    {
        client = new ClientOfServer(socket, name);
        listOfUser.add(client);
    }
    
    static void sendToAll(String text, String name) throws IOException
    {
        for(ClientOfServer client : listOfUser)
        {
            OutputStream out = client.getSocket().getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);
            dataOut.writeUTF(name + ": " + text + "\r\n");
        }
    }
    
    private void sendToAll(String text) throws IOException
    {
        for(ClientOfServer c : listOfUser)
        {
            if(c.getSocket() != client.getSocket())
            {
                OutputStream out = c.getSocket().getOutputStream();
                DataOutputStream dataOut = new DataOutputStream(out);
                dataOut.writeUTF("Server: " + text + "\r\n");
            }
        }
    }
    
    static String compareSocketAndReturnName(Socket s)
    {        
        for(ClientOfServer c : listOfUser)
        {
            if(c.getSocket().equals(s))
            {
                return c.getName();
            }
        }
        return null;
    }
}
