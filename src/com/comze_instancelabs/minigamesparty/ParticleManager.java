package com.comze_instancelabs.minigamesparty;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ParticleManager {
	
	public static void runManager(Location l, int i, int j){
		if (Bukkit.getVersion().contains("MC: 1.6.4") || Bukkit.getVersion().contains("1.6.2")) {
			ParticleEffect164 explosion = ParticleEffect164.HUGE_EXPLOSION;
			explosion.animateAtLocation(l, i, j);
		}else if(Bukkit.getVersion().contains("MC: 1.7.2")){
			ParticleEffect172 explosion = ParticleEffect172.HUGE_EXPLOSION;
			explosion.animateAtLocation(l, i, j);
		}else if(Bukkit.getVersion().contains("MC: 1.7.5")){
			ParticleEffect175 explosion = ParticleEffect175.HUGE_EXPLOSION;
			explosion.animateAtLocation(l, i, j);
		}else{
			//fallback
			ParticleEffect172 explosion = ParticleEffect172.HUGE_EXPLOSION;
			explosion.animateAtLocation(l, i, j);
		}
	}
}
