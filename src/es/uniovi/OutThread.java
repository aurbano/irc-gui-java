package es.uniovi;

import es.uniovi.popups.ChangeNick;

/**
 * Outgoing thread
 */
class OutThread extends Thread{
	public OutThread(){
		this.start();
	}
	
	public void run(){
		Response resp;
		String[] aux;
		while(!ChatClient.quit){
			try{
				resp = ChatClient.netIn.remove();
				if(resp.status==3){
					error(resp);
					continue;
				}
				switch(resp.type){
				/*
				 * Custom logic per command
				 */
					case "MSG":
						if(resp.params.length > 2){
							printMessage(resp.params[1],resp.params[0],resp.params[2]);
						}
						break;
					case "JOIN":
						if(resp.status == 2){
							// New room
							ChatClient.joinRoom(resp.params[1]);
						}else{
							ChatClient.addUser(resp.params[1], resp.params[0]);
						}
						ChatClient.println(resp.params[1], "&raquo; <i style=\"color:green\">" + resp.params[0] + "</i> joined room <i style=\"color:orange\">" + resp.params[1] + "</i>");
						break;
					case "LEAVE":
						ChatClient.println(resp.params[1], "&raquo; <i style=\"color:green\">" + resp.params[0] + "</i> left room <i style=\"color:orange\">" + resp.params[1] + "</i>");
						if(resp.status == 2){
							// Leaving room
							ChatClient.leaveRoom(resp.params[1]);
						}
						ChatClient.removeUser(resp.params[1], resp.params[0]);
						break;
					case "NICK":
						ChatClient.println("&raquo; <i style=\"color:blue\">" + resp.params[0] + "</i> changed his nick to <i style=\"color:green\">" + resp.params[1] + "</i>");
						if(resp.status == 2){
							// Nick changes
							ChatClient.nick = resp.params[1];
						}
						// Update the username
						ChatClient.replaceUser(resp.params[0], resp.params[1]);
						break;
					case "QUIT":
						ChatClient.println("&raquo; <span style=\"color:green\">" + resp.params[0] + "</span> disconnected.");
						ChatClient.s.close();
						ChatClient.quit = true;
						if(resp.status == 1){
							// Someone sent a /QUIT
							ChatClient.removeUserAll(resp.params[0]);
						}else{
							// The current user /QUIT, exiting app
							ChatClient.finish();
						}
						break;
					case "LIST":
						aux = splitParams(resp.params[0]);
						String print = "&raquo; Rooms: ";
						if(aux.length>0){
							for(int i=0; i<aux.length; i++){
								print += aux[i];
								if(i<aux.length - 1) print += ", ";
							}
							ChatClient.listRooms(aux);
						}else{
							print += "There are no rooms";
						}
						ChatClient.println(ChatClient.room, print);
						break;
					case "WHO":
						aux = splitParams(resp.params[1]);
						String printMsg = "&raquo; Users in <span style=\"color:orange\">"+resp.params[0]+"</span>: ";
						if(aux.length>0){
							for(int i=0; i<aux.length; i++){
								printMsg += aux[i];
								if(i<aux.length - 1) printMsg += ", ";
							}
							ChatClient.addUsers(resp.params[0], aux);
						}else{
							printMsg += "There are no rooms";
						}
						ChatClient.println(resp.params[0], printMsg);
						break;
					case "HELLO":
						ChatClient.println("&raquo; " + resp.params[0]);
						// Send a NICK
						ChatClient.netOut.send(new Command("/NICK "+ ChatClient.nick));
						ChatClient.netOut.send(new Command("/LIST"));
						break;
					case "OTHERS":
						error(resp);
						break;
					default:
						ChatClient.println("&raquo; <span style=\"color:red\">Unknown command (" + resp.type + ")</span> Status=" + resp.status + ". Message: " + resp.params[0]);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Display errors in a nicer way
	 * @param resp Error response
	 */
	private void error(Response resp){
		if(resp.type.equals("NICK")){
			// The nick was taken
			new ChangeNick(resp.params[0]);
			return;
		}
		ChatClient.println("<b style=\"color:red\">&raquo;</b> <b style=\"color:white; background:red\">Error: " + resp.params[0] + "</b>");
	}
	
	/**
	 * Split input parameters with ;
	 * @param parameters Parameter list
	 * @return Array with all parameters
	 */
	private String[] splitParams(String parameters){
		return parameters.split(";");
	}
	
	/**
	 * Prettify and format messages before displaying, this includes:
	 * 	- Substitute emoji references with images
	 * 	- Highlight the current username
	 * 	- If the room is not the current one, it sets the tab to orange.
	 * @param room Room name
	 * @param nick User name
	 * @param msg Message
	 */
	private void printMessage(String room, String nick, String msg){
		if(nick.equals(ChatClient.nick)){
			nick = "<span style=\"background:green;color:white\"> "+nick+" </span>";
		}else{
			nick = "<span style=\"color:green\"> "+nick+" </span>";
		}
		// Emoji
		msg = msg.replaceAll(":(smile|sad|wink|wow|surprise|meh|what|love|hmm):", "<img src=\"file:img/icons/$1.png\" height=\"22\" width=\"22\" style=\"display:inline-block; vertical-align:middle;\" />");

		// Mentions
		msg = msg.replace(ChatClient.nick,"<strong style=\"color:cyan\">"+ ChatClient.nick+"</strong>");
		if(!nick.equals(ChatClient.nick)){
			// A message from another user
			ChatClient.printMsg(room, "<span style=\"color:orange\">" + room + "</span> |" + nick + "&gt; " + msg);
		}else{
			// A message from the current user
			ChatClient.println(room, "<span style=\"color:orange\">" + room + "</span> |" + nick + "&gt; " + msg);
		}
	}
}
