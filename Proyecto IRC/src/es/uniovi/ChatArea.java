package es.uniovi;

import javax.swing.JTextPane;

public class ChatArea extends JTextPane {
	
	String content="";
	
	public ChatArea() {
		super();
	}
	
	/**
	 * Añadimos el metodo append, para poner HTML
	 * al final del chat
	 * @param text
	 */
	public void append(String text) {
		content += text+"<br />";
		super.setText("<html><head></head><body>"+content+"</body></html>");
		//this.setCaretPosition(this.getCaretPosition()+text.length());
	}
}