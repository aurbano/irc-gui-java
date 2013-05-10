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
	public static JTextField msg;
	private static JTabbedPane tabs;
	public static JTextArea users;
	private static Map<String, Sala> salas;
	private static ArrayList<String> salasList;
	private static JMenu salasMenu;
	public static String sala;
	private static JMenuItem abandonarSala;
	
	/**
	 * Variable para el nombre de usuario
	 */
	static String nick = "";
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
	
	/**
	 * Método principal del Cliente, muestra por pantalla algo de información
	 * y lanza los hilos de entrada por teclado y salida por pantalla.
	 * @param args Es necesario especificar IP y puerto del servidor.
	 */
	public static void main(String[] args){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ClienteChat();
					ClienteChat.frame.setVisible(true);
					ClienteChat.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					// Iniciamos las salas
					salas = new HashMap<String, Sala>();
					salasList = new ArrayList<String>();
					
					// Espera a que el usuario configure el Chat
					config();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
	 * Arranca la interfaz
	 */
	public ClienteChat(){
		initialize();
	}
	
	/**
	 * Escribe en una sala determinada
	 * @param text
	 */
	public static void println(String sala, String text){
		final String s = sala, t = text;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Sala actual = salas.get(s);
				if(actual != null){
					salas.get(s).append(t);
				}
			}
		});
	}
	
	/**
	 * Escribe un mensaje en la sala seleccionada, si la sala
	 * no es la actual, cambia el color de la pestaña para llamar la atencion
	 * del usuario.
	 * @param text
	 */
	public static void printMsg(String sala, String text){
		final String s = sala, t = text;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Sala salaDelMensaje = salas.get(s);
				if(salaDelMensaje != null){
					salas.get(s).append(t);
					// Cambia a naranja si no es la seleccionada
					if(!s.equals(ClienteChat.sala)){
						tabs.setBackgroundAt(salaDelMensaje.num, Color.ORANGE);
					}
				}
			}
		});
	}
	
	/**
	 * Escribe en todas las salas.
	 * @param text
	 */
	public static void println(String text){
		final String t = text;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				for (Map.Entry<String, Sala> entry : salas.entrySet()) {
					entry.getValue().append(t);
				}
			}
		});
	}
	
	/**
	 * Envia el /JOIN para unirse a una sala 
	 * @param name
	 */
	public static void sendJoin(String name){
		try{
			//addSala(name, tabs.getTabCount()-1);
			ClienteChat.netOut.send(new Comando("/JOIN "+name));
		}catch(Exception e){}
	}
	
	/**
	 * Cambia a la pestaña de la sala seleccionada, si no la hay la crea
	 * requiere que la sala exista!! Se ejecuta basicamente a la vuelta
	 * de un JOIN
	 * @param name Sala a la que cambiar
	 */
	public static void joinSala(String n){
		final String name = n;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Mira si ya esta abierta:
				Sala sala = salas.get(name);
				if(sala != null){
				    // Seleccionamos la sala
					ClienteChat.sala = name;
					tabs.setSelectedIndex(sala.num);
					sala.displayUsers();
				}else{
					addSala(name, tabs.getTabCount()-1);
					// Seleccionamos la ultima
					tabs.setSelectedIndex(tabs.getTabCount()-1);
					ClienteChat.sala = name;
					// Si el numero de pestañas es menor que 2, implica que se trata
					// de la pestaña "Acerca" o de la pestaña vacia que crea nuevas pestañas.
					if(tabs.getTabCount()>1){
						addMenuSala(name);
						try{
							// Cuando nos conectamos por primera vez, lanzamos un WHO para ver quien esta ya dentro
							ClienteChat.netOut.send(new Comando("/WHO "+name));
						}catch(Exception e){ }	
					}
				}
			}
		});
	}
	
	/**
	 * Envia el /LEAVE para unirse a una sala 
	 * @param name
	 */
	public static void sendLeave(String name){
		try{
			ClienteChat.netOut.send(new Comando("/LEAVE "+name));
		}catch(Exception e){}
	}
	
	/**
	 * Abandona una sala determinada, llama a algunas funciones
	 * de la interfaz, asi que lo envolvemos en un invokeLater.
	 * Despues de eliminar la sala actualiza el numero de pestaña
	 * de las que estaban por delante, ya que ahora han bajado todas
	 * 1 posicion.
	 * @param name
	 */
	public static void leaveSala(String n){
		final String name = n;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Sala sala = salas.get(name);
				if(sala != null){
					tabs.setSelectedIndex(0);
					tabs.remove(sala.num);
					ClienteChat.sala = "Acerca";
					salas.remove(name);
					
					int index = sala.num;
					HashMap<String, Sala> aux = new HashMap<String, Sala>();
					Sala s;
					// Ahora hay que reducir el identificador de todas las
					// salas que esten por delante
					for (Map.Entry<String, Sala> entry : salas.entrySet()) {
						s = entry.getValue();
						if(s.num > index){
							s.num--;
							aux.put(entry.getKey(), s);
						}
					}
					salas = aux;
				}
			}
		});
	}
	
	/**
	 * Genera la lista de usuarios activos por sala
	 * que se muestra a la derecha de cada sala.
	 * La informacion de usuarios activos se almacena en un Arraylist
	 * de cada Sala.
	 * @param list
	 */
	public static void addUsers(String sala, String[] list){
		Sala s = salas.get(sala);
		if(s != null){
			s.addUsers(list);
		}
	}
	
	/**
	 * Añade un usuario a la lista de usuarios online
	 * de cada sala.
	 * @param sala
	 * @param user
	 */
	public static void addUser(String sala, String user){
		Sala s = salas.get(sala);
		if(s != null){
			s.addUser(user);
		}
	}
	
	/**
	 * Elimina un usuario de una sala
	 * @param sala
	 * @param user
	 */
	public static void removeUser(String sala, String user){
		Sala s = salas.get(sala);
		if(s != null){
			s.removeUser(user);
		}
	}
	
	/**
	 * Elimina un usuario de todas las salas
	 * (Tras un /QUIT)
	 * @param user
	 */
	public static void removeUserAll(String user){
		for (Map.Entry<String, Sala> entry : salas.entrySet()) {
			entry.getValue().removeUser(user);
		}
	}
	
	/**
	 * Reemplaza un usuario en todas las salas
	 * se deberia utilizar despues de que un usuario
	 * cambie su NICK
	 * @param old
	 * @param user
	 */
	public static void replaceUser(String old, String user){
		for (Map.Entry<String, Sala> entry : salas.entrySet()) {
			entry.getValue().replaceUser(old,user);
		}
	}
	
	/**
	 * Añade una sala al array de salas, sin crear la pestaña
	 * @see joinSala
	 * @param name
	 */
	private static void addSala(String name, int num){
		salas.put(name, new Sala(name, num));
		sala = name; // Cambia a la sala actual
	}
	
	/**
	 * Añade una lista de salas al menu
	 * @param list
	 */
	public static void listSalas(String[] list){
		for(String each : list){
			if(!salasList.contains(each)) salasList.add(each);
		}
		Collections.sort(salasList);
		menuSalas();
	}
	
	/**
	 * Añade una sala al menu
	 * @param sala
	 */
	public static void addMenuSala(String sala){
		if(sala.trim().length() > 0 && !salasList.contains(sala)){
			salasList.add(sala);
			Collections.sort(salasList);
			menuSalas();
		}
	}
	
	/**
	 * Elimina una sala del menu
	 * @param sala
	 */
	public static void removeMenuSala(String sala){
		if(salasList.remove(sala)){
			menuSalas();
		}
	}
	
	/**
	 * Mostrar las salas en el desplegable
	 */
	private static void menuSalas(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				salasMenu.removeAll();
				for(final String each : salasList){
					JMenuItem sala1 = new JMenuItem(each);
					sala1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							Sala s = salas.get(each);
							if(s!=null){
								// Si ya estaba, va a la pestaña
								joinSala(each);
							}else{
								// Si no, envia el /JOIN
								sendJoin(each);
							}
						}
					});
					salasMenu.add(sala1);
				}
			}
		});
	}
	
	/**
	 * Envia una peticion de /NICK, el nick se cambia
	 * cuando el servidor lo confirme
	 * @param nick
	 */
	public static void newNick(String nick){
		try{
			ClienteChat.netOut.send(new Comando("/NICK "+nick));
		}catch(Exception ex){}
	}
	
	/**
	 * Añade una pestaña, solo se llama desde joinSala, que ya esta en un invokeLater
	 * @see joinSala
	 * @param name
	 * @return
	 */
	public static ChatArea addTab(String name){
		JPanel tab = new JPanel();
		tab.setBackground(Color.WHITE);
		tab.setBorder(null);
		
		tabs.add(name, tab);
		tab.setLayout(new BorderLayout(0, 0));
		
		ChatArea chat = new ChatArea();
		//txtpnchevi.setEditable(false);
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
	 * Inicia la conexion al servidor.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	static void connect() throws UnknownHostException, IOException{
		// Intenta conectarse
		s = new Socket(host, port);
		
		// Añade la sala inicial
		joinSala("Acerca");
		
		// Bienvenida
		ClienteChat.println("Acerca","<pre style=\"background-color:#efefef; color:#444444; width:352px;\">" +
				"|-----------------  <span style=\"color:red\">ClienteChat v3</span>  -------------------|\n" +
				"| En el Menu superior puedes encontrar Ayuda sobre el  |\n" +
				"| funcionamiento del programa.                         |\n" +
				"|                                                      |\n" +
				"| Esta pestaña únicamente muestra mensajes genéricos   |\n" +
				"| del servidor e información, aunque permite ejecutar  |\n" +
				"| comandos.                                            |\n" +
				"|                                                      |\n" +
				"| Para abandonar una sala puedes ir a Salas > Abandonar|\n" +
				"| sala, o pulsar Ctrl+W                                |\n" +
				"|------------------------------------------------------|\n" +
				"</pre>");
		
		// Lanzamos los hilos
		netOut = new SalidaRed();
		netIn = new EntradaRed();
		entrada = new HiloEntrada();
		salida = new HiloSalida();
	}
	
	/**
	 * Lanza la ventana de configuracion
	 */
	private static void config(){
		new Config();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("ClienteChat v3");
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setFont(new Font("Georgia", Font.PLAIN, 25));
		frame.setBounds(100, 100, 720, 574);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setMinimumSize(new Dimension(400, 300));
		// Intenta poner un icono a la app
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
		
		JButton smileBtn = new JButton("");
		smileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msg.setText(msg.getText()+":smile:");
			}
		});
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
		
		JButton send = new JButton("Enviar");
		btnBottom.add(send);
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
		
		JMenu mnNewMenu = new JMenu("ClienteChat");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Cerrar");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClienteChat.close();
			}
		});
		
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenu tools = new JMenu("Salas");
		menuBar.add(tools);
		
		salasMenu = new JMenu("Entrar en");
		tools.add(salasMenu);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Actualizar lista");
		mntmNewMenuItem_4.setMnemonic('L');
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					ClienteChat.netOut.send(new Comando("/LIST"));
				}catch(Exception ex){}
			}
		});
		tools.add(mntmNewMenuItem_4);
		
		abandonarSala = new JMenuItem("Abandonar sala (W)");
		abandonarSala.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClienteChat.sendLeave(ClienteChat.sala);
			}
		});
		abandonarSala.setEnabled(false);
		tools.add(abandonarSala);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		header.add(panel_2, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("ClienteChat v3");
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
		// Escuchamos cambios de pestaña
		tabs.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	            if(tabs.getSelectedIndex()>0){
	            	sala = tabs.getTitleAt(tabs.getSelectedIndex());
	            	salas.get(sala).displayUsers();
	            	abandonarSala.setEnabled(true);
	            	// La pasa a blanco
	            	tabs.setBackgroundAt(tabs.getSelectedIndex(), Color.WHITE);
	            }else{
	            	users.setText("-Entra en una sala-");
	            	abandonarSala.setEnabled(false);
	            }
	        }
	    });
		
		// Accion para abandonar salas
		AbstractAction leaveSalaAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if(tabs.getSelectedIndex()>0 && tabs.getSelectedIndex() < tabs.getTabCount()-1){
					ClienteChat.sendLeave(ClienteChat.sala); 
				}
			}
		};
		
		// Detectar Cntr+W
		KeyStroke ctrlW = KeyStroke.getKeyStroke("control W");
		
		InputMap inputMap = content.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(ctrlW, "closeTab");
		 
	    // Now add a single binding for the action name to the anonymous action
	    content.getActionMap().put("closeTab", leaveSalaAction);
	    
	    // Seguimos con el diseño
		
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
		users.setText("- Entra en una sala -");
		
		JScrollPane scrollPane = new JScrollPane(users);
		usersPanel.add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel usersRightSeparator = new JPanel();
		usersRightSeparator.setPreferredSize(new Dimension(30, 10));
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
