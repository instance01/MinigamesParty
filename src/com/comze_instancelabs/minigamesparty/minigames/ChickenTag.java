package com.comze_instancelabs.minigamesparty.minigames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;
import com.comze_instancelabs.minigamesparty.Shop;

public class ChickenTag extends Minigame implements Listener{
	
	static ArrayList<Integer> ints = new ArrayList<Integer>();
	
	public ChickenTag(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("ChickenTag", MinigameUtil.getDescription(arg2, "ChickenTag"), arg2, arg3, arg4, arg5, null);
	}
	
	static int[][] chicken_word = new int[][]{
	  { 1, 1, 1, 1, 0, 1, 0, 0, 1, 0,1, 0, 1, 1, 1, 1, 0, 1, 0, 0,1, 0, 1, 1, 1, 1, 0, 1, 1, 0,0, 1 },
	  { 1, 0, 0, 0, 0, 1, 0, 0, 1, 0,1, 0, 1, 0, 0, 0, 0, 1, 0, 1,0, 0, 1, 0, 0, 0, 0, 1, 0, 1,0, 1 },
	  { 1, 0, 0, 0, 0, 1, 1, 1, 1, 0,1, 0, 1, 0, 0, 0, 0, 1, 1, 0,0, 0, 1, 1, 1, 0, 0, 1, 0, 0,1, 1 },
	  { 1, 0, 0, 0, 0, 1, 0, 0, 1, 0,1, 0, 1, 0, 0, 0, 0, 1, 0, 1,0, 0, 1, 0, 0, 0, 0, 1, 0, 0,0, 1 },
	  { 1, 0, 0, 0, 0, 1, 0, 0, 1, 0,1, 0, 1, 0, 0, 0, 0, 1, 0, 0,1, 0, 1, 0, 0, 0, 0, 1, 0, 0,0, 1 },
	  { 1, 1, 1, 1, 0, 1, 0, 0, 1, 0,1, 0, 1, 1, 1, 1, 0, 1, 0, 0,1, 0, 1, 1, 1, 1, 0, 1, 0, 0,0, 1 }
	};
	
	public static void setup(Location start, Main main, String name_){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY();
		int y_ = start.getBlockY() - 4;
		int z = start.getBlockZ() - 32;
		
		main.saveComponentForMinigame(name_, "spawn", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "spectatorlobby", new Location(start.getWorld(), start.getBlockX(), y + 30, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "lobby", main.getLobby());
		
		int current = 0;
		
		// platform
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				Block b_ = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y_, z + j));
				b_.setType(Material.GLOWSTONE);
				b.setType(Material.COAL_BLOCK);
			}
		}
		
		// chicken word
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 32; j++){
				if(chicken_word[i][j] == 1){
					Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + 47 - j, y, z - i + 9));
					b.setType(Material.WOOL);
				}
			}
		}
		
		// chicken head
		// TODO
	}
	
	public static HashMap<Player, Integer> xpsecp = new HashMap<Player, Integer>();
	public static ArrayList<BukkitTask> tasks = new ArrayList<BukkitTask>(); // arena -> task/ task

	long n = 0;
	@Override
	public BukkitTask start(){		
		final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {
			@Override
			public void run(){
				ArrayList<Boolean> playerHasChicken = new ArrayList<Boolean>();
				for (int ip = 0; ip <= m.players.size() - 1; ip++) {
					if (ip <= m.players.size() / 2 - 1)
						playerHasChicken.add(true);
					else
						playerHasChicken.add(false);
				}
				Collections.shuffle(playerHasChicken);
				int ip = 0;
				for(String pl : m.players){
					final Player p = Bukkit.getPlayerExact(pl);
					// approx. half of the players get a chicken
					if (playerHasChicken.get(ip) == true) {
						m.hasChicken.put(pl, true);
						Bukkit.getScheduler().runTask(m, new Runnable(){
							public void run(){
								final Chicken c = (Chicken) p.getWorld().spawnEntity(p.getLocation(), EntityType.CHICKEN);
								Bukkit.getScheduler().runTaskLater(m, new Runnable() {
									public void run() {
										p.setPassenger(c);
									}
								}, 2L);
							}
						});
						p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You got a Chicken! Pass it to someone else!");
					}
					ip++;//for random user

					// set inventory and exp bar
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
					}, (100L - n) / 12, (100L - n) / 12));
				}
				Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable(){
					public void run(){
						Bukkit.getScheduler().runTask(m, new Runnable(){
							public void run(){
								for (BukkitTask t : tasks) {
									t.cancel();
								}
								for (Player p : xpsecp.keySet()) {
									xpsecp.put(p, 1);
								}
							}
						});
					}
				}, 120L - n);
				
				
				//BukkitTask id = Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable() {
				BukkitTask id = Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable() {
					@Override
					public void run(){
						Bukkit.getScheduler().runTask(m, new Runnable(){
							public void run(){
								for(String pl : m.players){
									Player p = Bukkit.getPlayerExact(pl);
									if(!m.hasChicken.containsKey(pl)){
										m.hasChicken.put(pl, false);
									}
									if(m.hasChicken.get(pl)){
										lost.add(p);
										int count = 0;
										for(String pl_ : m.players){
											Player p_ = Bukkit.getPlayerExact(pl_);
											if(p_.isOnline()){
												if(!lost.contains(p_)){
													count++;
												}
											}
										}
										m.sendPlace(count, p);
										spectate(p);
										// there's only one man standing
										if(count < 2){
											m.c_ += m.seconds-m.c;
											m.c = m.seconds; // just skips all the remaining seconds and sets to 60, current timer will do the rest
										}
									}
								}
							}
						});
					}
				}, 120);
				// update count
				n-=1;
			}
		}, 20, 140); // 7 seconds
		
		return id__;
	}
	
	
	@Override
	public void leave(Player p){
		super.leave(p);
		if(p.getPassenger() != null){
			Entity t = p.getPassenger();
			p.eject();
			t.remove();
		}
	}
	
	@Override
	public void join(final Player p){
		super.join(p);
		Bukkit.getScheduler().runTaskLater(m, new Runnable(){
			public void run(){
				int temp = Shop.getPlayerShopComponent(m, p.getName(), "chickentag_boost");
				if(temp > 0){
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 2));
					Shop.removeFromPlayerShopComponent(m, p.getName(), "chickentag_boost", 1);
				}
			}
		}, 5L);
	}
	
	@Override
	public void reset(Location l){
		Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(m, new Runnable(){
			public void run(){
				Bukkit.getScheduler().runTask(m, new Runnable(){
					public void run(){
						for (BukkitTask t : tasks) {
							t.cancel();
						}
						for (Player p : xpsecp.keySet()) {
							xpsecp.put(p, 1);
						}
					}
				});
			}
		}, 5L);
	}

}
