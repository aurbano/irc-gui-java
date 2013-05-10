package es.uniovi.popups;

import java.awt.Dialog;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import es.uniovi.ClienteChat;

import java.awt.SystemColor;

public class CambiarNick extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final JTextField textField = new JTextField();
	private String msg = "";
	
	public CambiarNick(){
		interfaz();
	}
	
	public CambiarNick(String msg){
		this.msg = msg;
		interfaz();
	}

	/**
	 * Create the dialog.
	 */
	public void interfaz() {
		final JDialog ventana = new JDialog(ClienteChat.frame, "Cambiar Nick", Dialog.ModalityType.APPLICATION_MODAL);
		final JLabel nick;
		
		ventana.setBounds(100, 100, 429, 181);
		ventana.getContentPane().setLayout(null);
		contentPanel.setBackground(SystemColor.control);
		contentPanel.setBounds(0, 0, 496, 167);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		ventana.getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClienteChat.newNick(textField.getText());
				ventana.dispose();
			}
		});
		textField.setBounds(49, 45, 313, 30);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		nick = new JLabel("Nuevo nick");
		nick.setBounds(49, 29, 109, 14);
		contentPanel.add(nick);
		
		JButton btnNewButton = new JButton("Ok");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClienteChat.newNick(textField.getText());
				ventana.dispose();
			}
		});
		btnNewButton.setBounds(273, 96, 89, 23);
		contentPanel.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel(msg);
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(49, 100, 201, 14);
		contentPanel.add(lblNewLabel);
		
		ventana.setVisible(true);
		ventana.setAlwaysOnTop(true);
	}
}
