package com.comze_instancelabs.minigamesparty;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.comze_instancelabs.minigamesparty.nms.NMSEffectManager;

public class Shop {

	public static int grenades_price = 0;
	public static int jumpboost_price = 0;
	public static int deadendboost_price = 0;
	
	public static int chickentagboost_price = 0;
	public static int slapfight_knockback_price = 0;
	public static int smokemonsterboost_price = 0;
	public static int jumpnrunboost_price = 0;
	public static int sheepfreenzyimmunity_price = 0;
	public static int megagrenades_price = 0;

	public static void initShop(Main m){
		m.getConfig().addDefault("shop.grenades_price", 70);
		m.getConfig().addDefault("shop.jumpboost_price", 60);
		m.getConfig().addDefault("shop.deadendboost_price", 50);
		
		m.getConfig().addDefault("shop.chickentagboost_price", 60);
		m.getConfig().addDefault("shop.slapfight_knockback_price", 100);
		m.getConfig().addDefault("shop.smokemonsterboost_price", 150);
		m.getConfig().addDefault("shop.jumpnrunboost_price", 150);
		m.getConfig().addDefault("shop.sheepfreenzyimmunity_price", 100);
		m.getConfig().addDefault("shop.megagrenades_price", 130);
	}
	
	public static void loadPrices(Main m){
		grenades_price = m.getConfig().getInt("shop.grenades_price");
		jumpboost_price = m.getConfig().getInt("shop.jumpboost_price");
		deadendboost_price = m.getConfig().getInt("shop.deadendboost_price");
		
		chickentagboost_price = m.getConfig().getInt("shop.chickentagboost_price");
		slapfight_knockback_price = m.getConfig().getInt("shop.slapfight_knockback_price");
		smokemonsterboost_price = m.getConfig().getInt("shop.smokemonsterboost_price");
		jumpnrunboost_price = m.getConfig().getInt("shop.jumpnrunboost_price");
		sheepfreenzyimmunity_price = m.getConfig().getInt("shop.sheepfreenzyimmunity_price");
		megagrenades_price = m.getConfig().getInt("shop.megagrenades_price");
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
				}else if (d.equalsIgnoreCase("chickentag boost")) {
					Shop.t(m, p, chickentagboost_price, "chickentag_boost", "You bought a ChickenTag boost!");
				}else if (d.equalsIgnoreCase("knockback")) {
					Shop.t(m, p, slapfight_knockback_price, "slapfight_knockback", "You bought a better SlapFight tool!");
				}else if (d.equalsIgnoreCase("smokemonster boost")) {
					Shop.t(m, p, smokemonsterboost_price, "smokemonster_boost", "You bought a SmokeMonster boost!");
				}else if (d.equalsIgnoreCase("jumpnrun boost")) {
					Shop.t(m, p, jumpnrunboost_price, "jumpnrun_boost", "You bought a JumpnRun boost!");
				}else if (d.equalsIgnoreCase("explosion immunity")) {
					Shop.t(m, p, sheepfreenzyimmunity_price, "sheepfreenzy_explosion_immunity", "You bought a Explosion Immunity for SheepFreenzy!");
				}else if (d.equalsIgnoreCase("megagrenades")) {
					Shop.t(m, p, megagrenades_price, "megagrenades", "You bought a MegaGrenade!");
				}
				m.updateScoreboardOUTGAME(p.getName());
				event.setWillClose(true);
			}
		}, m)
		.setOption(0, new ItemStack(Material.EGG, 1), "Grenades", false, "Use Grenades in Spleef to destroy 3x3 fields!", "Cost: " + Integer.toString(grenades_price))
		.setOption(1, new ItemStack(Material.POTION, 1), "Jump Boost", false, "Jump Boost for ColorMatch!", "Cost: " + Integer.toString(jumpboost_price))
		.setOption(2, new ItemStack(Material.POTION, 1), "Speed Boost", false, "Speed Boost for DeadEnd!", "Cost: " + Integer.toString(deadendboost_price))
		.setOption(9, getCustomHead("MHF_Chicken"), "ChickenTag Boost", false, "Gives you a speed boost in ChickenTag!", "Cost: " + Integer.toString(chickentagboost_price))
		.setOption(10, new ItemStack(Material.STICK, 1), "Knockback", true, "Enchants your stick with a higher level in SlapFight!", "Cost: " + Integer.toString(slapfight_knockback_price))
		.setOption(11, new ItemStack(Material.POTION, 1), "SmokeMonster Boost", false, "Gives you a speed boost in SmokeMonster!", "Cost: " + Integer.toString(smokemonsterboost_price))
		.setOption(12, new ItemStack(Material.POTION, 1), "Jumpnrun Boost", false, "Gives you a speed boost in JumpnRun!", "Cost: " + Integer.toString(jumpnrunboost_price))
		.setOption(13, getCustomHead("MHF_Sheep"), "Explosion Immunity", false, "Saves your from one explosion in SheepFreenzy!", "Cost: " + Integer.toString(sheepfreenzyimmunity_price))
		.setOption(14, new ItemStack(Material.EGG, 1), "MegaGrenades", true, "Use MegaGrenades in Spleef to destroy 5x5 fields!", "Cost: " + Integer.toString(megagrenades_price));

		iconm.open(Bukkit.getPlayerExact(p));
	}
	
	public static ItemStack getCustomHead(String name){
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
		skullmeta.setOwner(name);
		item.setItemMeta(skullmeta);
		return item;
	}
	
	public static ItemStack enchantedItemStack(ItemStack item, String name){
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
        item.setItemMeta(im);
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
        return NMSEffectManager.fakeGlow(item);
	}
	
	
	public static void t(Main m, Player p, int price, String comp, String message){
		int currentcredits = m.getPlayerStats(p.getName(), "credits");
		if (currentcredits >= price) {
			m.updatePlayerStats(p.getName(), "credits", currentcredits - price);
			m.msql.updateShopperStats(p.getName(), price);
			addToPlayerShopComponent(m, p.getName(), comp, 1);
			p.sendMessage(ChatColor.GREEN + message);
		} else {
			p.sendMessage(ChatColor.RED + "You don't have enough money!");
		}
	}
	
}
