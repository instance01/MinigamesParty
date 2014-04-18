package com.comze_instancelabs.minigamesparty;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class NMSManager {
	
	public static void createParticles(Location l, int i, int j){
		try{
			if (Bukkit.getVersion().contains("MC: 1.6.4") || Bukkit.getVersion().contains("1.6.2")) {
				NMSFunctions164 explosion = NMSFunctions164.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else if(Bukkit.getVersion().contains("MC: 1.7.2")){
				NMSFunctions172 explosion = NMSFunctions172.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else if(Bukkit.getVersion().contains("MC: 1.7.5")){
				NMSFunctions175 explosion = NMSFunctions175.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else if(Bukkit.getVersion().contains("MC: 1.7.8")){
				NMSFunctions178 explosion = NMSFunctions178.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else{
				//fallback
				NMSFunctions172 explosion = NMSFunctions172.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}
		}catch(Exception e){
			System.out.println("Your Bukkit build appears to be invalid! Please post a comment with the following string on the project page: " + Bukkit.getVersion());
		}
	}
	
	public static ItemStack fakeGlow(ItemStack item){
		try{
			if (Bukkit.getVersion().contains("MC: 1.6.4") || Bukkit.getVersion().contains("1.6.2")) {
				return NMSFunctions164.addGlow(item);
			}else if(Bukkit.getVersion().contains("MC: 1.7.2")){
				return NMSFunctions172.addGlow(item);
			}else if(Bukkit.getVersion().contains("MC: 1.7.5")){
				return NMSFunctions175.addGlow(item);
			}else if(Bukkit.getVersion().contains("MC: 1.7.8")){
				return NMSFunctions178.addGlow(item);
			}else{
				//fallback
				return NMSFunctions172.addGlow(item);
			}
		}catch(Exception e){
			System.out.println("Your Bukkit build appears to be invalid! Please post a comment with the following string on the project page: " + Bukkit.getVersion());
			return item;
		}
	}
}
