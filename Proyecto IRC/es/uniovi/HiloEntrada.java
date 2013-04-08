package es.uniovi;
import java.io.*;
public class HiloEntrada {
	String line;
	public HiloEntrada(){
		
	}

	public void run(){
	LineNumberReader text = new LineNumberReader (new InputStreamReader(System.in));
	
	while(true){
		try {
			
			line = text.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	}
	
}
