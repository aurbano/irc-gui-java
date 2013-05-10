package es.uniovi;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.*;

/**
 * Permite insertar varios botones con iconos
 * de manera dinamica en la interfaz.
 *
 */
public class IconButton extends JButton{
	private static final long serialVersionUID = 1L;

	public IconButton(String n){
		super("");
		final String name = n;
		setToolTipText(name);
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClienteChat.msg.setText(ClienteChat.msg.getText()+":"+name+":");
			}
		});
		setBackground(SystemColor.text);
		setIcon(new ImageIcon("img/icons/"+name+".png"));
	}
}
