package com.comze_instancelabs.minigamesparty.minigames;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;

public class DisIntegration extends Minigame implements Listener{
	
	static ArrayList<Integer> ints = new ArrayList<Integer>();
	static ArrayList<Integer[]> coords = new ArrayList<Integer[]>();
	
	public DisIntegration(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("DisIntegration", MinigameUtil.getDescription(arg2, "DisIntegration"), arg2, arg3, arg4, arg5, null);
	}
	
	static Random r = new Random();
	
	public static void setup(Location start, Main main, String name_){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY();
		int y_ = start.getBlockY() - 4;
		int z = start.getBlockZ() - 32;
		
		main.saveComponentForMinigame(name_, "spawn", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "spectatorlobby", new Location(start.getWorld(), start.getBlockX(), y + 30, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "lobby", main.getLobby());
		
		int current = 0;
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				ints.add(current);
				Integer[] f = new Integer[]{i, j};
				coords.add(f);

				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				Block b_ = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y_, z + j));
				b_.setType(Material.GLOWSTONE);
				b.setType(Material.WOOL);
			}
		}
	}
	
	
	@Override
	public BukkitTask start(){		
		// setup ints arraylist
		getAll(this.spawn);
		
		final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {
			@Override
			public void run(){
				Bukkit.getServer().getScheduler().runTaskLater(m, new Runnable(){
					public void run(){
						removeNew(spawn);
					}
				}, 5L);
			}
		}, 10, 10);
		
		return id__;
	}
	
	public static void getAll(Location start){
		ints.clear();
		coords.clear();
		
		int x = start.getBlockX() - 32;
		int y = start.getBlockY() - 2;
		int z = start.getBlockZ() - 32;
		
		int current = 0;
		int count = 0;
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				int x_ = x + i;
				int z_ = z + j;
				
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x_, y, z_));

				ints.add((int)b.getData());
				Integer[] f = new Integer[]{i, j};
				coords.add(f);
			}
		}
	}
	
	@Override
	public void reset(final Location start){
		try{
			//final MassBlockUpdate mbu = CraftMassBlockUpdate.createMassBlockUpdater(m, start.getWorld());

    		//mbu.setRelightingStrategy(MassBlockUpdate.RelightingStrategy.NEVER);
    		
			if(ints.size() < 1){
				getAll(start);
			}
			
			int x = start.getBlockX() - 32;
			int y = start.getBlockY() - 2;
			int y_ = start.getBlockY() - 6;
			int z = start.getBlockZ() - 32;
			
			World w = start.getWorld();
			
			int current = 0;
			int count = 0;
			
			
			for(int i = 0; i < 64; i++){
				for(int j = 0; j < 64; j++){
					ints.add(current);
					Integer[] f = new Integer[]{i, j};
					coords.add(f);

					Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));

					//mbu.setBlock(x + i, y, z + j, 35, current);
					//mbu.setBlock(x + i, y_, z + j, 89);
					
					b.setType(Material.WOOL);
					b.setData((byte)current);
				}
			}

			//mbu.notifyClients();
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void removeNew(Location start){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY() - 2;
		int z = start.getBlockZ() - 32;
		
		for(int i = 0; i < 78 * 4; i++){
			int ind = r.nextInt(4096);
			Integer[] f_ = coords.get(ind);
			int colorcode = ints.get(ind);
			
			Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + f_[0], y, z + f_[1]));

			if(colorcode == 0){
				b.setData((byte)4);
			}else if(colorcode == 4){ // yellow
				b.setData((byte)1);
			}else if(colorcode == 1){ // orange
				b.setData((byte)14);
			}else if(colorcode == 14){ // red
				b.setType(Material.AIR);
			}else{
				b.setData((byte)0);
			}
		}
		
		getAll(start);

	}
	


}
