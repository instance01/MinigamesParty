package com.comze_instancelabs.minigamesparty.nms;

import org.bukkit.Bukkit;

public class NMSClass {

	public static Class getClazz(String t){
		 String version = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

		 Class c = null;
		 try {
		     c = Class.forName(version + t);
		 }catch(Exception e) {
			 e.printStackTrace();
			 return null;
		 }
		 
		 return c;
	}
	
}
