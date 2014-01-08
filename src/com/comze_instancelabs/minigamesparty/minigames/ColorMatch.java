package com.comze_instancelabs.minigamesparty.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;
import com.comze_instancelabs.minigamesparty.nms.CraftMassBlockUpdate;
import com.comze_instancelabs.minigamesparty.nms.MassBlockUpdate;

public class ColorMatch extends Minigame implements Listener{
	
	static ArrayList<Integer> ints = new ArrayList<Integer>();
	
	public ColorMatch(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("ColorMatch", arg2, arg3, arg4, arg5, null);
	}
	
	static ArrayList<DyeColor> colors = new ArrayList<DyeColor>(Arrays.asList(DyeColor.BLUE, DyeColor.RED, DyeColor.CYAN, DyeColor.BLACK, DyeColor.GREEN, DyeColor.YELLOW, DyeColor.ORANGE));
	static Random r = new Random();
	
	public static void setup(Location start, Main main, String name_){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY();
		int z = start.getBlockZ() - 32;
		
		main.saveComponentForMinigame(name_, "spawn", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "spectatorlobby", new Location(start.getWorld(), start.getBlockX(), y + 30, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "lobby", main.getLobby());
		
		int current = 0;
		int temp = 4;
		boolean cont = false;
		
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
				int x_ = x + i * 4;
				int z_ = z + j * 4;
				
				current = r.nextInt(colors.size());
				ints.add(current);
				
				for(int i_ = 0; i_ < 4; i_++){
					for(int j_ = 0; j_ < 4; j_++){
						Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x_ + i_, y, z_ + j_));
						b.setType(Material.WOOL);
						b.setData(colors.get(current).getData());
					}
				}
			}
		}
	}
	
	
	//TODO CHANGE SYNC TO ASYNC
	long n = 0;
	int currentw = 0;
	@Override
	public BukkitTask start(){		
		// setup ints arraylist
		getAll(this.spawn);
		
		final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {
			@Override
			public void run(){
				
				currentw = r.nextInt(colors.size());
				for(String pl : m.players){
					Player p = Bukkit.getPlayerExact(pl);
					// set inventory and exp bar
					p.getInventory().clear();
					p.updateInventory();
					Wool w = new Wool();
					w.setColor(colors.get(currentw));
					
					ItemStack wool = new ItemStack(Material.WOOL, 1, colors.get(currentw).getData());
					//p.getInventory().all(wool);
					for(int i = 0; i<9; i++){
						p.getInventory().setItem(i, wool);
					}
					p.updateInventory();
				}
				// remove all wools except current one
				Bukkit.getServer().getScheduler().runTaskLater(m, new Runnable(){
				//Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable(){
					public void run(){
						removeAllExceptOne(spawn, currentw);
					}
				}, 40L - n);
				
				
				//BukkitTask id = Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable() {
				BukkitTask id = Bukkit.getServer().getScheduler().runTaskLater(m, new Runnable() {
					@Override
					public void run(){
						reset(spawn);
					}
				}, 120);
				// update count
				n-=1;
			}
		}, 20, 140); // 7 seconds
		
		return id__;
	}
	
	public static void getAll(Location start){
		ints.clear();
		
		int x = start.getBlockX() - 32;
		int y = start.getBlockY() - 2;
		int z = start.getBlockZ() - 32;
		
		int current = 0;
		int count = 0;
		
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
				int x_ = x + i * 4;
				int z_ = z + j * 4;
				
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x_, y, z_));

				ints.add((int)b.getData());
			}
		}
	}
	
	public static void reset(final Location start){
		try{
			final MassBlockUpdate mbu = CraftMassBlockUpdate.createMassBlockUpdater(m, start.getWorld());
   		 
    		mbu.setRelightingStrategy(MassBlockUpdate.RelightingStrategy.NEVER);
    		
			if(ints.size() < 1){
				getAll(start);
			}
			
			int x = start.getBlockX() - 32;
			int y = start.getBlockY() - 2;
			int z = start.getBlockZ() - 32;
			
			World w = start.getWorld();
			
			int current = 0;
			int count = 0;
			
			for(int i = 0; i < 16; i++){
				for(int j = 0; j < 16; j++){
					int x_ = x + i * 4;
					int z_ = z + j * 4;
					
					//current = r.nextInt(colors.size());
					current = ints.get(count);
					count += 1;
					
					for(int i_ = 0; i_ < 4; i_++){
						for(int j_ = 0; j_ < 4; j_++){
							//Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x_ + i_, y, z_ + j_));

							mbu.setBlock(x_ + i_, y, z_ + j_, 35, current);

							//b.setType(Material.WOOL);
							//b.setData((byte)current);
						}
					}
				}
			}
			
			//sendClientChanges(start, test);
			mbu.notifyClients();
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void resetNew(final Location start){
		Bukkit.getScheduler().runTask(m, new Runnable(){
			public void run(){
				int x = start.getBlockX() - 32;
				int y = start.getBlockY() - 2;
				int z = start.getBlockZ() - 32;

				int current = 0;
				
				for(int i = 0; i < 16; i++){
					for(int j = 0; j < 16; j++){
						int x_ = x + i * 4;
						int z_ = z + j * 4;
						
						current = r.nextInt(colors.size());

						for(int i_ = 0; i_ < 4; i_++){
							for(int j_ = 0; j_ < 4; j_++){
								Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x_ + i_, y, z_ + j_));
								b.setType(Material.WOOL);
								b.setData(colors.get(current).getData());
							}
						}
					}
				}	
			}
		});
		
		
	}
	
	
	public void removeAllExceptOne(Location start, int exception){
		final MassBlockUpdate mbu = CraftMassBlockUpdate.createMassBlockUpdater(m, start.getWorld());
  		 
		mbu.setRelightingStrategy(MassBlockUpdate.RelightingStrategy.NEVER);
		
		int x = start.getBlockX() - 32;
		int y = start.getBlockY() - 2;
		int z = start.getBlockZ() - 32;
		Byte data = colors.get(currentw).getData();
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				if(b.getData() != data){
					//b.setType(Material.AIR);
					mbu.setBlock(x + i, y, z + j, 0);
				}
			}
		}
		
		mbu.notifyClients();
	}
	


}
