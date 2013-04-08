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
				if (line.startsWith("/")){
					cmd=new Comando(line.split(" "));
				}else{
					cmd=new Comando(new String[]{"/MSG", line});
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		
	}
	
}
