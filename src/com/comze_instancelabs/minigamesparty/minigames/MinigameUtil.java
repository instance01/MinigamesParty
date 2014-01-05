package com.comze_instancelabs.minigamesparty.minigames;

import org.bukkit.ChatColor;

public class MinigameUtil {

	public static String nowPlaying(String name) {
		return ChatColor.GOLD + "You are now playing " + ChatColor.GREEN + name + ChatColor.GOLD + "!";
	}
}
