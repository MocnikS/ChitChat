package chitchat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.http.client.ClientProtocolException;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.JList;

public class Window extends JFrame {
	
	private CallsToServer cts;
	private boolean loggedIn = false;
	private String sendTo = "All";
	
	private JTextField username;
	private JTextField message;
	private JTextArea messages;
	private JList allUsernames; 
	
	public Window() throws ClientProtocolException, URISyntaxException, IOException {
		super();
		
		cts = new CallsToServer();
		initUI();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (loggedIn) {
					try {
						cts.loggout(cts.username);
					} catch (URISyntaxException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	private void initUI() throws ClientProtocolException, URISyntaxException, IOException {   
        setTitle("Chit Chat");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{15, 86, 46, 101, 89, 74, 75, 0, 0};
		gridBagLayout.rowHeights = new int[]{12, 23, 19, 0, 29, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel usernamaeLabel = new JLabel("Username:");
		GridBagConstraints gbc_usernamaeLabel = new GridBagConstraints();
		gbc_usernamaeLabel.anchor = GridBagConstraints.EAST;
		gbc_usernamaeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_usernamaeLabel.gridx = 1;
		gbc_usernamaeLabel.gridy = 1;
		getContentPane().add(usernamaeLabel, gbc_usernamaeLabel);
		
		username = new JTextField();
		username.setText(cts.username);
		GridBagConstraints gbc_username = new GridBagConstraints();
		gbc_username.fill = GridBagConstraints.HORIZONTAL;
		gbc_username.gridwidth = 2;
		gbc_username.insets = new Insets(0, 0, 5, 5);
		gbc_username.gridx = 2;
		gbc_username.gridy = 1;
		getContentPane().add(username, gbc_username);
		username.setColumns(10);
		
		JButton logginButton = logginButton();
		GridBagConstraints gbc_logginButton = new GridBagConstraints();
		gbc_logginButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_logginButton.insets = new Insets(0, 0, 5, 5);
		gbc_logginButton.gridx = 4;
		gbc_logginButton.gridy = 1;
		getContentPane().add(logginButton, gbc_logginButton);
		
		JButton loggoutButton = loggoutButton();
		GridBagConstraints gbc_loggoutButton = new GridBagConstraints();
		gbc_loggoutButton.insets = new Insets(0, 0, 5, 5);
		gbc_loggoutButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_loggoutButton.gridx = 5;
		gbc_loggoutButton.gridy = 1;
		getContentPane().add(loggoutButton, gbc_loggoutButton);
		
		JLabel activeUsersLabel = new JLabel("Active users:");
		GridBagConstraints gbc_activeUsersLabel = new GridBagConstraints();
		gbc_activeUsersLabel.gridwidth = 2;
		gbc_activeUsersLabel.insets = new Insets(0, 0, 5, 5);
		gbc_activeUsersLabel.gridx = 5;
		gbc_activeUsersLabel.gridy = 2;
		getContentPane().add(activeUsersLabel, gbc_activeUsersLabel);
		
		messages = new JTextArea();
		messages.setDisabledTextColor(Color.BLACK);
		messages.setEnabled(false);
		messages.setColumns(60);
		messages.setRows(20);
		messages.setWrapStyleWord(true);
		messages.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(messages);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 3;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		allUsernames = addAllUsers();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridwidth = 2;
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 5;
		gbc_list.gridy = 3;
		getContentPane().add(allUsernames, gbc_list);
		
		JLabel messageLabel = new JLabel("New label");
		GridBagConstraints gbc_messageLabel = new GridBagConstraints();
		gbc_messageLabel.insets = new Insets(0, 0, 5, 5);
		gbc_messageLabel.anchor = GridBagConstraints.EAST;
		gbc_messageLabel.gridx = 1;
		gbc_messageLabel.gridy = 4;
		getContentPane().add(messageLabel, gbc_messageLabel);
		
		message = new JTextField();
		GridBagConstraints gbc_message = new GridBagConstraints();
		gbc_message.gridwidth = 3;
		gbc_message.insets = new Insets(0, 0, 5, 5);
		gbc_message.fill = GridBagConstraints.HORIZONTAL;
		gbc_message.gridx = 2;
		gbc_message.gridy = 4;
		getContentPane().add(message, gbc_message);
		message.setColumns(10);
		message.setEditable(false);
		
		JButton sendButton = sendButton();
		GridBagConstraints gbc_sendButton = new GridBagConstraints();
		gbc_sendButton.insets = new Insets(0, 0, 5, 5);
		gbc_sendButton.gridx = 5;
		gbc_sendButton.gridy = 4;
		getContentPane().add(sendButton, gbc_sendButton);
		
		ActionListener refreshMessages = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                	if(loggedIn){
                		String msg = cts.getMessages(cts.username);
                		if(msg != ""){
                			messages.append(msg);
                		}
                	}
				} catch (URISyntaxException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
		};
		
		
		
		Timer timer = new Timer(5000 , refreshMessages);
		timer.setRepeats(true);
		timer.start();
		
		ActionListener refreshUsers = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ListModel model = allUsernames.getModel();
				List<String> usernames;
				try {
					usernames = cts.getUsernames();
					// Zbrisi vse uporabnike ki so se odjavili
					int i = 0;
					while(i < model.getSize()) {
						String username = (String)model.getElementAt(i);
						for(String user : usernames) {
							if(username.equals(user)) {
								break;
							}
						}
						if(!username.equals("All")) {
							((DefaultListModel)allUsernames.getModel()).remove(i);
							i = i - 1;
						}
						i = i + 1;
					}
					
					
					for(String user : usernames) {
						i = 0;
						boolean isNew = true;
						 while(i < model.getSize()){
							String username = (String)model.getElementAt(i);
							if(username.equals(user)) {
								isNew = false;
								break;
							}
							i = i + 1;
						}
						if(!cts.username.equals(user) && isNew) {
							((DefaultListModel<String>)allUsernames.getModel()).addElement(user);
						}
						
					}
					
				} catch (URISyntaxException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		
		Timer refreshUsersTask = new Timer(8000,refreshUsers);
		refreshUsersTask.setRepeats(true);
		refreshUsersTask.start();
	}
	
	private JButton logginButton(){
		JButton logginButton = new JButton("Loggin");
		logginButton.setBounds(312, 7, 86, 23);
		logginButton.addActionListener((ActionEvent event) -> {
        	if(username.getText() != ""){
	            try {
					String response = cts.loggin(username.getText());
					cts.username = username.getText();
					username.setEditable(false);
					message.setEditable(true);
					loggedIn = true;
				} catch (URISyntaxException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });
		
		return logginButton;
	}
	
	private JButton loggoutButton(){
		JButton loggoutButton = new JButton("Loggout");
		loggoutButton.setBounds(407, 7, 86, 23);
		
		loggoutButton.addActionListener((ActionEvent event)->{
        	if(loggedIn){
        		try {
					cts.loggout(cts.username);
					loggedIn = false;
					username.setEditable(true);
					message.setEditable(false);
				} catch (URISyntaxException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });
		
		return loggoutButton;
	}

	private JButton sendButton(){
		JButton sendButton = new JButton("Send");
		sendButton.setBounds(481, 376, 65, 23);
		sendButton.addActionListener((ActionEvent event) -> {
        	if(message.getText() != "" && loggedIn){
	            try {
	            	if(sendTo.equals("All")) {
	            		cts.sendPublicMessage(cts.username, message.getText());
	            		messages.append("My global message: " + message.getText() + "\n");
	            	}
	            	else {
	            		cts.sendPrivateMessage(cts.username, sendTo, message.getText());
	            		messages.append("My private message to " + sendTo + ": " + message.getText() + "\n");
	            	}
				} catch (URISyntaxException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        message.setText(null);
        	}
        });
		
		return sendButton;
	}
	
	private JList addAllUsers() throws ClientProtocolException, URISyntaxException, IOException {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement("All");
		for(String i : cts.getUsernames()) {
			listModel.addElement(i);
		}
		
		JList list = new JList<>(listModel);
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(!arg0.getValueIsAdjusting() && list.getSelectedValue() != null) {
					sendTo = (String)list.getSelectedValue();
				}
			}
		});
		return list;
	}
}
