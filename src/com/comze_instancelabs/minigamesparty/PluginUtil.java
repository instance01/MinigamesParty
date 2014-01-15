package com.comze_instancelabs.minigamesparty;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PluginUtil {
	
	public static Main m;

	public static void tellInGamePlayers(String s) {
		for(String pl : m.players) {
			Player p = Bukkit.getPlayerExact(pl);
			p.sendMessage(s);
		}
	}
}
