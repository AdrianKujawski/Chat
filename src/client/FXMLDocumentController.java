/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Adrian
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button sendButton;
    
    @FXML
    private Button connectButton;
    
    @FXML
    private Button disconnectButton;
    
    @FXML
    private TextArea textArea;
    
    @FXML
    private TextArea textField;
    
    ClientServer client = null;
    Socket socket = null;
    
    @FXML
    private TextField yourName;
    
    
    @FXML
    public void onEnterSend(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)  
        {
            sendToServer();
            
        }
    }
    
    @FXML
    private void sendToServer() throws IOException 
    {
        
        try
        {
        client.SendMessage(textField.getText());
        textField.clear();
        }
        catch(Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Połączenie zerwane");
            alert.setHeaderText("Nie udało się wysłać wiadomości.");
            alert.setContentText("Spróbuj połączyć się ponownie.");
            alert.show();
        }
    }
    
    @FXML
    private void disconnectFromServer(ActionEvent event) throws IOException 
    {
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
        textField.setDisable(true);
        sendButton.setDisable(true);
        yourName.setDisable(false);
        socket.close(); 
        textArea.appendText("You disconnected.\r\n");
    }
    
    @FXML
    private void connectToServer() throws IOException {

        try {
            socket = new Socket("localhost", 444);
            ClientReceive receive = new ClientReceive(socket, textArea);

            Thread thread = new Thread(receive);
            thread.setDaemon(true);
            thread.start();

            client = new ClientServer(textArea, yourName.getText(), socket);
            client.SendName(yourName.getText());

            connectButton.setDisable(true);
            disconnectButton.setDisable(false);
            textField.setDisable(false);
            sendButton.setDisable(false);
            yourName.setDisable(true);
            
        } catch (Exception ex) 
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Połączenie zerwane");
            alert.setHeaderText("Nie można nawiązać połączenia z serwerem.");
            alert.setContentText("Serwer może być wyłączony.");
            alert.show();
        }

    }
     
    @FXML
    public void onEnterConnect() throws IOException 
    {
        connectToServer();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        disconnectButton.setDisable(true);
        textField.setDisable(true);
        sendButton.setDisable(true);
        textArea.setEditable(false);
        textField.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) 
                {
                    try {
                        sendToServer();
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ke.consume(); // necessary to prevent event handlers for this event
                }
            }
        });
    }    
    
}
