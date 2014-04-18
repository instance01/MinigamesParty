package com.comze_instancelabs.minigamesparty.minigames;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;

public class LastArcherStanding extends Minigame implements Listener{

	public LastArcherStanding(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("LastArcherStanding", MinigameUtil.getDescription(arg2, "LastArcherStanding"), arg2, arg3, arg4, arg5, null);
	}

	
	@Override
	public void join(final Player p){
		super.join(p);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.getInventory().clear();
				p.updateInventory();
				p.getInventory().addItem(new ItemStack(Material.BOW, 1));
				p.getInventory().addItem(new ItemStack(Material.ARROW, 32));
				p.updateInventory();
			}
		}, 5);
	}
	
	public static void setup(Location start, Main main, String name_){
		Random r = new Random();
		int x = start.getBlockX() - 32;
		int y = start.getBlockY();
		int z = start.getBlockZ() - 32;
		
		main.saveComponentForMinigame(name_, "spawn", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "spectatorlobby", new Location(start.getWorld(), start.getBlockX(), y + 30, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "lobby", main.getLobby());
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				boolean c = r.nextBoolean();
				if(c){
					b.setType(Material.COBBLESTONE);
				}else{
					b.setType(Material.GRASS);
				}
			}
		}
	}

}
