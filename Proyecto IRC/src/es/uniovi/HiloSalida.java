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
		String[] aux;
		while(!ClienteChat.quit){
			try{
				resp = ClienteChat.netIn.remove();
				if(resp.status==3){
					error(resp);
					continue;
				}
				switch(resp.type){
				/*
				 * Implementamos algo de logica para los diferentes comandos.
				 */
					case "MSG":
						if(resp.params.length > 2){
							ClienteChat.println("<span style=\"color:orange\">"+resp.params[1]+"</span> |"+resp.params[0]+"&gt; " +resp.params[2]);
						}
						break;
					case "JOIN":
						if(resp.status == 2){
							// Hay que crear una nueva sala
							ClienteChat.joinSala(resp.params[1]);
						}
						ClienteChat.println("&raquo; <i style=\"color:green\">"+resp.params[0]+"</i> se ha unido a la sala <i style=\"color:orange\">"+ resp.params[1]+ "</i>");
						break;
					case "LEAVE":
						ClienteChat.println("&raquo; <i style=\"color:green\">"+resp.params[0]+"</i> ha abandonado la sala <i style=\"color:orange\">"+ resp.params[1]+ "</i>");
						break;
					case "NICK":
						ClienteChat.println("&raquo; <i style=\"color:blue\">"+resp.params[0]+"</i> ha cambiado su nick a <i style=\"color:green\">"+ resp.params[1] + "</i>");
						if(resp.status == 2){
							// Si has cambiado tu usuario
							ClienteChat.nick = resp.params[1];
						}
						break;
					case "QUIT":
						ClienteChat.println("&raquo; <span style=\"color:green\">"+resp.params[0]+"</span> se ha desconectado.");
						ClienteChat.s.close();
						ClienteChat.quit = true;
						if(resp.status == 2){
							// El usuario actual ha enviado el QUIT, cerramos la aplicación
							ClienteChat.finish();
						}
						break;
					case "LIST":
						aux = separar(resp.params[0]);
						String print = "&raquo; Salas: ";
						if(aux.length>0){
							for(int i=0; i<aux.length; i++){
								print += aux[i];
								if(i<aux.length - 1) print += ", ";
							}
						}else{
							print += "No hay salas";
						}
						ClienteChat.println(print);
						break;
					case "WHO":
						aux = separar(resp.params[1]);
						String printMsg = "&raquo; Usuarios en <span style=\"color:orange\">"+resp.params[0]+"</span>: ";
						if(aux.length>0){
							for(int i=0; i<aux.length; i++){
								printMsg += aux[i];
								if(i<aux.length - 1) printMsg += ", ";
							}
						}else{
							printMsg += "No hay salas";
						}
						ClienteChat.println(printMsg);
						break;
					case "HELLO":
						ClienteChat.println("&raquo; "+resp.params[0]);
						// Envia un NICK
						ClienteChat.netOut.send(new Comando("/NICK "+ClienteChat.nick));
						ClienteChat.netOut.send(new Comando("/LIST"));
						break;
					case "OTROS":
						error(resp);
						break;
					default:
						ClienteChat.println("&raquo; <span style=\"color:red\">Unknown command ("+resp.type+")</span> Status="+resp.status+". Mensaje: "+resp.params[0]);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Muestra los errores de manera diferente.
	 * @param resp
	 */
	private void error(Respuesta resp){
		ClienteChat.println("<b style=\"color:red\">&raquo;</b> <b style=\"color:white; background:red\">Error: "+resp.params[0]+"</b>");
	}
	
	/**
	 * Separa los parametros de entrada por ;
	 * @param parametros
	 * @return
	 */
	private String[] separar(String parametros){
		String[] ret = parametros.split(";");
		return ret;
	}
}
