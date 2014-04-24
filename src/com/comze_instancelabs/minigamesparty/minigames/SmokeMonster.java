package com.comze_instancelabs.minigamesparty.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;
import com.comze_instancelabs.minigamesparty.Shop;
import com.comze_instancelabs.minigamesparty.nms.NMSEffectManager;

public class SmokeMonster extends Minigame implements Listener {

	public SmokeMonster(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("SmokeMonster", MinigameUtil.getDescription(arg2, "SmokeMonster"), arg2, arg3, arg4, arg5, null);
	}

	public static ArrayList<Location> locs = new ArrayList<Location>();
	int currentloc = 0;

	int cxz = 0;

	public ArrayList<Integer> x_ = new ArrayList<Integer>();
	public ArrayList<Integer> z_ = new ArrayList<Integer>();

	Random r = new Random();
	
	// possible modes: straight, straightUD
	String cmode = "straight";
	
	@Override
	public BukkitTask start() {
		fillArrayLists();

		final Random r = new Random();

		final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimer(m, new Runnable() {
			@Override
			public void run() {
				if(r.nextInt(100) < 3){
					if(cmode.equalsIgnoreCase("straight")){
						cmode = "straightUD";
					}else{
						cmode = "straight";
					}
				}
				
				if(cmode.equalsIgnoreCase("straight")){
					straightLineMonster();
				}else{
					straightLineMonsterUpDown();
				}
				
			}
		}, 2, 2);

		return id__;
	}

