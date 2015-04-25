package es.uniovi.popups;

import java.awt.Dialog;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import javax.swing.JTable;

import es.uniovi.ChatClient;

public class Commands extends JDialog {

	/**
	 * Create the dialog.
	 */
	public Commands() {
		final JDialog window = new JDialog(ChatClient.frame, "New room", Dialog.ModalityType.APPLICATION_MODAL);
		
		window.setTitle("Commands - ChatClient v3");
		window.setBounds(100, 100, 512, 205);
		window.getContentPane().setLayout(null);
		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(0, 0, 496, 198);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		/*
		 * Command list
		 */
		String[] columnNames = {"Command","Parameters","Description"};
		
		Object[][] data = {
		    {"/MSG", "Text", "Sends a message, it's also the default command that gets sent if you don't specify another one."},
		    {"/JOIN","Room","Join a room, creating it if it didn't exist."},
		    {"/LEAVE","Room","Abandon a room"},
		    {"/NICK","Nick","Change your nick"},
		    {"/LIST","","Return all available rooms"},
		    {"/WHO","Room","Get list of users connected to specified room."},
		    {"/QUIT","","Close the connection with the server"}
		};

		JTable table = new JTable(data, columnNames);
		table.setShowHorizontalLines(false);
		table.setBounds(23, 136, 374, -120);
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setMaxWidth(70);
		tcm.getColumn(1).setMaxWidth(90);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setEnabled(false);
		scrollPane.setBounds(10, 11, 476, 142);
		table.setFillsViewportHeight(true);
		contentPanel.add(scrollPane);
		window.setVisible(true);
		window.setAlwaysOnTop(true);
	}
}
