package es.uniovi;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import javax.swing.*;

import es.uniovi.popups.About;
import es.uniovi.popups.ChangeNick;
import es.uniovi.popups.Commands;
import es.uniovi.popups.NewRoom;

import java.awt.*;
import java.awt.event.*;

/**
 *  Main Chat Client launcher
 */
public class ChatClient {
	
	public static JFrame frame;
	public static JTextField msg;
	private static JTabbedPane tabs;
	public static JTextArea users;
	private static Map<String, Room> rooms;
	private static ArrayList<String> roomList;
	private static JMenu roomMenu;
	public static String room;
	private static JMenuItem leaveRoom;
	
	static String nick = "";
	static String host = "127.0.0.1";
	static int port = 69;

	static boolean quit = false;

	static NetworkOut netOut;
	static NetworkIn netIn;
	
	static Socket s;

	private static InThread in;
	
	/**
	 * Main class for the client, it launches the UI and starts the network threads.
	 * @param args Unused
	 */
	public static void main(String[] args){
		
		EventQueue.invokeLater(() -> {
            try {
                new ChatClient();
                ChatClient.frame.setVisible(true);
                ChatClient.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                // Start the rooms
                rooms = new HashMap<>();
                roomList = new ArrayList<>();

                // Wait for server configuration
                config();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}
	
	/**
	 * Sends a /QUIT command to the server. It will wait for an answer before closing
	 * the application
	 */
	public static void close(){
		try{
    		ChatClient.netOut.send(new Command("/QUIT"));
    	}catch(Exception ex){
    		System.out.println("Unable to send /QUIT");
    	}
	}
	
	/**
	 * Close the application
	 */
	public static void finish(){
		ChatClient.quit = true;
    	netIn.finish();
    	netOut.finish();
    	ChatClient.frame.dispose();
    	System.exit(0);
	}
	
	/**
	 * Initialize the GUI
	 */
	private ChatClient(){
		initialize();
	}
	
	/**
	 * Send a message to a given room
	 * @param text Message
	 */
	public static void println(String room, String text){
		final String s = room, t = text;
		EventQueue.invokeLater(() -> {
            Room currentRoom = rooms.get(s);
            if(currentRoom != null){
                rooms.get(s).append(t);
            }
        });
	}
	
	/**
	 * Write a message on the specified room, changing the tab color
	 * if it isn't active to provide visual feedback.
	 * @param text Message
	 */
	public static void printMsg(String room, String text){
		final String s = room, t = text;
		EventQueue.invokeLater(() -> {
            Room room1 = rooms.get(s);
            if(room1 != null){
                rooms.get(s).append(t);
                // Change the room color
                if(!s.equals(ChatClient.room)){
                    tabs.setBackgroundAt(room1.num, Color.ORANGE);
                }
            }
        });
	}
	
	/**
	 * Broadcast a message to all rooms
	 * @param text Message
	 */
	public static void println(String text){
		final String t = text;
		EventQueue.invokeLater(() -> {
            for (Map.Entry<String, Room> entry : rooms.entrySet()) {
                entry.getValue().append(t);
            }
        });
	}
	
	/**
	 * Sends the /JOIN command to join a room
	 * @param name Room name
	 */
	public static void sendJoin(String name){
		try{
			ChatClient.netOut.send(new Command("/JOIN "+name));
		}catch(Exception ignored){}
	}
	
	/**
	 * Change to the specified tab, creating the room if it didn't exist.
	 * @param n Room name
	 */
	public static void joinRoom(String n){
		final String name = n;
		EventQueue.invokeLater(() -> {
            Room room1 = rooms.get(name);
            if(room1 != null){
                // Select the new room
                room = name;
                tabs.setSelectedIndex(room1.num);
                room1.displayUsers();
            }else{
                addRoom(name, tabs.getTabCount() - 1);
                // Select the last room
                tabs.setSelectedIndex(tabs.getTabCount()-2);
                room = name;
                // If the number of tabs is less than 2 it means its either the about tab
                // or something weird..
                if(tabs.getTabCount()>2){
                    addRoom(name);
                    try{
                        // Get a list of users in the room to populate the right panel
                        ChatClient.netOut.send(new Command("/WHO "+name));
                    }catch(Exception ignored){ }
                }
            }
        });
	}
	
	/**
	 * Send a /LEAVE command
	 * @param name Room name
	 */
	private static void sendLeave(String name){
		try{
			ChatClient.netOut.send(new Command("/LEAVE "+name));
		}catch(Exception ignored){}
	}
	
	/**
	 * Leave a specified room, adjusting the UI accordingly
	 * @param n Room name
	 */
	public static void leaveRoom(String n){
		final String name = n;
		EventQueue.invokeLater(() -> {
            Room room1 = rooms.get(name);
            if(room1 != null){
                tabs.setSelectedIndex(0);
                tabs.remove(room1.num);
                room = "About";
                rooms.remove(name);

                int index = room1.num;
                HashMap<String, Room> aux = new HashMap<>();
                Room s1;
                // Reduce the tab index of all tabs after the removed one
                for (Map.Entry<String, Room> entry : rooms.entrySet()) {
                    s1 = entry.getValue();
                    if(s1.num > index){
                        s1.num--;
                        aux.put(entry.getKey(), s1);
                    }
                }
                rooms = aux;
            }
        });
	}
	
	/**
	 * Generate the user online list for a given room
	 * @param room Room name
	 * @param list User list
	 * 
	 */
	public static void addUsers(String room, String[] list){
		Room s = rooms.get(room);
		if(s != null){
			s.addUsers(list);
		}
	}
	
	/**
	 * Add a user to the online list for a room
	 * @param room Room name
	 * @param user User name
	 */
	public static void addUser(String room, String user){
		Room s = rooms.get(room);
		if(s != null){
			s.addUser(user);
		}
	}
	
	/**
	 * Remove a user from the specified room
	 * @param room Room name
	 * @param user User name
	 */
	public static void removeUser(String room, String user){
		Room s = rooms.get(room);
		if(s != null){
			s.removeUser(user);
		}
	}
	
	/**
	 * Remove a user from all tabs
	 * @param user User name
	 */
	public static void removeUserAll(String user){
		for (Map.Entry<String, Room> entry : rooms.entrySet()) {
			entry.getValue().removeUser(user);
		}
	}
	
	/**
	 * Change a username in every room, after receiving a NICK update
	 * @param old Old username
	 * @param user New username
	 */
	public static void replaceUser(String old, String user){
		for (Map.Entry<String, Room> entry : rooms.entrySet()) {
			entry.getValue().replaceUser(old,user);
		}
	}
	
	/**
	 * Add a room without creating the tab for it
	 * @see #joinRoom(String)
	 * @param name Room name
	 * @param num Tab index
	 */
	private static void addRoom(String name, int num){
		rooms.put(name, new Room(name, num));
		room = name;
	}
	
	/**
	 * Add all the rooms to the panel
	 * @param list Room list
	 */
	public static void listRooms(String[] list){
		for(String each : list){
			if(!roomList.contains(each)) roomList.add(each);
		}
		Collections.sort(roomList);
		roomMenu();
	}
	
	/**
	 * Add a room to the right panel
	 * @param room Room name
	 */
	private static void addRoom(String room){
		if(room.trim().length() > 0 && !roomList.contains(room)){
			roomList.add(room);
			Collections.sort(roomList);
			roomMenu();
		}
	}
	
	/**
	 * Remove a room from the right panel
	 */
	@SuppressWarnings("unused")
	private static void removeRoom(String room){
		if(roomList.remove(room)){
			roomMenu();
		}
	}
	
	/**
	 * Display the room list on the right panel
	 */
	private static void roomMenu(){
		EventQueue.invokeLater(() -> {
            roomMenu.removeAll();
            for(final String each : roomList){
                JMenuItem room1 = new JMenuItem(each);
                // Click action
                room1.addActionListener(arg0 -> {
                    Room s1 = rooms.get(each);
                    if (s1 != null) {
                        joinRoom(each);
                    } else {
                        sendJoin(each);
                    }
                });
                roomMenu.add(room1);
            }
        });
	}
	
	/**
	 * Sends a /NICK change request
	 * @param nick New Nick
	 */
	public static void newNick(String nick){
		try{
			ChatClient.netOut.send(new Command("/NICK "+nick));
		}catch(Exception ignored){}
	}
	
	/**
	 * Adds a tab, this one should be called instead of joinRoom
	 * @see #joinRoom(String)
	 * @param name Room name
	 * @return ChatArea for the new tab
	 */
	public static ChatArea addTab(String name){
		JPanel tab = new JPanel();
		tab.setBackground(Color.WHITE);
		tab.setBorder(null);
		
		if(tabs.getTabCount()>0) tabs.remove(tabs.getTabCount()-1);
		
		tabs.add(name, tab);
		tab.setLayout(new BorderLayout(0, 0));
		
		tabs.addTab("", null, new JButton("Button"),"New tab");
		
		ChatArea chat = new ChatArea();
		chat.setContentType("text/html");
		chat.setBackground(Color.WHITE);
		tab.add(chat, BorderLayout.CENTER);
		
		JScrollPane scrollBar = new JScrollPane(chat);
		scrollBar.setBackground(Color.WHITE);
		scrollBar.setEnabled(false);
		scrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tab.add(scrollBar);
		
		return chat;
	}
	
	/**
	 * Initiates a connection with the server and joins the general room
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	static void connect() throws IOException{
		// Try connecting
		s = new Socket(host, port);
		
		// Join the main room
		joinRoom("About");
		
		// Welcome message
		ChatClient.println("About", "<pre style=\"background-color:#efefef; color:#444444; width:352px;\">" +
				"|------------------  <span style=\"color:red\">ChatClient v3</span>  -------------------|\n" +
				"| In the top menu you can access help with the client, |\n" +
				"| and other options and configuration.                 |\n" +
				"|                                                      |\n" +
				"| This tab only display general chat messages from     |\n" +
				"| the server, although it allows you to send commands  |\n" +
				"| to the server.                                       |\n" +
				"|                                                      |\n" +
				"| To leave a room you can go to Rooms > Abandon room,  |\n" +
				"| or type Ctrl+W while on the room.                    |\n" +
				"|------------------------------------------------------|\n" +
				"</pre>");
		
		// Start the threads

		netOut = new NetworkOut();
		netIn = new NetworkIn();

		in = new InThread();
	}
	
	/**
	 * Launch the config screen
	 * (Host, port, nick)
	 */
	private static void config(){
		new Config();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("ChatClient v3");
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setFont(new Font("Georgia", Font.PLAIN, 25));
		frame.setBounds(100, 100, 720, 574);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setMinimumSize(new Dimension(400, 300));
		// Try to assign an icon to the app
		try{
			URL url = new URL("file:img/icons/icon16.png");
			Toolkit kit = Toolkit.getDefaultToolkit();
			frame.setIconImage(kit.createImage(url));
		}catch(Exception e){ e.printStackTrace();}
		
		JPanel footer = new JPanel();
		footer.setBackground(Color.WHITE);
		frame.getContentPane().add(footer, BorderLayout.SOUTH);
		footer.setLayout(new BorderLayout(0, 0));
		
		JPanel footerLeft = new JPanel();
		footerLeft.setBackground(Color.WHITE);
		footer.add(footerLeft, BorderLayout.CENTER);
		footerLeft.setLayout(new BorderLayout(0, 0));
		
		JPanel writeArea = new JPanel();
		footerLeft.add(writeArea, BorderLayout.CENTER);
		writeArea.setLayout(new BorderLayout(0, 0));
		
		JPanel toolbarBox = new JPanel();
		toolbarBox.setBackground(SystemColor.text);
		writeArea.add(toolbarBox, BorderLayout.NORTH);
		toolbarBox.setLayout(new BoxLayout(toolbarBox, BoxLayout.X_AXIS));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(SystemColor.text);
		toolbarBox.add(panel_3);
		
		JToolBar toolBar = new JToolBar();
		toolbarBox.add(toolBar);
		toolBar.setFloatable(false);
		toolBar.setBackground(SystemColor.text);
		
		// Emoji icons
		try{
			
			String[] iconNames = {"smile","sad","wink","wow","surprise","meh","what","love","hmm"};
			IconButton[] icons = new IconButton[iconNames.length];
			
			for(int i=0; i<icons.length; i++){
				icons[i] = new IconButton(iconNames[i]);
				toolBar.add(icons[i]);
			}
		
		}catch(Exception e){
			// Don't show the icon bar if for some reason
			// they fail to load
			e.printStackTrace();
		}
		
		JButton smileBtn = new JButton("");
		smileBtn.addActionListener(e -> msg.setText(msg.getText()+":smile:"));
		smileBtn.setBackground(SystemColor.text);
		smileBtn.setIcon(new ImageIcon("img/icons/smile.png"));
		toolBar.add(smileBtn);
		
		JPanel msgBox = new JPanel();
		writeArea.add(msgBox, BorderLayout.CENTER);
		msgBox.setLayout(new BoxLayout(msgBox, BoxLayout.X_AXIS));
		
		JPanel msgMargin = new JPanel();
		msgMargin.setBackground(SystemColor.text);
		msgMargin.setPreferredSize(new Dimension(30, 40));
		msgBox.add(msgMargin);
		msgMargin.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		msg = new JTextField();
		msgBox.add(msg);
		msg.addActionListener(arg0 -> {
            // Enter key
            try{
                in.messageQueue.put(msg.getText());
                msg.setText("");
            }catch(Exception e){
                System.out.println("The outbound queue is full!");
            }
        });
		msg.setBackground(SystemColor.window);
		msg.setColumns(30);
		
		JPanel footerRight = new JPanel();
		footerRight.setBackground(Color.WHITE);
		footer.add(footerRight, BorderLayout.EAST);
		footerRight.setLayout(new BorderLayout(0, 0));
		
		JPanel btnTop = new JPanel();
		btnTop.setPreferredSize(new Dimension(160, 30));
		btnTop.setBackground(SystemColor.text);
		footerRight.add(btnTop, BorderLayout.NORTH);
		
		JPanel btnBottom = new JPanel();
		btnBottom.setBackground(SystemColor.text);
		footerRight.add(btnBottom);
		btnBottom.setLayout(new BoxLayout(btnBottom, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		btnBottom.add(panel_1);
		panel_1.setPreferredSize(new Dimension(30, 10));
		panel_1.setBackground(Color.WHITE);
		
		JButton send = new JButton("Send");
		btnBottom.add(send);
		send.addActionListener(arg0 -> {
            try{
                in.messageQueue.put(msg.getText());
                msg.setText("");
            }catch(Exception e){
                System.out.println("Inbound queue full!");
            }
        });
		send.setForeground(SystemColor.textHighlight);
		send.setFont(new Font("Georgia", Font.PLAIN, 18));
		send.setBackground(SystemColor.text);
		
		JPanel panel = new JPanel();
		btnBottom.add(panel);
		panel.setPreferredSize(new Dimension(30, 10));
		panel.setBackground(Color.WHITE);
		
		JPanel footerSeparator = new JPanel();
		footerSeparator.setPreferredSize(new Dimension(10, 30));
		footerSeparator.setBackground(Color.WHITE);
		footer.add(footerSeparator, BorderLayout.NORTH);
		footerSeparator.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel bottomMargin = new JPanel();
		bottomMargin.setPreferredSize(new Dimension(10, 30));
		bottomMargin.setBackground(SystemColor.text);
		footer.add(bottomMargin, BorderLayout.SOUTH);
		
		JPanel header = new JPanel();
		header.setBackground(Color.WHITE);
		frame.getContentPane().add(header, BorderLayout.NORTH);
		header.setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		header.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnNewMenu = new JMenu("ChatClient");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Close");
		mntmNewMenuItem.addActionListener(arg0 -> ChatClient.close());
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Change nick");
		mntmNewMenuItem_5.setMnemonic('K');
		mntmNewMenuItem_5.addActionListener(e -> new ChangeNick());
		mnNewMenu.add(mntmNewMenuItem_5);
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenu tools = new JMenu("Rooms");
		menuBar.add(tools);
		
		roomMenu = new JMenu("Enter");
		tools.add(roomMenu);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("New");
		mntmNewMenuItem_1.setMnemonic('N');
		mntmNewMenuItem_1.addActionListener(e -> {
            // End tab, launch window
            new NewRoom();
        });
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Refresh list");
		mntmNewMenuItem_4.setMnemonic('L');
		mntmNewMenuItem_4.addActionListener(e -> {
            try{
                ChatClient.netOut.send(new Command("/LIST"));
            }catch(Exception ignored){}
        });
		tools.add(mntmNewMenuItem_4);
		tools.add(mntmNewMenuItem_1);
		
		leaveRoom = new JMenuItem("Leave room (W)");
		leaveRoom.addActionListener(e -> ChatClient.sendLeave(ChatClient.room));
		leaveRoom.setEnabled(false);
		tools.add(leaveRoom);
		
		JMenu mnNewMenu_2 = new JMenu("Help");
		menuBar.add(mnNewMenu_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Commands");
		mntmNewMenuItem_3.setMnemonic('C');
		mntmNewMenuItem_3.addActionListener(e -> new Commands());
		mnNewMenu_2.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("About...");
		mntmNewMenuItem_2.setMnemonic('A');
		mntmNewMenuItem_2.addActionListener(e -> new About());
		mnNewMenu_2.add(mntmNewMenuItem_2);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		header.add(panel_2, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("ChatClient v3");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 22));
		header.add(lblNewLabel, BorderLayout.SOUTH);
		
		JPanel content = new JPanel();
		content.setBackground(Color.WHITE);
		frame.getContentPane().add(content, BorderLayout.CENTER);
		content.setLayout(new BorderLayout(0, 0));
		
		JPanel contentLeft = new JPanel();
		contentLeft.setBackground(Color.WHITE);
		content.add(contentLeft, BorderLayout.CENTER);
		contentLeft.setLayout(new BorderLayout(0, 0));
		
		tabs = new JTabbedPane();
		tabs.setForeground(Color.BLACK);
		tabs.setBackground(Color.WHITE);
		tabs.setBorder(null);
		//tabs.setUI(new TabDesign());
		contentLeft.add(tabs);
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		// Tab changing
		tabs.addChangeListener(e -> {
            if(tabs.getSelectedIndex()>0){
                if(tabs.getSelectedIndex() == tabs.getTabCount()-1){
                    // End tab, launch window
                    tabs.setSelectedIndex(0);
                    new NewRoom();
                    return;
                }
                room = tabs.getTitleAt(tabs.getSelectedIndex());
                rooms.get(room).displayUsers();
                leaveRoom.setEnabled(true);
                tabs.setBackgroundAt(tabs.getSelectedIndex(), Color.WHITE);
            }else{
                users.setText("-Enter a room-");
                leaveRoom.setEnabled(false);
            }
        });
		
		// Leave rooms
		AbstractAction leaveRoomAction = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(tabs.getSelectedIndex()>0 && tabs.getSelectedIndex() < tabs.getTabCount()-1){
					ChatClient.sendLeave(ChatClient.room);
				}
			}
		};
		
		// Detect Ctrl+W
		KeyStroke ctrlW = KeyStroke.getKeyStroke("control W");
		
		InputMap inputMap = content.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(ctrlW, "closeTab");
		 
	    // Now add a single binding for the action name to the anonymous action
	    content.getActionMap().put("closeTab", leaveRoomAction);
	    
	    JPanel chatSeparator = new JPanel();
		chatSeparator.setPreferredSize(new Dimension(30, 10));
		chatSeparator.setBackground(SystemColor.text);
		contentLeft.add(chatSeparator, BorderLayout.WEST);
		
		JPanel tabsPanel = new JPanel();
		contentLeft.add(tabsPanel, BorderLayout.NORTH);
		tabsPanel.setBackground(Color.WHITE);
		tabsPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel contentRight = new JPanel();
		contentRight.setBackground(Color.WHITE);
		content.add(contentRight, BorderLayout.EAST);
		contentRight.setLayout(new BorderLayout(0, 0));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(new BorderLayout(0, 0));
		contentRight.add(titlePanel, BorderLayout.NORTH);
		
		JPanel usersTopSeparator = new JPanel();
		usersTopSeparator.setBackground(Color.WHITE);
		titlePanel.add(usersTopSeparator, BorderLayout.NORTH);
		
		JLabel userTitleLabel = new JLabel("Online");
		userTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userTitleLabel.setBackground(Color.WHITE);
		titlePanel.add(userTitleLabel, BorderLayout.CENTER);
		
		JPanel usersPanel = new JPanel();
		contentRight.add(usersPanel);
		usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.X_AXIS));
		
		JPanel usersSeparator = new JPanel();
		usersSeparator.setPreferredSize(new Dimension(30, 10));
		usersPanel.add(usersSeparator);
		usersSeparator.setBackground(Color.WHITE);
		
		users = new JTextArea();
		users.setEditable(false);
		users.setLineWrap(true);
		users.setBackground(Color.WHITE);
		users.setColumns(12);
		usersPanel.add(users);
		users.setText("- Enter a room -");
		
		JScrollPane scrollPane = new JScrollPane(users);
		usersPanel.add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel usersRightSeparator = new JPanel();
		usersRightSeparator.setPreferredSize(new Dimension(30, 10));
		usersPanel.add(usersRightSeparator);
		usersRightSeparator.setBackground(SystemColor.text);
		
		// Override the closing event
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e){
		    	ChatClient.close();
		    }
		});
	}
}