	@Override
	public void join(final Player p) {
		super.join(p);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 64, 4));
				int temp = Shop.getPlayerShopComponent(m, p.getName(), "smokemonster_boost");
				if(temp > 0){
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 2));
					Shop.removeFromPlayerShopComponent(m, p.getName(), "smokemonster_boost", 1);
				}
			}
		}, 5);
	}

	public static void setup(Location start, Main main, String name_) {
		Random r = new Random();
		int x = start.getBlockX();// - 32;
		int y = start.getBlockY();
		int z = start.getBlockZ();// - 32;

		main.saveComponentForMinigame(name_, "spawn", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "spectatorlobby", new Location(start.getWorld(), start.getBlockX(), y + 30, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "lobby", main.getLobby());

		int radius = 30;
		int radiusSquared = radius * radius;
		 
		for(int x_ = -radius; x_ <= radius; x_++) {
		    for(int z_ = -radius; z_ <= radius; z_++) {
		        if( (x_*x_) + (z_*z_) <= radiusSquared) {
		        	Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x - x_, y, z - z_));
					b.setType(Material.NETHER_BRICK);
		        }
		    }
		}
		
		/*for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				b.setType(Material.NETHER_BRICK);
			}
		}*/
	}

	public void fillArrayLists() {
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
	public void reset(Location start) {
		z_.clear();
		x_.clear();
		currentloc = 0;
		locs.clear();
		cxz = 0;
		currentoffset = 0;
		errorcount = 0;
	}

	public void straightLineMonster() {
		Vector v = new Vector(x_.get(cxz), 0, z_.get(cxz));

		locs.clear();

		if (v.getBlockX() != 0 && v.getBlockZ() != 0) {
			BlockIterator b = new BlockIterator(spawn.getWorld(), spawn.toVector(), v, 2, 30);
			BlockIterator b_ = new BlockIterator(spawn.getWorld(), spawn.toVector(), v.multiply(-1D), 2, 30);

			while (b.hasNext()) {
				if (b.hasNext()) {
					Location l = b.next().getLocation();
					locs.add(l);
					/*l.getWorld().createExplosion(l, -1F);
					l.getWorld().createExplosion(l.add(0D, -1D, 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -2D, 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -0.5D, 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -1.5D, 0D), -1F);*/
					NMSEffectManager.createParticles(l, 1, 1);
				}
			}
			
			while (b_.hasNext()) {
				if (b_.hasNext()) {
					Location l = b_.next().getLocation();
					locs.add(l);
					/*l.getWorld().createExplosion(l, -1F);
					l.getWorld().createExplosion(l.add(0D, -1D, 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -2D, 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -0.5D, 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -1.5D, 0D), -1F);*/
					NMSEffectManager.createParticles(l, 1, 1);
				}
			}
		}

		if (currentloc < locs.size() - 1) {
			currentloc += 1;
		}

		if (cxz < 120 - 1) {
			cxz++;
		} else {
			cxz = 0;
		}
	}

	public ArrayList<Integer> w = new ArrayList<Integer>(Arrays.asList(7, 7, 7, 6, 6, 5, 4, 3, 2, 2, 1, 1, 1, 2, 2, 3, 4, 5, 6, 6, 7, 7, 7, 6, 6, 5, 4, 3, 2, 2, 1, 1, 1, 2, 2, 3, 4, 5, 6, 6, 7, 7, 7, 6, 6, 5, 4, 3, 2, 2, 1, 1, 1, 2, 2, 3, 4, 5, 6, 6, 7, 7, 7, 6, 6, 5, 4, 3, 2, 2, 1, 1, 1, 2, 2, 3, 4, 5, 6, 6, 7, 7, 7, 6, 6, 5, 4, 3));
	int currentoffset = 0;
	boolean back = false;

	int errorcount = 0;
	
	public void straightLineMonsterUpDown() {
		Vector v = new Vector(x_.get(cxz), 0, z_.get(cxz));

		locs.clear();

		if (v.getBlockX() != 0 && v.getBlockZ() != 0) {
			BlockIterator b = new BlockIterator(spawn.getWorld(), spawn.toVector(), v, 2, 30);
			BlockIterator b_ = new BlockIterator(spawn.getWorld(), spawn.toVector(), v.multiply(-1D), 2, 30);

			int c = 0;
			int c_ = 0;
			if (!back) {
				currentoffset++;
			} else {
				currentoffset--;
			}

			if (currentoffset > 11) {
				back = true;
			}

			if (currentoffset < 1) {
				back = false;
			}

			while (b.hasNext()) {
				if (b.hasNext()) {
					int mod = 0;
					if(c + currentoffset > w.size() - 1){
						if(errorcount < 10){
							m.getLogger().info("The offset was higher than expected.");
						}
						errorcount++;
						mod = w.size() - 1;
					}else{
						mod = w.get(c + currentoffset);
					}
					c++;
					Location l = b.next().getLocation();
					locs.add(l);
					/*l.getWorld().createExplosion(l.add(0D, 0D + (mod / 2), 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -1D + (mod / 2), 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -2D + (mod / 2), 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -0.5D + (mod / 2), 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -1.5D + (mod / 2), 0D), -1F);*/
					NMSEffectManager.createParticles(l.add(0D, (mod / 2), 0D), 1, 1);
				}
			}
			
			while (b_.hasNext()) {
				if (b_.hasNext()) {
					int mod = 0;
					if(c_ + currentoffset > w.size() - 1){
						if(errorcount < 10){
							m.getLogger().info("The offset was higher than expected.");
						}
						errorcount++;
						mod = w.size() - 1;
					}else{
						mod = w.get(c_ + currentoffset);
					}
					c_++;
					Location l = b_.next().getLocation();
					locs.add(l);
					/*l.getWorld().createExplosion(l.add(0D, 0D + (mod / 2), 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -1D + (mod / 2), 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -2D + (mod / 2), 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -0.5D + (mod / 2), 0D), -1F);
					l.getWorld().createExplosion(l.add(0D, -1.5D + (mod / 2), 0D), -1F);*/
					NMSEffectManager.createParticles(l.add(0D, (mod / 2), 0D), 1, 1);
				}
			}
		}

		if (currentloc < locs.size() - 1) {
			currentloc += 1;
		}

		if (cxz < 120 - 1) {
			cxz++;
		} else {
			cxz = 0;
		}
	}
}
