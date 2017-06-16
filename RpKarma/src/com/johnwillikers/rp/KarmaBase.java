package com.johnwillikers.rp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

public class KarmaBase {

	public static String dir = Karma.dir + "/KarmaBase/";
		
		public static void createKarmaBaseDir(){
			File pb = new File(dir);
			Core.debug(Core.name, Codes.STARTUP.toString(), "Checking Whether Karma Base Location Exists.");
			if(!pb.exists()){
				Core.debug(Core.name, Codes.FIRST_LAUNCH.toString(), "Karma Base Location Doesn't Exist. Attempting to create Location.");
				boolean status = pb.mkdirs();
				if(status){
					Core.debug(Core.name, Codes.FIRST_LAUNCH.toString(), "Karma Base Location created.");
				}else{
					Core.debug(Core.name, Codes.FIRST_LAUNCH.toString(), "Karma Base Location creation failed.");
				}
			}else{
				Core.debug(Core.name, Codes.STARTUP.toString(), "Karma Base Location Exists.");
			}
		}
		
		public static void writeKarma(String UUID, int karma, JSONArray incidents){
			if(exists(UUID)){
			new File(dir + UUID + ".json").delete();
			JSONObject kfile = new JSONObject().put("karma", karma).put("incidents", incidents);
			PrintWriter write;
			try {
				write = new PrintWriter(new File(dir + UUID + ".json"));
				write.println(kfile.toString());
				write.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}else{
				JSONObject incident = new JSONObject().put("Date", Utilities.getDate()).put("desc", "Player enrolled in karma system.")
						.put("actions", "Karma + 100").put("gm", "Server");
				JSONObject kfile = new JSONObject().put("karma", 100).put("incidents", new JSONArray().put(incident));
				PrintWriter write;
				try {
					write = new PrintWriter(new File(dir + UUID + ".json"));
					write.println(kfile.toString());
					write.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public static void updateKarma(String UUID, int karma, JSONObject incident) throws IOException{
			File user = new File(dir + UUID + ".json");
			FileReader fr = new FileReader(user);
			BufferedReader br = new BufferedReader(fr);
			JSONObject kfile = new JSONObject(br.readLine());
			br.close();
			fr.close();
			
			/*
			 * action is in charge of telling this method whether to update the Karma or the incidents. Hell it even does both.
			 * if it needs to change the karma value it adds 1 to action and if it needs to update incidents it adds 2. So it checks to see if action 
			 * is 1 for int change, 2 for incidents change, or 3 for both.
			 */
			
			int action = 0;
			if(karma > 0 || karma < 0){
				action++;
			}
			if(!(incident == null)){
				action = action + 2;
			}
			Core.debug(Karma.name, Codes.DEBUG + "KarmaBase.updateKarma", "action = " + action);
			JSONArray incidents = kfile.getJSONArray("incidents");
			int oldKarma = kfile.getInt("karma");
			
			switch(action){
			case 0: Core.debug(Karma.name, Codes.DEBUG + "KarmaBase.updateKarma", "Something called this function without needing to change anything.");
					break;
			case 1: kfile.put("karma", oldKarma + karma);
					writeKarma(UUID, kfile.getInt("karma"), kfile.getJSONArray("incidents"));
					break;
			case 2: incidents.put(incident);
					writeKarma(UUID, kfile.getInt("karma"), kfile.getJSONArray("incidents"));
					break;
			case 3: kfile.put("karma", oldKarma + karma);
					incidents.put(incident);
					writeKarma(UUID, kfile.getInt("karma"), kfile.getJSONArray("incidents"));
					break;
			}
		}
		
		public static boolean exists(String UUID){
			File user = new File(dir + UUID + ".json");
			Core.debug(Karma.name, Codes.DEBUG + "Karmabase.exists()", "user File = " + user.toString());
			if(user.exists()){
				return true;
			}else{
				return false;
			}
		}
		
		public static JSONObject getKarmaInfo(Player player){
			File user = new File(dir + player.getUniqueId().toString() + ".json");
			try{
				FileReader fr = new FileReader(user);
				BufferedReader br = new BufferedReader(fr);
				String json = br.readLine();
				br.close();
				fr.close();
				JSONObject obj = new JSONObject(json);
				obj.put("status", 1);
				return obj;
			}catch(IOException e){
				e.printStackTrace();
				JSONObject fail = new JSONObject().put("status", 0);
				return fail;
			}
		}
}
