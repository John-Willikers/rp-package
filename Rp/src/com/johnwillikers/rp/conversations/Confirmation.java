package com.johnwillikers.rp.conversations;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.PlayerBaseMySql;
import com.johnwillikers.rp.Utilities;
import com.johnwillikers.rp.enums.Codes;

public class Confirmation extends StringPrompt{

	@Override
	public Prompt acceptInput(ConversationContext con, String s) {
		Conversable c = con.getForWhom();
		Player player = (Player) c;
		String ip = player.getAddress().toString();
		ip.substring(1);
		ip.substring(0, ip.length()-6);
		//Putting the players data in a handy dandy array
		String[] pData = {player.getUniqueId().toString(), con.getSessionData("first").toString(), con.getSessionData("last").toString(), 
				player.getDisplayName(), con.getSessionData("gender").toString(), ip, ip, Utilities.getDate(), Utilities.getDate()};
		
		if(s.equalsIgnoreCase("yes")){
			//TODO Check to use Json or Mysql Data Methods
			if(Core.dataMethod.equalsIgnoreCase("mysql")) {
				Core.log(Core.name, Codes.PLAYERBASE.toString(), "Attempting to Create new Player " + player.getDisplayName() + " as UUID " + player.getUniqueId().toString() + " within PlayerBase.");
				PlayerBaseMySql.createPlayer(pData);
				Core.log(Core.name, Codes.PLAYERBASE.toString(), "Player " + player.getDisplayName() + " has been created in the database.");
			}else {
				try {
					Core.log(Core.name, Codes.PLAYERBASE.toString(), "Attempting to Create new Player " + player.getDisplayName() + " as UUID " + player.getUniqueId().toString() + " within PlayerBase.");
					PlayerBase.writePlayer(player.getUniqueId().toString(), con.getSessionData("first").toString(), con.getSessionData("last").toString(), player.getDisplayName(), con.getSessionData("gender").toString(), player.getAddress().toString(), player.getAddress().toString());
					Core.log(Core.name, Codes.PLAYERBASE.toString(), "Player " + player.getDisplayName() + " within " + player.getUniqueId().toString() + ".json");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Core.log(Core.name, Codes.PLAYERNAME.toString(), "Attempting to name " + player.getDisplayName() + " as " + con.getSessionData("first").toString() + " " + con.getSessionData("last").toString());
			Bukkit.getPlayer(player.getUniqueId()).setDisplayName(con.getSessionData("first").toString() + " " + con.getSessionData("last"));
			Core.log(Core.name, Codes.PLAYERNAME.toString(), "Welcome " + player.getDisplayName());
			return new WelcomePrompt();
		}else{
			return new EntryPrompt();
		}
	}

	@Override
	public String getPromptText(ConversationContext con) {
		return "\n\n\n\n\n\n\n\n\n\nAre you sure your name is " + con.getSessionData("first") + " " + con.getSessionData("last") + "?";
	}

}
