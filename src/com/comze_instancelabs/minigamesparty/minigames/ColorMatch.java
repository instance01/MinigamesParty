package com.comze_instancelabs.minigamesparty.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;
import com.comze_instancelabs.minigamesparty.Shop;
import com.comze_instancelabs.minigamesparty.nms.CraftMassBlockUpdate;
import com.comze_instancelabs.minigamesparty.nms.MassBlockUpdate;

public class ColorMatch extends Minigame implements Listener{
	
	static ArrayList<Integer> ints = new ArrayList<Integer>();
	
	public ColorMatch(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("ColorMatch", MinigameUtil.getDescription(arg2, "ColorMatch"), arg2, arg3, arg4, arg5, null);
	}
	
	static ArrayList<DyeColor> colors = new ArrayList<DyeColor>(Arrays.asList(DyeColor.BLUE, DyeColor.RED, DyeColor.CYAN, DyeColor.BLACK, DyeColor.GREEN, DyeColor.YELLOW, DyeColor.ORANGE, DyeColor.PURPLE, DyeColor.LIME));
	static Random r = new Random();
	
	
	@Override
	public void join(final Player p){
		super.join(p);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.getInventory().clear();
				p.updateInventory();
				
				int temp = Shop.getPlayerShopComponent(m, p.getName(), "jump_boost");
				if(temp > 0){
					p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 60, 2));
					Shop.removeFromPlayerShopComponent(m, p.getName(), "jump_boost", 1);
				}
			}
		}, 5);
	}
	
	
	
	public static void setup(Location start, Main main, String name_){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY();
		int y_ = start.getBlockY() - 4;
		int z = start.getBlockZ() - 32;
		
		main.saveComponentForMinigame(name_, "spawn", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "spectatorlobby", new Location(start.getWorld(), start.getBlockX(), y + 30, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "lobby", main.getLobby());
		
		int current = 0;
		
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
				int x_ = x + i * 4;
				int z_ = z + j * 4;
				
				int newcurrent = r.nextInt(colors.size());
				if(current == newcurrent){
					if(newcurrent > 0){
						newcurrent -= 1;
					}else{
						newcurrent += 2;
					}
				}
				
				if(ints.size() > 15){
					if(ints.get(ints.size() - 16) == colors.get(newcurrent).getData()){
						if(newcurrent > 0){
							newcurrent -= 1;
						}else{
							newcurrent += 2;
						}
					}
				}
				current = newcurrent;
				
				ints.add((int) colors.get(current).getData());
				
				for(int i_ = 0; i_ < 4; i_++){
					for(int j_ = 0; j_ < 4; j_++){
						Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x_ + i_, y, z_ + j_));
						Block b_ = start.getWorld().getBlockAt(new Location(start.getWorld(), x_ + i_, y_, z_ + j_));
						b_.setType(Material.GLOWSTONE);
						b.setType(Material.WOOL);
						b.setData(colors.get(current).getData());
					}
				}
			}
		}
	}
	
	public static HashMap<Player, Integer> xpsecp = new HashMap<Player, Integer>();
	public static ArrayList<BukkitTask> tasks = new ArrayList<BukkitTask>(); // arena -> task/ task

	long n = 0;
	int currentw = 0;
	@Override
	public BukkitTask start(){		
		// setup ints arraylist
		getAll(this.spawn);
		
		final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {
			@Override
			public void run(){
				
				int temp = r.nextInt(colors.size());
				if (currentw == temp) {
					currentw = r.nextInt(colors.size());
				} else {
					currentw = temp;
				}

				for(String pl : m.players){
					final Player p = Bukkit.getPlayerExact(pl);
					// set inventory and exp bar
					p.getInventory().clear();
					p.updateInventory();
					Wool w = new Wool();
					w.setColor(colors.get(currentw));
					
					p.setExp(0.97F);
					if (!xpsecp.containsKey(p)) {
						xpsecp.put(p, 1);
					}
					tasks.add(Bukkit.getServer().getScheduler().runTaskTimer(m, new Runnable() {
						public void run() {
							if (!xpsecp.containsKey(p)) {
								xpsecp.put(p, 1);
							}
							int xpsec = xpsecp.get(p);
							p.setExp(1 - (0.083F * xpsec));
							xpsecp.put(p, xpsec + 1);
						}
					}, (40L - n) / 12, (40L - n) / 12));

					
					ItemStack wool = new ItemStack(Material.WOOL, 1, colors.get(currentw).getData());
					//p.getInventory().all(wool);
					for(int i = 0; i<9; i++){
						p.getInventory().setItem(i, wool);
					}
					p.updateInventory();
				}
				// remove all wools except current one
				Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable(){
				//Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable(){
					public void run(){
						Bukkit.getScheduler().runTask(m, new Runnable(){
							public void run(){
								removeAllExceptOne(spawn, currentw);
								for (BukkitTask t : tasks) {
									t.cancel();
								}
								for (Player p : xpsecp.keySet()) {
									xpsecp.put(p, 1);
								}
							}
						});
					}
				}, 40L - n);
				
				
				//BukkitTask id = Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable() {
				BukkitTask id = Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable() {
					@Override
					public void run(){
						Bukkit.getScheduler().runTask(m, new Runnable(){
							public void run(){
								reset(spawn);
							}
						});
					}
				}, 120);
				// update count
				n-=2;
			}
		}, 20, 140-n); // 7 seconds
		
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
	

	@Override
	public void reset(final Location start){
		try{
			final MassBlockUpdate mbu = CraftMassBlockUpdate.createMassBlockUpdater(m, start.getWorld());
   		 
    		mbu.setRelightingStrategy(MassBlockUpdate.RelightingStrategy.NEVER);
    		
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
			
			for(int i = 0; i < 16; i++){
				for(int j = 0; j < 16; j++){
					int x_ = x + i * 4;
					int z_ = z + j * 4;
					
					//current = r.nextInt(colors.size());
					current = ints.get(count);
					if (current < 1) {
						current = (int) colors.get(r.nextInt(colors.size())).getData();
					}
					count += 1;
					
					for(int i_ = 0; i_ < 4; i_++){
						for(int j_ = 0; j_ < 4; j_++){
							Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x_ + i_, y, z_ + j_));
							try{
								mbu.setBlock(x_ + i_, y, z_ + j_, 35, current);
							}catch(Exception e){
								b.setType(Material.WOOL);
								b.setData((byte)current);
							}
						}
					}
				}
			}
			
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
					try{
						mbu.setBlock(x + i, y, z + j, 0);
					}catch(Exception e){
						b.setType(Material.AIR);
					}
				}
			}
		}
		
		mbu.notifyClients();
	}
	


}
