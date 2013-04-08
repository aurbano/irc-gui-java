package es.uniovi;

/**
 * Hilo de salida, espera a que lleguen nuevos mensajes y los muestra por pantalla
 */
public class HiloSalida extends Thread{
	/**
	 * Constructor
	 */
	public HiloSalida(){
		this.start();
	}
	
	/**
	 * Método run
	 */
	public void run(){
		Respuesta resp;
		while(true){
			try{
				resp = ClienteChat.netIn.remove();
				switch(resp.type){
				// Mostramos segun el formato especificado
				
					case "/MSG":
						if(resp.params.length > 2){
							System.out.println(resp.params[0]+"|"+resp.params[1]+"> "+resp.params[2]);
						}
						break;
						
					case "/JOIN":
						if(resp.params.length > 2){
							System.out.println(">> " +resp.params[0]+" ha entrado en la sala "+resp.params[1]);
						}
						break;
						
					case "/LEAVE":
						if(resp.params.length > 2){
							System.out.println(">> " +resp.params[0]+" ha abandonado la sala "+resp.params[1]);
						}
						break;
						
					case "/NICK":
						if(resp.params.length > 2){
							System.out.println(">> " +resp.params[0]+" ha cambiado su nick a "+resp.params[2]);
						}
						break;
						
					case "/LIST":
						System.out.println(">> Lista de salas");
						break;
						
					case "/QUIT":
						System.out.println(">> " +resp.params[0] + "ha abandonado la sala.");
						break;
					
					case "/ERROR":
						System.out.println(">> ERROR: " +resp.params[0]);
						break;
						
					default:
						System.out.println("> Unknown type: "+resp.toString());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
