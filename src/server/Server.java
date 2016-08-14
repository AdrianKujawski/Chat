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

/**
 *
 * @author Adrian
 */
public class Server 
{
    public static void main(String[] args) throws IOException 
    {
        RunServer();
    }
    
    static void RunServer() throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(444);
        while(true)
        {
            System.out.println("Oczekiwanie na nowego gościa.");
            Socket socket = serverSocket.accept();
            new ServerThread(socket).start();
        }
        
    }
}
