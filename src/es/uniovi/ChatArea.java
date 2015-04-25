package es.uniovi;

import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

public class ChatArea extends JTextPane {
	
	private static final long serialVersionUID = 1L;
	
	String content="";
	
	public ChatArea(){
		super();
		setEditable(false);
	}
	
	/**
	 * Añadimos el metodo append, para poner HTML
	 * al final del chat
	 * @param text
	 */
	public void append(String text) {
		content += text+"<br />";
		super.setText("<html><head></head><body>"+content+"</body></html>");
		// Mantenemos el scroll abajo del todo
		DefaultCaret caret = (DefaultCaret) getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
	}
}