package com.comze_instancelabs.minigamesparty.minigames;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;
import com.comze_instancelabs.minigamesparty.PluginUtil;

public class MineField extends Minigame implements Listener{
	
	public MineField(Main arg2, Location arg3, Location arg4, Location arg5, Location arg6) {
		super("MineField", MinigameUtil.getDescription(arg2, "MineField"), arg2, arg3, arg4, arg5, arg6);
	}
	
	@Override
	public void join(final Player p){
		super.join(p);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.getInventory().clear();
				p.updateInventory();
			}
		}, 5);
	}
	
	public static void setup(Location start, Main main, String name_){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY();
		int z = start.getBlockZ() - 32;
		
		main.saveComponentForMinigame(name_, "spawn", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ() - 30));
		main.saveComponentForMinigame(name_, "finishline", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ() + 30));
		main.saveComponentForMinigame(name_, "spectatorlobby", new Location(start.getWorld(), start.getBlockX(), y + 30, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "lobby", main.getLobby());
		
		Random r = new Random();
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				b.setType(Material.GRASS);
			}
		}
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 56; j++){
				Block b_ = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y + 1, z + j + 4));
				if(r.nextBoolean()){
					b_.setType(Material.STONE_PLATE);
				}else{
					b_.setType(Material.AIR);
				}
			}
		}
	}
	
	@Override
	public void reset(Location start){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY() - 2;
		int z = start.getBlockZ() - 32;
		
		Random r = new Random();
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 56; j++){
				Block b_ = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y + 1, z + j + 4));
				if(r.nextBoolean()){
					b_.setType(Material.STONE_PLATE);
				}else{
					b_.setType(Material.AIR);
				}
			}
		}
	}
	
	@Override
	public void getWinner(){
		HashMap<String, Integer> currentscore_ = m.currentscore;
		Map<String, Integer> currentscore__ = PluginUtil.sortByValue(currentscore_);

		Map.Entry<String, Integer> max = null;
		
		for (Map.Entry<String, Integer> entry : currentscore__.entrySet())
		{
			if(!lost.contains(Bukkit.getPlayerExact(entry.getKey()))){
				if (max == null || entry.getValue().compareTo(max.getValue()) > 0)
			    {
			    	max = entry;
			    }	
			}
		}
		
		if(max != null){
			m.win(Bukkit.getPlayerExact(max.getKey()));
		}
	}
}
