package es.uniovi;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

/**
 *  Cliente de consola de chat, para el hito 1
 *  Lanza los hilos necesarios para el funcionamiento.
 */
public class ClienteChat {
	
	public static JFrame frame;
	private JTextField msg;
	private JTabbedPane tabs;
	static ChatArea chat;
	
	/**
	 * Variable para el nombre de usuario
	 */
	static String nick = "";
	/**
	 * Nombre de la sala actual
	 */
	static String sala = "";
	/**
	 * IP del servidor
	 */
	static String host = "127.0.0.1";
	static int port = 69;
	/**
	 * Finaliza la sesion
	 */
	static boolean quit = false;
	/*
	 * Lanzamos algunos hilos como estáticos para poder acceder
	 * a ellos desde los demás.
	 */
	static SalidaRed netOut;
	static EntradaRed netIn;
	
	static Socket s;
	/*
	 * Los otros hilos no tienen por que ser estaticos
	 * Ya que solo se acceden desde aqui dentro
	 */
	static HiloSalida salida;
	static HiloEntrada entrada;
	
	static String memory="";
	
	/**
	 * Método principal del Cliente, muestra por pantalla algo de información
	 * y lanza los hilos de entrada por teclado y salida por pantalla.
	 * @param args Es necesario especificar IP y puerto del servidor.
	 */
	public static void main(String[] args){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteChat window = new ClienteChat();
					window.frame.setVisible(true);
					window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					// Bienvenida
					ClienteChat.println(">> Conectando al servidor...");
					
					new Config();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ClienteChat(){
		initialize();
	}
	
	/**
	 * Finaliza la ejecución
	 */
	public static void close(){
		try{
    		ClienteChat.netOut.send(new Comando("/QUIT"));
    	}catch(Exception ex){
    		System.out.println("No se pudo enviar el /QUIT");
    	}
	}
	
	/**
	 * Cierra la aplication
	 */
	public static void finish(){
		ClienteChat.quit = true;
    	netIn.termina();
    	netOut.termina();
    	ClienteChat.frame.dispose();
    	System.exit(0);
	}


	/**
	 * Muestra un mensaje por la pantalla
	 * @param t
	 */
	public static void println(String t){
		final String text = t;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				chat.append(text);
			}
		});
	}
	
	/**
	 * Inicia la conexion al servidor.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	static void connect() throws UnknownHostException, IOException{
		// Intenta conectarse
		s = new Socket(host, port);
		
		// Lanzamos los hilos
		netOut = new SalidaRed();
		netIn = new EntradaRed();
		entrada = new HiloEntrada();
		salida = new HiloSalida();
	}

	
	private ChatArea addTab(String name){
		JPanel tab = new JPanel();
		
				tabs.add(name, tab);
				tab.setLayout(new BorderLayout(0, 0));
		
		ChatArea chat = new ChatArea();
		//txtpnchevi.setEditable(false);
		chat.setContentType("text/html");
		chat.setBackground(SystemColor.control);
		tab.add(chat, BorderLayout.CENTER);
		
		JScrollPane scrollBar = new JScrollPane(chat);
		scrollBar.setEnabled(false);
		scrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tab.add(scrollBar);
		
		return chat;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("ClienteChat v3");
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setFont(new Font("Georgia", Font.PLAIN, 25));
		frame.setBounds(100, 100, 526, 374);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setMinimumSize(new Dimension(340, 200));
		
		JPanel footer = new JPanel();
		footer.setBackground(Color.WHITE);
		frame.getContentPane().add(footer, BorderLayout.SOUTH);
		footer.setLayout(new BorderLayout(0, 0));
		
		JPanel footerLeft = new JPanel();
		footerLeft.setBackground(Color.WHITE);
		footer.add(footerLeft, BorderLayout.CENTER);
		footerLeft.setLayout(new BorderLayout(0, 0));
		
		msg = new JTextField();
		msg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Evento para pulsación "Enter"
				try{
					entrada.messageQueue.put(msg.getText());
					msg.setText("");
				}catch(Exception e){
					// Cola llena
					System.out.println("Cola de entrada llena");
				}
			}
		});
		msg.setBackground(SystemColor.window);
		footerLeft.add(msg);
		msg.setColumns(30);
		
		JPanel footerLeftMargin = new JPanel();
		footerLeftMargin.setBackground(SystemColor.text);
		footerLeft.add(footerLeftMargin, BorderLayout.WEST);
		
		JPanel footerRight = new JPanel();
		footerRight.setBackground(Color.WHITE);
		footer.add(footerRight, BorderLayout.EAST);
		footerRight.setLayout(new BoxLayout(footerRight, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		footerRight.add(panel_1);

		JButton send = new JButton("Enviar");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					entrada.messageQueue.put(msg.getText());
					msg.setText("");
				}catch(Exception e){
					// Cola llena
					System.out.println("Cola de entrada llena");
				}
			}
		});
		send.setFont(new Font("Georgia", Font.PLAIN, 20));
		send.setBackground(SystemColor.text);
		footerRight.add(send);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		footerRight.add(panel);
		
		JPanel footerSeparator = new JPanel();
		footerSeparator.setBackground(Color.WHITE);
		footer.add(footerSeparator, BorderLayout.NORTH);
		footerSeparator.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel bottomMargin = new JPanel();
		bottomMargin.setBackground(SystemColor.text);
		footer.add(bottomMargin, BorderLayout.SOUTH);
		
		JPanel header = new JPanel();
		header.setBackground(Color.WHITE);
		frame.getContentPane().add(header, BorderLayout.NORTH);
		header.setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		header.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnNewMenu = new JMenu("ClienteChat");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Cerrar");
		mnNewMenu.add(mntmNewMenuItem);
		
		JLabel lblNewLabel = new JLabel("ClienteChat v3");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 22));
		header.add(lblNewLabel, BorderLayout.CENTER);
		
		JPanel content = new JPanel();
		content.setBackground(Color.WHITE);
		frame.getContentPane().add(content, BorderLayout.CENTER);
		content.setLayout(new BorderLayout(0, 0));
		
		JPanel contentLeft = new JPanel();
		contentLeft.setBackground(Color.WHITE);
		content.add(contentLeft, BorderLayout.CENTER);
		contentLeft.setLayout(new BorderLayout(0, 0));
		
		tabs = new JTabbedPane();
		contentLeft.add(tabs);
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		chat = addTab("Principal");
		
		JPanel chatSeparator = new JPanel();
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
		
		JLabel userTitleLabel = new JLabel("Online:");
		userTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userTitleLabel.setBackground(Color.WHITE);
		titlePanel.add(userTitleLabel, BorderLayout.CENTER);
		
		JPanel usersPanel = new JPanel();
		contentRight.add(usersPanel);
		usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.X_AXIS));
		
		JPanel usersSeparator = new JPanel();
		usersPanel.add(usersSeparator);
		usersSeparator.setBackground(Color.WHITE);
		
		JTextArea users = new JTextArea();
		users.setEditable(false);
		users.setLineWrap(true);
		users.setBackground(SystemColor.control);
		users.setColumns(8);
		users.setRows(10);
		usersPanel.add(users);
		
		JScrollPane scrollPane = new JScrollPane(users);
		usersPanel.add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel usersRightSeparator = new JPanel();
		usersPanel.add(usersRightSeparator);
		usersRightSeparator.setBackground(SystemColor.text);
		
		// Cambiamos el evento de cierre
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e){
		    	ClienteChat.close();
		    }
		});

	}

}
