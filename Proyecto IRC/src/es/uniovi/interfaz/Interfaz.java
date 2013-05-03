package es.uniovi.interfaz;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTextPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JSeparator;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;

public class Interfaz {

	private JFrame frame;
	private JTextField msg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interfaz window = new Interfaz();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Interfaz() {
		initialize();
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
		
		JTextPane chat = new JTextPane();
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
