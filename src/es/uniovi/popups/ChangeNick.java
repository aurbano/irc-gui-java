package es.uniovi.popups;

import java.awt.Dialog;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;

import es.uniovi.ChatClient;

import java.awt.SystemColor;

public class ChangeNick extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final JTextField textField = new JTextField();
	private String msg = "";
	
	public ChangeNick(){
		gui();
	}
	
	public ChangeNick(String msg){
		this.msg = msg;
		gui();
	}

	/**
	 * Create the dialog.
	 */
	private void gui() {
		final JDialog window = new JDialog(ChatClient.frame, "Change Nick", Dialog.ModalityType.APPLICATION_MODAL);
		final JLabel nick;
		
		window.setBounds(100, 100, 429, 181);
		window.getContentPane().setLayout(null);
		contentPanel.setBackground(SystemColor.control);
		contentPanel.setBounds(0, 0, 496, 167);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		textField.addActionListener(e -> {
            ChatClient.newNick(textField.getText());
            window.dispose();
        });
		textField.setBounds(49, 45, 313, 30);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		nick = new JLabel("New nick");
		nick.setBounds(49, 29, 109, 14);
		contentPanel.add(nick);
		
		JButton btnNewButton = new JButton("Ok");
		btnNewButton.addActionListener(e -> {
            ChatClient.newNick(textField.getText());
            window.dispose();
        });
		btnNewButton.setBounds(273, 96, 89, 23);
		contentPanel.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel(msg);
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(49, 100, 201, 14);
		contentPanel.add(lblNewLabel);
		
		window.setVisible(true);
		window.setAlwaysOnTop(true);
	}
}
