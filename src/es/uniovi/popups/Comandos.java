package es.uniovi.popups;

import java.awt.Dialog;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import javax.swing.JTable;

import es.uniovi.ClienteChat;

public class Comandos extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Create the dialog.
	 */
	public Comandos() {
		final JDialog ventana = new JDialog(ClienteChat.frame, "Nueva sala", Dialog.ModalityType.APPLICATION_MODAL);
		
		ventana.setTitle("Comandos - ClienteChat v3");
		ventana.setBounds(100, 100, 512, 205);
		ventana.getContentPane().setLayout(null);
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(0, 0, 496, 198);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		ventana.getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		/*
		 * Listado de comandos
		 */
		String[] columnNames = {"Comando","Parámetros","Descripción"};
		
		Object[][] data = {
		    {"/MSG", "Texto", "Envia un mensaje"},
		    {"/JOIN","Sala","Se une a una sala, si no existe la crea"},
		    {"/LEAVE","Sala","Abandona una sala"},
		    {"/NICK","Nuevo nick","Cambia el nick"},
		    {"/LIST","","Muestra las salas existentes"},
		    {"/WHO","Sala","Muestra los usuarios conectados en una sala"},
		    {"/QUIT","","Cierra la conexión"}
		};
		
		table = new JTable(data, columnNames);
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
		ventana.setVisible(true);
		ventana.setAlwaysOnTop(true);
	}
}
