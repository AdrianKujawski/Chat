/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

/**
 *
 * @author Adrian
 */
public class ClientReceive extends Task {

    Socket socket = null;
    TextArea area = null;

    ClientReceive(Socket socket, TextArea area) {
        this.socket = socket;
        this.area = area;
    }

    @Override
    protected Object call() throws Exception {
        while (socket.isConnected()) 
        {
            InputStream in = socket.getInputStream();
            DataInputStream dataIn = new DataInputStream(in);

            String text = dataIn.readUTF();

            Platform.runLater(new Runnable() {
                public void run() {
                    area.appendText(text);
                }
            });

        }
        
        return null;
    }
}
