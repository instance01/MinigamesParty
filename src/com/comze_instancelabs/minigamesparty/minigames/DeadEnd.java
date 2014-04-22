package com.comze_instancelabs.minigamesparty.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;
import com.comze_instancelabs.minigamesparty.Shop;

public class DeadEnd extends Minigame implements Listener{
	
	public DeadEnd(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("DeadEnd", MinigameUtil.getDescription(arg2, "DeadEnd"), arg2, arg3, arg4, arg5, null);
	}
	
	@Override
	public void join(final Player p){
		super.join(p);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.getInventory().clear();
				p.updateInventory();
				
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 64, -5));
				
				int temp = Shop.getPlayerShopComponent(m, p.getName(), "speed_boost");
				if(temp > 0){
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 2));
					Shop.removeFromPlayerShopComponent(m, p.getName(), "speed_boost", 1);
				}
			}
		}, 5);
	}
	
	@Override
	public BukkitTask start(){
		return Bukkit.getScheduler().runTaskTimer(m, new Runnable(){
			public void run(){
				for(String p_ : m.players){
					Player p = Bukkit.getPlayer(p_);
					if(p.isOnline()){
						if(!lost.contains(p)){
							Location l_ = p.getLocation();
							l_.setPitch(0F);
							Vector dir = l_.getDirection().normalize().multiply(0.5D); // 0.4
							Vector dir_ = new Vector(dir.getX(), 0.0001D, dir.getZ());
							p.setVelocity(dir_);

							Vector v = p.getLocation().getDirection().normalize();
							Location l = p.getLocation().subtract((new Vector(v.getX(), 0.0001D, v.getZ()).multiply(-1D)));
						}
					}
				}
			}
		}, 3L, 3L);
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
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				b.setType(Material.LAPIS_BLOCK);
				Block b_ = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y - 4, z + j));
				b_.setType(Material.GLOWSTONE);
			}
		}
	}
	
	@Override
	public void reset(Location start){
		int x = start.getBlockX() - 32;
		int y = start.getBlockY() - 2;
		int z = start.getBlockZ() - 32;
		
		int current = 0;
		int temp = 4;
		boolean cont = false;
		
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				b.setType(Material.LAPIS_BLOCK);
			}
		}
	}
}
