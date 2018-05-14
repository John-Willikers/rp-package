package com.johnwillikers.rp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DbHandler {
	
	public static void createTables() {
		String query = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`players` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(200) NOT NULL , `first` VARCHAR(200) NOT NULL , `last` VARCHAR(200) NOT NULL , `player_name` VARCHAR(200) NOT NULL , `gender` INT NOT NULL , `creation_ip` VARCHAR(200) NOT NULL , `last_ip` VARCHAR(200) NOT NULL , `created_at` VARCHAR(200) NOT NULL , `updated_at` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM";
		String gamemastersTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db +"`.`gamemasters` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(200) NOT NULL , `name` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String karmaTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`karma` ( `id` INT NOT NULL AUTO_INCREMENT , `player_id` INT NOT NULL , `karma` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String reportsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`reports` ( `id` INT NOT NULL AUTO_INCREMENT , `gm_id` INT NOT NULL , `player_id` INT NOT NULL , `body` VARCHAR(200) NOT NULL , `actions` INT NOT NULL , `date` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		executeUpdate(query, Core.name);
		if(Core.dependables[1] == 1) {
			executeUpdate(gamemastersTableQuery, Core.name);
			executeUpdate(karmaTableQuery, Core.name);
			executeUpdate(reportsTableQuery, Core.name);
		}
	}
	
	public static void executeQuery(Plugin plugin, String query, String pluginName, MySqlCallback callback) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection conn = DriverManager.getConnection(Core.driver);
					Statement stmt = conn.createStatement();
					Core.debug(pluginName, "DbHandler.executeQuery", "Query String = " + query);
					ResultSet rs = stmt.executeQuery(query);
					Bukkit.getScheduler().runTask(plugin, new Runnable() {
						@Override
						public void run() {
							callback.onQueryDone(rs);
						}
					});
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					
				}
			}
			
		});
	}
	
	public static void executeUpdate(String query, String pluginName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(Core.driver);
			Statement stmt = conn.createStatement();
			Core.debug(pluginName, "DbHandler.executeUpdate", "Query String = " + query);
			stmt.executeUpdate(query);
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
	}
	
}