package com.comze_instancelabs.minigamesparty.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.Plugin;


public class MainSQL {
	
	Plugin m;
	
	public MainSQL(Plugin pl){
		this.m = pl;
	}

	public void updateWinnerStats(String p_, int reward){
		if(!m.getConfig().getBoolean("mysql.enabled")){
			return;
		}
		MySQL MySQL = new MySQL(m.getConfig().getString("mysql.host"), "3306", m.getConfig().getString("mysql.database"), m.getConfig().getString("mysql.user"), m.getConfig().getString("mysql.pw"));
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM mgparty WHERE player='" + p_ + "'");
			if(!res3.isBeforeFirst()){
				// there's no such user
				c.createStatement().executeUpdate("INSERT INTO mgparty VALUES('0', '" + p_ + "', '" + Integer.toString(reward) + "', '1')");
				return;
			}
			res3.next();
			int credits = res3.getInt("credits") + reward;
			int wins = res3.getInt("wins") + 1;
			
			c.createStatement().executeUpdate("UPDATE mgparty SET credits='" + Integer.toString(credits) + "', wins='" + Integer.toString(wins) + "' WHERE player='" + p_ + "'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public int getCredits(String p_){
		if(!m.getConfig().getBoolean("mysql.enabled")){
			return -1;
		}
		MySQL MySQL = new MySQL(m.getConfig().getString("mysql.host"), "3306", m.getConfig().getString("mysql.database"), m.getConfig().getString("mysql.user"), m.getConfig().getString("mysql.pw"));
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM mgparty WHERE player='" + p_ + "'");

			/*if(res3.next()){
				int credits = res3.getInt("credits");
				return credits;
			}
			return -1;*/
			
			res3.next();
			int credits = res3.getInt("credits");
			return credits;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("New User detected.");
		}
		return -1;
	}
	
	public int getWins(String p_){
		if(!m.getConfig().getBoolean("mysql.enabled")){
			return -1;
		}
		MySQL MySQL = new MySQL(m.getConfig().getString("mysql.host"), "3306", m.getConfig().getString("mysql.database"), m.getConfig().getString("mysql.user"), m.getConfig().getString("mysql.pw"));
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM mgparty WHERE player='" + p_ + "'");

			res3.next();
			int wins = res3.getInt("wins");
			return wins;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public void updateShopperStats(String p_, int amount){
		if(!m.getConfig().getBoolean("mysql.enabled")){
			return;
		}
		MySQL MySQL = new MySQL(m.getConfig().getString("mysql.host"), "3306", m.getConfig().getString("mysql.database"), m.getConfig().getString("mysql.user"), m.getConfig().getString("mysql.pw"));
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM mgparty WHERE player='" + p_ + "'");
			if(!res3.isBeforeFirst()){
				// there's no such user
				//c.createStatement().executeUpdate("INSERT INTO mgparty VALUES('0', '" + p_ + "', '" + Integer.toString(amount) + "', '1')");
				return;
			}
			res3.next();
			int credits = res3.getInt("credits") - amount;
			int wins = res3.getInt("wins") + 1;
			
			c.createStatement().executeUpdate("UPDATE mgparty SET credits='" + Integer.toString(credits) + "', wins='" + Integer.toString(wins) + "' WHERE player='" + p_ + "'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
