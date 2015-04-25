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

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public About() {
		final JDialog ventana = new JDialog(ChatClient.frame, "About ChatClient v3", Dialog.ModalityType.APPLICATION_MODAL);
		
		ventana.setBounds(100, 100, 512, 274);
		ventana.getContentPane().setLayout(null);
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(0, 0, 496, 243);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		ventana.getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblClientechatV = new JLabel("ChatClient v3");
		lblClientechatV.setBounds(5, 5, 481, 52);
		lblClientechatV.setFont(new Font("Georgia", Font.PLAIN, 20));
		lblClientechatV.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblClientechatV);
		
		JLabel lblIreneMartnezDe = new JLabel("Irene Mart\u00EDnez de Soto (UO217249)");
		lblIreneMartnezDe.setHorizontalAlignment(SwingConstants.CENTER);
		lblIreneMartnezDe.setBounds(5, 114, 481, 52);
		contentPanel.add(lblIreneMartnezDe);
		
		JLabel lblAlejandroUrbanolvarez = new JLabel("Alejandro Urbano \u00C1lvarez (UO212087)");
		lblAlejandroUrbanolvarez.setHorizontalAlignment(SwingConstants.CENTER);
		lblAlejandroUrbanolvarez.setBounds(5, 57, 481, 52);
		contentPanel.add(lblAlejandroUrbanolvarez);
		
		JLabel lblEpiGijn = new JLabel("EPI Gij\u00F3n - 2013");
		lblEpiGijn.setForeground(Color.GRAY);
		lblEpiGijn.setHorizontalAlignment(SwingConstants.CENTER);
		lblEpiGijn.setBounds(5, 177, 481, 52);
		contentPanel.add(lblEpiGijn);
		
		
		ventana.setVisible(true);
		ventana.setAlwaysOnTop(true);
	}
}
