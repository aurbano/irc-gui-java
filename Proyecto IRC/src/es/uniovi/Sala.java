package es.uniovi;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Almacena la informacion de cada sala, los usuarios online,
 * la referencia al area de chat... etc
 *
 */
public class Sala {
	private ChatArea chat;
	private ArrayList<String> users;
	public String name;
	public int num;
	
	/**
	 * Crea una nueva sala, siempre es llamado desde un invokeLater
	 * @see joinSala
	 * @param name
	 * @param num
	 */
	public Sala(String name, int num){
		this.name = name;
		chat = ClienteChat.addTab(name);
		users = new ArrayList<String>();
		this.num = num;
	}
	
	/**
	 * Escribe texto en la pantalla del chat
	 * @param text
	 */
	public void append(String text){
		chat.append(text);
	}
	
	/**
	 * Añade usuarios a la lista de online sala de una lista de WHO
	 * @param list
	 */
	public void addUsers(String[] list){
		users.clear();
		for(String each : list){
			users.add(each);
		}
		try{
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					displayUsers();
				}
			});
		}catch(Exception e){ e.printStackTrace(); }
	}
	
	/**
	 * Añade un usuario a la lista de online de la sala
	 * @param user
	 */
	public void addUser(String user){
		if(!users.contains(user)){
			users.add(user);
		}
		try{
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					displayUsers();
				}
			});
		}catch(Exception e){ e.printStackTrace(); }
	}
	
	/**
	 * Elimina un usuario de la lista de online de la sala
	 * @param user
	 */
	public void removeUser(String user){
		users.remove(user);
		try{
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					displayUsers();
				}
			});
		}catch(Exception e){ e.printStackTrace(); }
	}
	
	/**
	 * Reemplaza un usuario de la lista de online de la sala
	 * util despues de que un usuario haga un /NICK
	 * @param old
	 * @param user
	 */
	public void replaceUser(String old, String user){
		if(users.contains(old)){
			// Remove and add new
			users.remove(old);
			users.add(user);
			// Update
			try{
				EventQueue.invokeAndWait(new Runnable() {
					public void run() {
						displayUsers();
					}
				});
			}catch(Exception e){ e.printStackTrace(); }
		}
	}
	
	/**
	 * Actualiza los usuarios que se muestran en la lista de online
	 */
	public void displayUsers(){
		if(name != ClienteChat.sala) return;
		
		Collections.sort(users);
		ClienteChat.users.setText("");
		for(String user : users){
			ClienteChat.users.append(" · "+user+"\n");
		}
	}
	
	/**
	 * Para comparar salas, son iguales si tienen el mismo nombre
	 */
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Sala))return false;
	    Sala sala = (Sala)other;
	    if(sala.name == name) return true;
	    return false;
	}
}
