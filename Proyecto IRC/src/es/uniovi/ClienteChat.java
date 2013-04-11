package es.uniovi;

/**
 *  Cliente de consola de chat, para el hito 1
 *  Lanza los hilos necesarios para el funcionamiento.
 */
public class ClienteChat {
	/**
	 * Variable para el nombre de usuario
	 */
	static String nick;
	/**
	 * Nombre de la sala actual
	 */
	static String sala = "pruebas";
	/*
	 * Lanzamos algunos hilos como estáticos para poder acceder
	 * a ellos desde los demás.
	 */
	static SalidaRed netOut = new SalidaRed();
	static EntradaRed netIn = new EntradaRed();
	static Network net = new Network();
	
	/**
	 * Método principal del Cliente, muestra por pantalla algo de información
	 * y lanza los hilos de entrada por teclado y salida por pantalla.
	 * @param args Es necesario especificar el nick por parametro.
	 */
	public static void main(String[] args){
		System.out.println("ClienteChat v1.0");
		
		
		if(args.length<1){
			System.err.println("Error: No has especificado tu nombre de usuario, pásalo por parámetro.");
			System.exit(-1);
		}
		
		nick = args[0];
		System.out.println("Bienvenido/a "+ nick);
		
		// Lanzamos los hilos
		HiloEntrada in = new HiloEntrada();
		HiloSalida out = new HiloSalida();
	}

}
