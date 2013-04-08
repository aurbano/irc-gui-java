package es.uniovi;

public class EntradaRed extends Thread {
	
	public EntradaRed(){}
	
	public void run(){
		String message;
		Respuesta res;
		Network net = new Network();
		while(true){
			try{
				message = net.recv();
				if(message.length() > 0){
					res = new Respuesta(message);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}

}
