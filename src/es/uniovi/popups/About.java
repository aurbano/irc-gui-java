package es.uniovi.popups;

import java.awt.Dialog;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;

import es.uniovi.ChatClient;

public class About extends JDialog {

	/**
	 * Create the dialog.
	 */
	public About() {
		final JDialog window = new JDialog(ChatClient.frame, "About ChatClient v3", Dialog.ModalityType.APPLICATION_MODAL);
		
		window.setBounds(100, 100, 512, 274);
		window.getContentPane().setLayout(null);
		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(0, 0, 496, 243);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblChatClient = new JLabel("ChatClient v3");
		lblChatClient.setBounds(5, 5, 481, 52);
		lblChatClient.setFont(new Font("Georgia", Font.PLAIN, 20));
		lblChatClient.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblChatClient);
		
		JLabel lblAuthor2 = new JLabel("Irene Mart\u00EDnez de Soto (UO217249)");
		lblAuthor2.setHorizontalAlignment(SwingConstants.CENTER);
		lblAuthor2.setBounds(5, 114, 481, 52);
		contentPanel.add(lblAuthor2);
		
		JLabel lblAuthor1 = new JLabel("Alejandro Urbano \u00C1lvarez (UO212087)");
		lblAuthor1.setHorizontalAlignment(SwingConstants.CENTER);
		lblAuthor1.setBounds(5, 57, 481, 52);
		contentPanel.add(lblAuthor1);
		
		JLabel lblUniversity = new JLabel("EPI Gij\u00F3n - 2013");
		lblUniversity.setForeground(Color.GRAY);
		lblUniversity.setHorizontalAlignment(SwingConstants.CENTER);
		lblUniversity.setBounds(5, 177, 481, 52);
		contentPanel.add(lblUniversity);
		
		
		window.setVisible(true);
		window.setAlwaysOnTop(true);
	}
}
