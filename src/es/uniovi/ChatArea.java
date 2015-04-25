package es.uniovi;

import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

/**
 * Custom JTextPane with support for the append function
 */
public class ChatArea extends JTextPane {

	private String content="";
	
	public ChatArea(){
		super();
		setEditable(false);
	}
	
	/**
	 * Adding append function, to keep adding data to the end of the chat window
	 * @param text New text to be added
	 */
	public void append(String text) {
		content += text+"<br />";
		super.setText("<html><head></head><body>"+content+"</body></html>");
		// Keep the box scrolled to the bottom
		DefaultCaret caret = (DefaultCaret) getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
	}
}