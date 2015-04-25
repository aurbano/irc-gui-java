package es.uniovi;

import java.awt.Dialog;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

class Config {

	/**
	 * Configuration dialog box
	 */
	public Config(){
		final JDialog conf = new JDialog(ChatClient.frame, "Welcome!", Dialog.ModalityType.APPLICATION_MODAL);
		
		JPanel contentPanel = new JPanel();
		final JTextField host;
		final JTextField nick;
		final JSpinner port;
		final JLabel error;
		
		conf.getContentPane().setBackground(Color.WHITE);
		
		conf.setTitle("Welcome - IRC - ChatClient v3");
		conf.setBounds(100, 100, 450, 182);
		conf.getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 434, 147);
		contentPanel.setBackground(SystemColor.control);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		conf.getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblHost = new JLabel("Host");
		lblHost.setBounds(36, 26, 68, 14);
		contentPanel.add(lblHost);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(226, 26, 59, 14);
		contentPanel.add(lblPort);
		
		JLabel lblNick = new JLabel("Nick");
		lblNick.setBounds(284, 26, 69, 14);
		contentPanel.add(lblNick);
		
		error = new JLabel();
		error.setBounds(36, 74, 362, 20);
		error.setForeground(Color.RED);
		contentPanel.add(error);
		
		host = new JTextField();
		host.setBounds(36, 43, 180, 20);
		contentPanel.add(host);
		host.setColumns(20);
		host.setText(ChatClient.host);
		
		port = new JSpinner();
		port.setBounds(226, 43, 48, 20);
		contentPanel.add(port);
		port.setValue(ChatClient.port);
		
		nick = new JTextField();
		nick.setBounds(284, 43, 114, 20);
		nick.addActionListener(arg0 -> {
            if(host.getText().length() < 1){
                error.setText("Specify a host please");
                return;
            }
            if((Integer)port.getValue() < 1){
                error.setText("Specify a valid port please");
                return;
            }
            if(nick.getText().length() < 1){
                error.setText("Specify a nick please");
                return;
            }
            error.setText("");

            ChatClient.host = host.getText();
            ChatClient.port = (Integer)port.getValue();
            ChatClient.nick = nick.getText();

            // Connect
            try{
                ChatClient.connect();
                conf.dispose();
            }catch(UnknownHostException e){
                error.setText("Invalid host");
            }catch(IOException e){
                error.setText("Could not establish a connection with the host");
            }
        });
		contentPanel.add(nick);
		nick.setColumns(10);
		nick.setText(ChatClient.nick);
		{
			JButton okButton = new JButton("Connect");
			okButton.setForeground(SystemColor.textHighlight);
			okButton.setBackground(SystemColor.text);
			okButton.setBounds(161, 100, 124, 23);
			contentPanel.add(okButton);
			okButton.addActionListener(arg0 -> {
                if(host.getText().length() < 1){
                    error.setText("Specify a host please");
                    return;
                }
                if((Integer)port.getValue() < 1){
                    error.setText("Specify a port please");
                    return;
                }
                if(nick.getText().length() < 1){
                    error.setText("Specify a nick please");
                    return;
                }
                error.setText("");

                ChatClient.host = host.getText();
                ChatClient.port = (Integer)port.getValue();
                ChatClient.nick = nick.getText();

                // Connect
                try{
                    ChatClient.connect();
                    conf.dispose();
                }catch(UnknownHostException e){
                    error.setText("Invalid host");
                }catch(IOException e){
                    error.setText("Could not establish a connection");
                }
            });
		}
		
		// Override the window closing event to finish the program
		conf.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e){
		    	System.exit(0);
		    }
		});
		
		conf.setVisible(true);
		conf.setAlwaysOnTop(true);
	}
}
