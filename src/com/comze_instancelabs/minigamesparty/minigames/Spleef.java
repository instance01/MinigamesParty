package com.comze_instancelabs.minigamesparty.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;
import com.comze_instancelabs.minigamesparty.Shop;

public class Spleef extends Minigame implements Listener{
	
	public Spleef(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("Spleef", MinigameUtil.getDescription(arg2, "Spleef"), arg2, arg3, arg4, arg5, null);
	}
	
	@Override
	public void join(final Player p){
		super.join(p);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.getInventory().clear();
				p.updateInventory();
				p.getInventory().addItem(new ItemStack(Material.DIAMOND_SPADE, 1));
				p.updateInventory();
				
				int temp = Shop.getPlayerShopComponent(m, p.getName(), "grenades");
				if(temp > 0){
					p.getInventory().addItem(new ItemStack(Material.EGG, temp));
					p.updateInventory();
				}
				
				int temp_ = Shop.getPlayerShopComponent(m, p.getName(), "megagrenades");
				if(temp_ > 0){
					p.getInventory().addItem(Shop.enchantedItemStack(new ItemStack(Material.EGG, temp_), "MegaGrenade"));
					p.updateInventory();
				}
			}
		}, 5);
	}
	
	public static void setup(Location start, Main main, String name_){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY();
		int z = start.getBlockZ() - 32;
		
		main.saveComponentForMinigame(name_, "spawn", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "spectatorlobby", new Location(start.getWorld(), start.getBlockX(), y + 30, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "lobby", main.getLobby());
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				Block b_ = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y - 4, z + j));
				b_.setType(Material.GLOWSTONE);
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				b.setType(Material.SNOW_BLOCK);
			}
		}
	}
	
	@Override
	public void reset(Location start){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY() - 2;
		int z = start.getBlockZ() - 32;
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				b.setType(Material.SNOW_BLOCK);
			}
		}
	}
}
