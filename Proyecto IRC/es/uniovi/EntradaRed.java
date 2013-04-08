package es.uniovi;

public class EntradaRed extends Thread {
	
	public EntradaRed(){}
	
	public void run(){
		String message;
		Respuesta res;
		while(true){
			try{
				message = Network.recv();
				if(message.length>0){
					res = new Respuesta(message);
				}
			}catch(Exception e){
				e.printStackTrace()
			}
			
		}
	}

}
