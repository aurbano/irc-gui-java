package es.uniovi;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.*;

/**
 * Generate buttons with icons in them easily
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
				ChatClient.msg.setText(ChatClient.msg.getText()+":"+name+":");
			}
		});
		setBackground(SystemColor.text);
		setIcon(new ImageIcon("img/icons/"+name+".png"));
	}
}
