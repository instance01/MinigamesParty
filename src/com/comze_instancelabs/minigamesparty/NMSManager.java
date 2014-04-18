package com.comze_instancelabs.minigamesparty;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class NMSManager {
	
	public static void createParticles(Location l, int i, int j){
		try{
			String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
			if (version.contains("1_6_R3")) {
				NMSFunctions164 explosion = NMSFunctions164.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else if(version.contains("1_7_R1")){
				NMSFunctions172 explosion = NMSFunctions172.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else if(Bukkit.getVersion().contains("1_7_R2")){
				NMSFunctions175 explosion = NMSFunctions175.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else if(Bukkit.getVersion().contains("1_7_R3")){
				NMSFunctions178 explosion = NMSFunctions178.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else{
				//fallback
				NMSFunctions172 explosion = NMSFunctions172.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}
		}catch(Exception e){
			System.out.println("Your Bukkit build appears to be unsupported! Please post a comment with the following string on the project page: " + Bukkit.getVersion());
		}
	}
	
	public static ItemStack fakeGlow(ItemStack item){
		try{
			String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
			if (version.contains("1_6_R3")) {
				return NMSFunctions164.addGlow(item);
			}else if(version.contains("1_7_R1")){
				return NMSFunctions172.addGlow(item);
			}else if(version.contains("1_7_R2")){
				return NMSFunctions175.addGlow(item);
			}else if(version.contains("1_7_R3")){
				return NMSFunctions178.addGlow(item);
			}else{
				//fallback
				return NMSFunctions172.addGlow(item);
			}
		}catch(Exception e){
			System.out.println("Your Bukkit build appears to be unsupported! Please post a comment with the following string on the project page: " + Bukkit.getVersion());
			return item;
		}
	}
}
