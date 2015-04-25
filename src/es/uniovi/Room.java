package es.uniovi;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;

public class Room {
	private ChatArea chat;
	private ArrayList<String> users;
	public String name;
	public int num;
	
	/**
	 * Creates a new room. Always call from an invokelater
	 * @see ChatClient
	 * @param name Room name
	 * @param num Room number
	 */
	public Room(String name, int num){
		this.name = name;
		chat = ChatClient.addTab(name);
		users = new ArrayList<>();
		this.num = num;
	}
	
	/**
	 * Add text to the chat area
	 * @param text HTML formatted text
	 */
	public void append(String text){
		chat.append(text);
	}
	
	/**
	 * Add users to the online list of a room
	 * @param list Online users list
	 */
	public void addUsers(String[] list){
		users.clear();
		Collections.addAll(users, list);
		try{
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					displayUsers();
				}
			});
		}catch(Exception e){ e.printStackTrace(); }
	}
	
	/**
	 * Adds a user to the online list
	 * @param user User name
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
	 * Removes a user from the online list
	 * @param user User name
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
	 * Change a user name from the online list
	 * @param old Old user name
	 * @param user New user name
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
	 * Update th online list
	 */
	public void displayUsers(){
		if(!name.equals(ChatClient.room)) return;
		
		Collections.sort(users);
		ChatClient.users.setText("");
		for(String user : users){
			ChatClient.users.append(" � "+user+"\n");
		}
	}
	
	/**
	 * Compare rooms based on their name
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Room room = (Room) o;

		return name.equals(room.name);

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
