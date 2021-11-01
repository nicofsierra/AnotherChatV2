package unlam.prograva.taller.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
  
public class Cliente extends Application {  
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;
	
	// Chat area
	private TextArea taChat = new TextArea();
	// Send area
	private TextArea taSend = new TextArea();
	// "Send" button
	private Button btSend = new Button("Send");
	
	@Override
	public void start(Stage primaryStage) {
		// UI
		taChat.setWrapText(true);
		taChat.setEditable(false);
		taChat.setPrefColumnCount(60);
		taChat.setPrefRowCount(25);
		
		taSend.setWrapText(true);
		taSend.setEditable(true);
		taSend.setPrefColumnCount(60);
		taSend.setPrefRowCount(8);
		
		VBox vBox = new VBox();
		vBox.getChildren().add(new ScrollPane(taChat));
		vBox.getChildren().add(new ScrollPane(taSend));
		vBox.getChildren().add(btSend);
		
		//SearchPane searchPane = new SearchPane();
		
		BorderPane pane = new BorderPane();
		pane.setCenter(vBox);
		//pane.setRight(searchPane);
		
		Scene scene = new Scene(pane, 1000, 800);
		primaryStage.setTitle("ChatWeEasy");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		// Connect to Server
		try {
			Socket socket = new Socket("localhost", 8000);
			fromServer = new DataInputStream(
					socket.getInputStream());
			toServer = new DataOutputStream(
					socket.getOutputStream());
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		
		// Trigger: send local message to server
		btSend.setOnAction(e -> {
			if(!taSend.getText().isEmpty()) {
				try {
					toServer.writeUTF(taSend.getText());
					toServer.flush();
					taSend.clear();		
				}
				catch(IOException ex) {
					ex.printStackTrace();
				}				
			}
		});			
		
		// New thread to monitor message from server
		new Thread(() -> {
			try {				
				while(true) {	
					String message = fromServer.readUTF();
					Platform.runLater(() -> {						
						taChat.appendText(message + '\n');			
					});	
					//ManageRecords.addMessageRecord(message);
				}			
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}).start();
			
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
