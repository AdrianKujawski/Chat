/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

/**
 *
 * @author Adrian
 */
public class ClientServer
{
    private String name = null;
    private Socket socket = null;
    private TextArea textArea = null;
    
    public ClientServer(TextArea area, String name, Socket socket) throws IOException 
    {
        this.name = name;
        this.socket = socket;
        textArea = area;
    }
    
    public void SendMessage(String text)
    {
        new Thread(){
            public void run()
            {
                OutputStream out = null;
                try {
                    out = socket.getOutputStream();
                    DataOutputStream dataOutput = new DataOutputStream(out);
                    dataOutput.writeUTF(text);
                } 
                catch (Exception ex) 
                {
                    Platform.runLater(new Runnable() {
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Połączenie zerwane");
                        alert.setHeaderText("Nie udało się wysłać wiadomości.");
                        alert.setContentText("Spróbuj połączyć się ponownie.");
                        alert.showAndWait();
               }
            }); 
            }
        }
        }.start();
    }
    
    public void SendName(String text) throws IOException
    {
        OutputStream out = socket.getOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(out);
        dataOutput.writeUTF(text);
    }
}
