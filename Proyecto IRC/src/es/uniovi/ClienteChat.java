package es.uniovi;

import java.io.IOException;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *  Cliente de consola de chat, para el hito 1
 *  Lanza los hilos necesarios para el funcionamiento.
 */
public class ClienteChat {
	
	private JFrame frame;
	private JTextField msg;
	static JTextPane chat;
	
	/**
	 * Variable para el nombre de usuario
	 */
	static String nick = "Anonimo";
	/**
	 * Nombre de la sala actual
	 */
	static String sala = "";
	/**
	 * IP del servidor
	 */
	static String host;
	static int port;
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
		
		try{
			host = args[0];
			port = new Integer(args[1]);
		}catch(Exception e){
			System.err.println("Error: Debes especificar IP del servidor y puerto");
			System.exit(-1);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteChat window = new ClienteChat();
					window.frame.setVisible(true);
					window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					// Bienvenida
					ClienteChat.println(">> Conectando al servidor...");
					
					// Con la ventana abierta
					s = new Socket(host, port);
					
					// Lanzamos los hilos
					netOut = new SalidaRed();
					netIn = new EntradaRed();
					entrada = new HiloEntrada();
					salida = new HiloSalida();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ClienteChat(){
		initialize();
	}
	
	public static void println(String text){
		memory += text+"<br />";
		chat.setText("<html><head></head><body>"+memory+"</body></html>");
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setFont(new Font("Georgia", Font.PLAIN, 25));
		frame.setBounds(100, 100, 526, 374);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
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
		
		JLabel lblNewLabel = new JLabel("ClienteChat v3");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 22));
		header.add(lblNewLabel);
		
		JPanel content = new JPanel();
		content.setBackground(Color.WHITE);
		frame.getContentPane().add(content, BorderLayout.CENTER);
		content.setLayout(new BorderLayout(0, 0));
		
		JPanel contentLeft = new JPanel();
		contentLeft.setBackground(Color.WHITE);
		content.add(contentLeft, BorderLayout.CENTER);
		contentLeft.setLayout(new BorderLayout(0, 0));
		
		JPanel chatSeparator = new JPanel();
		chatSeparator.setBackground(SystemColor.text);
		contentLeft.add(chatSeparator, BorderLayout.WEST);
		
		chat = new JTextPane();
		//txtpnchevi.setEditable(false);
		chat.setContentType("text/html");
		chat.setBackground(SystemColor.control);
		contentLeft.add(chat, BorderLayout.CENTER);
		
		JScrollPane scrollBar = new JScrollPane(chat);
		scrollBar.setEnabled(false);
		scrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentLeft.add(scrollBar, BorderLayout.CENTER);
		
		JPanel contentRight = new JPanel();
		contentRight.setBackground(Color.WHITE);
		content.add(contentRight, BorderLayout.EAST);
		contentRight.setLayout(new BoxLayout(contentRight, BoxLayout.X_AXIS));
		
		JPanel usersSeparator = new JPanel();
		usersSeparator.setBackground(Color.WHITE);
		contentRight.add(usersSeparator);
		
		JTextArea users = new JTextArea();
		users.setEditable(false);
		users.setLineWrap(true);
		users.setBackground(SystemColor.control);
		users.setColumns(10);
		users.setRows(10);
		contentRight.add(users);
		
		JScrollPane scrollPane = new JScrollPane(users);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentRight.add(scrollPane);
		
		JPanel usersRightSeparator = new JPanel();
		usersRightSeparator.setBackground(SystemColor.text);
		contentRight.add(usersRightSeparator);
	}

}
