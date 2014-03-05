package com.comze_instancelabs.minigamesparty.minigames;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;
import com.comze_instancelabs.minigamesparty.Shop;

public class SmokeMonster extends Minigame implements Listener{

	public SmokeMonster(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("SmokeMonster", "Avoid the smoke monster!", arg2, arg3, arg4, arg5, null);
	}
	
	
	public ArrayList<Location> locs = new ArrayList<Location>();
	int currentloc = 0;
	
	int cxz = 0;
	
	public ArrayList<Integer> x_ = new ArrayList<Integer>();
	public ArrayList<Integer> z_ = new ArrayList<Integer>();
	
	@Override
	public BukkitTask start(){		

		fillArrayLists();
		
		int radius = 30;
		int radiusSquared = radius * radius;
		 
		for(int x = -radius; x <= radius; x++) {
		    for(int z = -radius; z <= radius; z++) {
		        if( (x*x) + (z*z) <= radiusSquared && (x*x) + (z*z) > radiusSquared - 60) {
		        	Location l = new Location(spawn.getWorld(), spawn.getX() + x, spawn.getY() + 2, spawn.getZ() + z);
		            locs.add(l);
		            spawn.getWorld().getBlockAt(l).setType(Material.WOOD);
		        }
		    }
		}
		
		final Random r = new Random();
		
		System.out.println("SIZE" + Integer.toString(locs.size()));
		
		final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimer(m, new Runnable() {
			@Override
			public void run(){
				Vector v = new Vector(x_.get(cxz), 0, z_.get(cxz));
				System.out.println(v);
				
				if(v.getBlockX() != 0 && v.getBlockZ() != 0){
					BlockIterator b = new BlockIterator(spawn.getWorld(), spawn.toVector(), v, 2, 30);
					
					while(b.hasNext()){
						if(b.hasNext()){
							Location l = b.next().getLocation();
							l.getWorld().createExplosion(l, 1F);
							l.getWorld().createExplosion(l.add(0D, -1D, 0D), 1F);
							l.getWorld().createExplosion(l.add(0D, -2D, 0D), 1F);
						}
					}
				}
				
				if(currentloc < locs.size() - 1){
					currentloc += 1;
				}
				
				if(cxz < 120 - 1){
					cxz ++;
				}else{
					cxz = 0;
				}
			}
		}, 8, 8);
		
		return id__;
	}

	
	@Override
	public void join(final Player p){
		super.join(p);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 60, 2));
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
				b.setType(Material.NETHER_BRICK);
			}
		}
	}
	
	public void fillArrayLists(){
		for (int i = 0; i < 30; i++) {
			x_.add(i); // 0 -> 30
		}
		for (int i = 30; i > 0; i--) {
			x_.add(i); // 30 -> 0
		}
		for (int i = 0; i < 30; i++) {
			x_.add(-i); // 0 -> -30
		}
		for (int i = 30; i > 0; i--) {
			x_.add(-i); // -30 -> 0
		}
		
		for (int i = 30; i > 0; i--) {
			z_.add(i); // 30 -> 0
		}
		for (int i = 0; i < 30; i++) {
			z_.add(-i); // 0 -> -30
		}
		for (int i = 30; i > 0; i--) {
			z_.add(-i); // -30 -> 0
		}
		for (int i = 0; i < 30; i++) {
			z_.add(i); // 0 -> 30
		}
	}
	
	
	@Override
	public void reset(Location start){
		z_.clear();
		x_.clear();
		currentloc = 0;
		locs.clear();
	}
}
