package es.uniovi;

/**
 *  Cliente de consola de chat
 */
public class ClienteChat {
	static String nick;
	static String sala = "pruebas";
	static SalidaRed netOut = new SalidaRed();
	static EntradaRed netIn = new EntradaRed();
	static Network net = new Network();
	
	public static void main(String[] args){
		System.out.println("ClienteChat v1.0");
		
		
		if(args.length<1){
			System.err.println("Error: No has especificado tu nombre de usuario, pásalo por parámetro.");
			System.exit(-1);
		}
		
		nick = args[0];
		System.out.println("Bienvenido/a "+ nick);
		
		// Lanza los hilos
		HiloEntrada in = new HiloEntrada();
		HiloSalida out = new HiloSalida();
	}

}
