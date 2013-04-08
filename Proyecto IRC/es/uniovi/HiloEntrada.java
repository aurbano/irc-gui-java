package es.uniovi;
import java.io.*;
public class HiloEntrada {
	
	public HiloEntrada(){
		
	}

	public void run(){
		LineNumberReader text = new LineNumberReader (new InputStreamReader(System.in));
		String line;
		Comando cmd;
		while(true){
			try {
				
				line = text.readLine();
				// Genera el comando
				cmd = new Comando(line);
				
				// Envia el comando
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		
	}
	
}
