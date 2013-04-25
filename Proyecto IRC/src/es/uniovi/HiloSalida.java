package es.uniovi;

/**
 * Hilo de salida, espera a que lleguen nuevos mensajes y los muestra por pantalla
 * también determina cómo tratar cada comando.
 */
public class HiloSalida extends Thread{
	/**
	 * Constructor, lanza el hilo
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
				if(resp.status==3){
					error(resp);
					continue;
				}
				switch(resp.type){
				/*
				 * Implementamos algo de logica para los diferentes comandos
				 * de cara al siguiente hito.
				 */
					case "MSG":
						if(resp.params.length > 2){
							System.out.println(resp.params[1]+"|"+resp.params[0]+"> " +resp.params[2]);
						}
						break;
					case "JOIN":
						System.out.println(">> "+resp.params[0]+" se ha unido a la sala "+ resp.params[1]);
						ClienteChat.sala = resp.params[1];
						break;
					case "LEAVE":
						System.out.println(">> "+resp.params[0]+" ha abandonado la sala "+ resp.params[1]);
						ClienteChat.sala = "";
						break;
					case "NICK":
						System.out.println(">> "+resp.params[0]+" ha cambiado su nick a "+ resp.params[1]);
						ClienteChat.nick=resp.params[1];
						break;
					case "QUIT":
						System.out.println(">> "+resp.params[0]+" se ha desconectado.");
					case "LIST":
						System.out.println(">> Salas: "+resp.params[0]);
						break;
					case "WHO":
						System.out.println(">> Usuarios en "+resp.params[0]+": "+resp.params[1]);
						break;
					case "HELLO":
						System.out.println(">> "+resp.params[0]);
						break;
					case "OTROS":
						error(resp);
						break;
					default:
						System.out.println(">> Unknown type ("+resp.type+") Status="+resp.status+". Mensaje: "+resp.params[0]);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void error(Respuesta resp){
		System.err.println(">> Servidor: "+resp.params[0]);
	}
}
