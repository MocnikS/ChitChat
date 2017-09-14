package chitchat;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.*;

public class ChitChatApp {
	
	public static void main(String[] args){
		CallsToServer cts = new CallsToServer();
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				Window chitChat;
				
				try {
					chitChat = new Window();

					chitChat.setVisible(true);
				} catch (URISyntaxException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
}
