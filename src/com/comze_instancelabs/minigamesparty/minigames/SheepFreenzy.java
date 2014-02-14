package com.comze_instancelabs.minigamesparty.minigames;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;

public class SheepFreenzy extends Minigame implements Listener{

	public SheepFreenzy(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("SheepFreenzy", "Shear as many Sheeps as possible! Attention: Some of them explode.", arg2, arg3, arg4, arg5, null);
	}
	
	
	
	@Override
	public BukkitTask start(){		

		final Random r = new Random();
		
		final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimer(m, new Runnable() {
			@Override
			public void run(){
				int x = r.nextInt(60) - 30;
				int z = r.nextInt(60) - 30;
				
				Location s = new Location(spawn.getWorld(), spawn.getBlockX() + x, spawn.getBlockY() + 2, spawn.getBlockZ() + z);
				spawn.getWorld().spawn(s, Sheep.class);
				spawn.getWorld().spawnEntity(s, EntityType.SHEEP);
			}
		}, 20, 40); // 7 seconds
		
		return id__;
	}
	
	@Override
	public void join(final Player p){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.teleport(spawn);
				p.setGameMode(GameMode.SURVIVAL);
				p.setAllowFlight(false);
				p.setFlying(false);
				p.sendMessage(MinigameUtil.nowPlaying(name));
				
				p.getInventory().clear();
				p.updateInventory();
				p.getInventory().addItem(new ItemStack(Material.SHEARS, 1));
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
				b.setType(Material.WOOL);
			}
		}
	}

}
