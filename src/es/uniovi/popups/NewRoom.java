package es.uniovi.popups;

import java.awt.Dialog;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

import es.uniovi.ChatClient;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class NewRoom extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	/**
	 * Create the dialog.
	 */
	public NewRoom() {
		final JDialog ventana = new JDialog(ChatClient.frame, "New room", Dialog.ModalityType.APPLICATION_MODAL);
		
		ventana.setTitle("New room");
		ventana.setBounds(100, 100, 326, 140);
		ventana.getContentPane().setLayout(null);
		contentPanel.setBackground(SystemColor.control);
		contentPanel.setBounds(0, 0, 310, 106);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		ventana.getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			textField = new JTextField();
			textField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ChatClient.sendJoin(textField.getText());
					ventana.dispose();
				}
			});
			textField.setBounds(22, 37, 265, 20);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		
		JLabel lblNombre = new JLabel("Name");
		lblNombre.setBounds(22, 22, 46, 14);
		contentPanel.add(lblNombre);
		{
			JButton okButton = new JButton("OK");
			okButton.setForeground(SystemColor.textHighlight);
			okButton.setBackground(SystemColor.text);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ChatClient.sendJoin(textField.getText());
					ventana.dispose();
				}
			});
			okButton.setBounds(232, 72, 55, 23);
			contentPanel.add(okButton);
			okButton.setActionCommand("OK");
			ventana.getRootPane().setDefaultButton(okButton);
		}
		ventana.setVisible(true);
		ventana.setAlwaysOnTop(true);
	}
}
