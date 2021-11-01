package unlam.prograva.taller.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
  
public class Servidor extends Application {  
	
	private static final int NUMBER_OF_THREAD = 40;		
	private TextArea ta = new TextArea();				
	private int UserNo = 0;                             
	private int UserNum = 0;
	
	// List of current users
    private List<DataOutputStream> currentUsers 
    		= new ArrayList<>(); 	
    // Thread pool
    private ExecutorService executor 
    		= Executors.newFixedThreadPool(NUMBER_OF_THREAD);
      
    @Override 
    public void start(Stage primaryStage) {
    	// UI 
    	ta.setWrapText(true);
    	ta.setEditable(false);
    	ta.setPrefRowCount(60);
    	Scene scene = new Scene(new ScrollPane(ta), 600, 700);
		primaryStage.setTitle("ChatWeEasyServer");
		primaryStage.setScene(scene);
		primaryStage.show();
    	
		// Running the Server 
    	new Thread(() -> {
    		 try{
    			 ServerSocket serverSocket = new ServerSocket(8000);  
    			 ta.appendText("ChatWeEasySever started at "
 						+ new Date() + "\n");
    	          // Monitor 	            
    	         while(true){     
    	        	 // A user come in
    	        	 Socket socket = serverSocket.accept();  
    	        	 UserNo++;
    	        	 UserNum++;
    	        	 
    	        	 // Print user's information
    	        	 Platform.runLater(() -> {
    	        		 ta.appendText("User " + UserNo + " enters at " 
    	        				 + new Date() + '\n' );
    	        		 ta.appendText("Now total users number: "
    	        				 + UserNum + "\n\n");
    	        	 });
    	      	     
    	        	 // Create a thread for each user coming in
    	             executor.execute(new UserHandler(socket));  
    	            }  
    	        }
    		 catch(Exception e){  
    	            e.printStackTrace();  
    	     }  
    	}).start();
    	
    }   
    
    // Run for users respectively
    private class UserHandler implements Runnable {  
        private Socket socket;  
          
        public UserHandler(Socket socket){  
            this.socket = socket;  
        }  
          
        @Override  
        public void run() {  
        	DataOutputStream temp = null;
            try {  
                DataOutputStream toUser = 
                		new DataOutputStream(socket.getOutputStream());
                DataInputStream fromUser = 
                		new DataInputStream(socket.getInputStream());
               
                temp = toUser;
                // Add new user to current users list
                addNewUser(toUser);
                 
                // Monitor
                while(true) {
                	// Sever send message to every user in current users list
                	String message = fromUser.readUTF();
                	sendMessage(message); 
                }          
            }
            catch (Exception ex) {  
                ex.printStackTrace();  
            }
            finally{  
            	// Remove the user quit
            	removeOldUser(temp);
            	UserNum--;
                if(socket != null){  
                    try{  
                        socket.close();  
                    }
                    catch(Exception ex){  
                        ex.printStackTrace();  
                    }  
                } 
            }       
        }    
        
    }  
     
    // Manage current users
    // Add new user to current users list
    private synchronized void addNewUser(DataOutputStream newUser){
    	currentUsers.add(newUser);
    }
    // Remove user quit
    private synchronized void removeOldUser(DataOutputStream oldUser){
    	currentUsers.remove(oldUser);
    }
    // Send message to every user in current users list
    private synchronized void sendMessage(String message){
    	for(DataOutputStream users:currentUsers){
    		try {
    			users.writeUTF(message);
    		}
    		catch(IOException ex) {
    			ex.printStackTrace();
    		}    		
    	}
    }
      
    public static void main(String[] args) {  
        Application.launch(args);
    }  
  
}
