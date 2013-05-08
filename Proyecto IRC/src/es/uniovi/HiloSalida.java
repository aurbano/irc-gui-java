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
							ClienteChat.println("<span style=\"color:orange\">"+resp.params[1]+"</span> | <span style=\"color:green\">"+resp.params[0]+"</span> &gt; " +resp.params[2]);
						}
						break;
					case "JOIN":
						ClienteChat.println("&gt;&gt; <i style=\"color:green\">"+resp.params[0]+"</i> se ha unido a la sala <i style=\"color:orange\">"+ resp.params[1]+ "</i>");
						ClienteChat.sala = resp.params[1];
						break;
					case "LEAVE":
						ClienteChat.println("&gt;&gt; <i style=\"color:green\">"+resp.params[0]+"</i> ha abandonado la sala <i style=\"color:orange\">"+ resp.params[1]+ "</i>");
						ClienteChat.sala = "";
						break;
					case "NICK":
						ClienteChat.println("&gt;&gt; <i style=\"color:blue\">"+resp.params[0]+"</i> ha cambiado su nick a <i style=\"color:green\">"+ resp.params[1] + "</i>");
						ClienteChat.nick=resp.params[1];
						break;
					case "QUIT":
						ClienteChat.println("&gt;&gt; "+resp.params[0]+" se ha desconectado.");
						if(resp.status == 2){
							// Hemos enviado un quit
							ClienteChat.finish();
						}
						break;
					case "LIST":
						String[] aux = separar(resp.params[0]);
						ClienteChat.println(">> Salas:");
						for(int i=0; i<aux.length; i++){
							ClienteChat.println("&nbsp;&nbsp;&nbsp;- "+aux[i]);					
						}
						break;
					case "WHO":
						String[] aux2 = separar(resp.params[1]);
						String out = "&gt;&gt; Usuarios en "+resp.params[0]+": <ul style=\"margin:0; padding:0\">";
						for(int i=0; i<aux2.length; i++){
							out += "<li style=\"margin: 0;\">"+aux2[i]+"</li>";					
						}
						ClienteChat.println(out+"</ul>");
						break;
					case "HELLO":
						ClienteChat.println("&gt;&gt; "+resp.params[0]);
						break;
					case "OTROS":
						error(resp);
						break;
					default:
						ClienteChat.println("&gt;&gt; <span style=\"color:red\">Unknown command ("+resp.type+")</span> Status="+resp.status+". Mensaje: "+resp.params[0]);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void error(Respuesta resp){
		ClienteChat.println("<b style=\"color:blue\">&gt;&gt;</b> <b style=\"color:red\">Error: "+resp.params[0]+"</b>");
	}
	public String[] separar(String parametros){
		String[] ret = parametros.split(";");
		return ret;
	}
}
