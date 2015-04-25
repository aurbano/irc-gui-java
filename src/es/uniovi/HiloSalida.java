package es.uniovi;

import es.uniovi.popups.CambiarNick;

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
							printMessage(resp.params[1],resp.params[0],resp.params[2]);
						}
						break;
					case "JOIN":
						if(resp.status == 2){
							// Hay que crear una nueva sala
							ClienteChat.joinSala(resp.params[1]);
						}else{
							ClienteChat.addUser(resp.params[1], resp.params[0]);
						}
						ClienteChat.println(resp.params[1],"&raquo; <i style=\"color:green\">"+resp.params[0]+"</i> se ha unido a la sala <i style=\"color:orange\">"+ resp.params[1]+ "</i>");
						break;
					case "LEAVE":
						ClienteChat.println(resp.params[1], "&raquo; <i style=\"color:green\">"+resp.params[0]+"</i> ha abandonado la sala <i style=\"color:orange\">"+ resp.params[1]+ "</i>");
						if(resp.status == 2){
							// Abandonamos la sala
							ClienteChat.leaveSala(resp.params[1]);
						}
						ClienteChat.removeUser(resp.params[1], resp.params[0]);
						break;
					case "NICK":
						ClienteChat.println("&raquo; <i style=\"color:blue\">"+resp.params[0]+"</i> ha cambiado su nick a <i style=\"color:green\">"+ resp.params[1] + "</i>");
						if(resp.status == 2){
							// Si has cambiado tu usuario
							ClienteChat.nick = resp.params[1];
						}
						// Reemplaza el nombre del usuario
						ClienteChat.replaceUser(resp.params[0], resp.params[1]);
						break;
					case "QUIT":
						ClienteChat.println("&raquo; <span style=\"color:green\">"+resp.params[0]+"</span> se ha desconectado.");
						ClienteChat.s.close();
						ClienteChat.quit = true;
						if(resp.status == 1){
							// Otro usuario ha hecho el QUIT, lo sacamos de las listas de cada sala
							ClienteChat.removeUserAll(resp.params[0]);
						}else{
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
							ClienteChat.listSalas(aux);
						}else{
							print += "No hay salas";
						}
						ClienteChat.println(ClienteChat.sala,print);
						break;
					case "WHO":
						aux = separar(resp.params[1]);
						String printMsg = "&raquo; Usuarios en <span style=\"color:orange\">"+resp.params[0]+"</span>: ";
						if(aux.length>0){
							for(int i=0; i<aux.length; i++){
								printMsg += aux[i];
								if(i<aux.length - 1) printMsg += ", ";
							}
							ClienteChat.addUsers(resp.params[0], aux);
						}else{
							printMsg += "No hay salas";
						}
						ClienteChat.println(resp.params[0],printMsg);
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
		if(resp.type=="NICK"){
			// El nick ya estaba cogido
			new CambiarNick(resp.params[0]);
			return;
		}
		ClienteChat.println("<b style=\"color:red\">&raquo;</b> <b style=\"color:white; background:red\">Error: "+resp.params[0]+"</b>");
	}
	
	/**
	 * Separa los parametros de entrada por ;
	 * @param parametros
	 * @return Lista de parametros separados
	 */
	private String[] separar(String parametros){
		String[] ret = parametros.split(";");
		return ret;
	}
	
	/**
	 * Esta funcion recibe la sala, el nick y el texto y prepara un mensaje "bonito" de salida.
	 * Dicha preparacion incluye sustituir cadenas de emoticono por sus correspondientes imagenes,
	 * destacar el nombre del usuario actual (menciones) y decidir que funcion de escritura usar.
	 * La diferencia entre las funciones de escritura esta en que println escribe en una sala sin mas,
	 * y printMsg escribe en una sala, y ademas comprueba si no esta seleccionada, en cuyo caso cambia
	 * el color de la pestaña a naranja, para informar de que hay nuevos mensajes.
	 * @param sala
	 * @param nick
	 * @param msg
	 */
	private void printMessage(String sala, String nick, String msg){
		//msg.replaceAll("(:\\)|:\\)|:\\(|:\\||<3|\\(L\\))", "<img src=\"file:/img/icons/smile.png\" height=\"16\" width="16" />");
		if(nick.equals(ClienteChat.nick)){
			nick = "<span style=\"background:green;color:white\"> "+nick+" </span>";
		}else{
			nick = "<span style=\"color:green\"> "+nick+" </span>";
		}
		// Emoticonos
		msg = msg.replaceAll(":(smile|sad|wink|wow|surprise|meh|what|love|hmm):", "<img src=\"file:img/icons/$1.png\" height=\"22\" width=\"22\" style=\"display:inline-block; vertical-align:middle;\" />");
		// Menciones
		msg = msg.replace(ClienteChat.nick,"<strong style=\"color:cyan\">"+ClienteChat.nick+"</strong>");
		if(!nick.equals(ClienteChat.nick)){
			// Un mensaje de un usuario diferente, printMsg decide si pintar la pestaña de la sala de naranja
			// si no estuviese seleccionada
			ClienteChat.printMsg(sala, "<span style=\"color:orange\">"+sala+"</span> |"+nick+"&gt; " +msg);
		}else{
			// Muestra el mensaje de manera normal, ya que lo escribio el usuario que lo pone.
			ClienteChat.println(sala, "<span style=\"color:orange\">"+sala+"</span> |"+nick+"&gt; " +msg);
		}
	}
}
