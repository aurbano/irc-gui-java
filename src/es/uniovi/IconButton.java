package es.uniovi;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.*;

/**
 * Generate buttons with icons in them easily
 *
 */
class IconButton extends JButton{

	public IconButton(String n){
		super("");
		final String name = n;
		setToolTipText(name);
		addActionListener(e -> ChatClient.msg.setText(ChatClient.msg.getText()+":"+name+":"));
		setBackground(SystemColor.text);
		setIcon(new ImageIcon("img/icons/"+name+".png"));
	}
}
