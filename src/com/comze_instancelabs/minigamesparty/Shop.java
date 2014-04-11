package com.comze_instancelabs.minigamesparty;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Shop {

	// TODO shop

	// Shop will open an item window

	/*
	 * ideas:
	 * 
	 * Spleef: grenades
	 * 
	 * Overall Style: colored Armor
	 * 
	 * ColorMatch: jump boost
	 */

	public static int grenades_price = 0;
	public static int jumpboost_price = 0;
	public static int deadendboost_price = 0;

	public static void initShop(Main m){
		m.getConfig().addDefault("shop.grenades_price", 70);
		m.getConfig().addDefault("shop.jumpboost_price", 60);
		m.getConfig().addDefault("shop.deadendboost_price", 50);
	}
	
	public static void loadPrices(Main m){
		grenades_price = m.getConfig().getInt("shop.grenades_price");
		jumpboost_price = m.getConfig().getInt("shop.jumpboost_price");
		deadendboost_price = m.getConfig().getInt("shop.deadendboost_price");
	}
	
	public static int getPlayerShopComponent(Main m, String p, String component) {
		int amount = 0;
		if(m.getConfig().isSet("shop." + p + "." + component)){
			amount = m.getConfig().getInt("shop." + p + "." + component);
		}
		return amount;
	}

	public static void addToPlayerShopComponent(Main m, String p, String component, int value) {
		if (m.getConfig().contains("shop." + p + "." + component)) {
			m.getConfig().set("shop." + p + "." + component, m.getConfig().getInt("shop." + p + "." + component) + value);
		} else {
			m.getConfig().set("shop." + p + "." + component, value);
		}
		m.saveConfig();
	}
	
	public static void removeFromPlayerShopComponent(Main m, String p, String component, int value) {
		if (m.getConfig().contains("shop." + p + "." + component)) {
			m.getConfig().set("shop." + p + "." + component, m.getConfig().getInt("shop." + p + "." + component) - value);
		} else {
			m.getConfig().set("shop." + p + "." + component, 0);
		}
		m.saveConfig();
	}

	public static void openShop(final Main m, String p) {
		IconMenu iconm = new IconMenu("Shop - Credits: " + Integer.toString(m.getPlayerStats(p, "credits")), 18, new IconMenu.OptionClickEventHandler() {
			@Override
			public void onOptionClick(IconMenu.OptionClickEvent event) {
				String d = event.getName();
				Player p = event.getPlayer();
				int currentcredits = m.getPlayerStats(p.getName(), "credits");
				if (d.equalsIgnoreCase("grenades")) {
					if (currentcredits >= grenades_price) {
						m.updatePlayerStats(p.getName(), "credits", currentcredits - grenades_price);
						m.msql.updateShopperStats(p.getName(), grenades_price);
						addToPlayerShopComponent(m, p.getName(), "grenades", 1);
						p.sendMessage(ChatColor.GREEN + "You bought a Grenade!");
					} else {
						p.sendMessage(ChatColor.RED + "You don't have enough money!");
					}
				}else if (d.equalsIgnoreCase("jump boost")) {
					if (currentcredits >= jumpboost_price) {
						m.updatePlayerStats(p.getName(), "credits", currentcredits - jumpboost_price);
						m.msql.updateShopperStats(p.getName(), jumpboost_price);
						addToPlayerShopComponent(m, p.getName(), "jump_boost", 1);
						p.sendMessage(ChatColor.GREEN + "You bought a Jump Boost!");
					} else {
						p.sendMessage(ChatColor.RED + "You don't have enough money!");
					}
				}else if (d.equalsIgnoreCase("speed boost")) {
					if (currentcredits >= deadendboost_price) {
						m.updatePlayerStats(p.getName(), "credits", currentcredits - deadendboost_price);
						m.msql.updateShopperStats(p.getName(), deadendboost_price);
						addToPlayerShopComponent(m, p.getName(), "speed_boost", 1);
						p.sendMessage(ChatColor.GREEN + "You bought a Speed Boost!");
					} else {
						p.sendMessage(ChatColor.RED + "You don't have enough money!");
					}
				}
				m.updateScoreboardOUTGAME(p.getName());
				event.setWillClose(true);
			}
		}, m)
		.setOption(0, new ItemStack(Material.EGG, 1), "Grenades", "Use Grenades in Spleef to destroy 3x3 fields! Cost: " + Integer.toString(grenades_price))
		.setOption(1, new ItemStack(Material.POTION, 1), "Jump Boost", "Jump Boost for ColorMatch! Cost: " + Integer.toString(jumpboost_price))
		.setOption(2, new ItemStack(Material.POTION, 1), "Speed Boost", "Speed Boost for DeadEnd! Cost: " + Integer.toString(deadendboost_price));

		iconm.open(Bukkit.getPlayerExact(p));
	}
}
