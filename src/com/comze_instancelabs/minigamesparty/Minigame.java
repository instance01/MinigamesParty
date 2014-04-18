package com.comze_instancelabs.minigamesparty;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesparty.minigames.MinigameUtil;

public class Minigame {
	
	public ArrayList<Player> lost = new ArrayList<Player>();
	
	public String name = "";
	public static Main m;
	public Location spawn;
	public Location lobby;
	public Location spectatorlobby;
	public Location finish;
	public String description;
	public boolean enabled;
	
	public Minigame(String arg1, String arg2, Main arg3, Location arg4, Location arg5, Location arg6, Location arg7){
		name = arg1;
		description = arg2;
		m = arg3;
		spawn = arg4;
		lobby = arg5;
		spectatorlobby = arg6;
		finish = arg7;
	}
	
	public void getWinner(){
		for(String pl : m.players){
			Player p = Bukkit.getPlayerExact(pl);
			if(p.isOnline()){
				if(!lost.contains(p)){
					m.win(p);
				}
			}
		}
		
		lost.clear();
	}
	
	int count = 5;
	BukkitTask cooldown = null;
	
	public void startCooldown(){
		//final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {

		final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimer(m, new Runnable() {
			public void run(){
				
				for(String p_ : m.players){
					Player p = Bukkit.getPlayerExact(p_);
					if(p.isOnline()){
						p.sendMessage(ChatColor.GREEN + "Starting in " + ChatColor.GOLD + Integer.toString(count));
					}
				}
				count--;
				if(count < 0){
					m.registerMinigameStart(m.minigames.get(m.currentmg).start());
					m.ingame_started = true;
					count = 5;
					cooldown.cancel();
					cooldown = null;
				}
			}
		}, 20, 20);
		cooldown = id__;
	}
	
	public BukkitTask start(){
		return null;
	}
	
	public void join(final Player p){
		if(p.hasPotionEffect(PotionEffectType.JUMP)){
			p.removePotionEffect(PotionEffectType.JUMP);
		}
		if(p.hasPotionEffect(PotionEffectType.SPEED)){
			p.removePotionEffect(PotionEffectType.SPEED);
		}
		if(p.getPassenger()  != null){
			Entity t = p.getPassenger();
			p.eject();
			t.remove();
		}
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				if(p.getPassenger() != null){
					Entity t = p.getPassenger();
					p.eject();
					t.remove();
				}
				p.teleport(spawn);
				p.setGameMode(GameMode.SURVIVAL);
				p.setAllowFlight(false);
				p.setFlying(false);
				p.sendMessage(MinigameUtil.nowPlaying(name));
				p.sendMessage(MinigameUtil.description(m.minigames.get(m.currentmg), description));
			}
		}, 5);
	}
	
	public void leave(final Player p){
		/*for (PotionEffect effect : p.getActivePotionEffects()) {
			if(p.hasPotionEffect(effect.getType())){
				try {
					p.removePotionEffect(effect.getType());
				} catch (Exception e) {
					
				}
			}
		}*/
		if(p.hasPotionEffect(PotionEffectType.JUMP)){
			p.removePotionEffect(PotionEffectType.JUMP);
		}
		if(p.hasPotionEffect(PotionEffectType.SPEED)){
			p.removePotionEffect(PotionEffectType.SPEED);
		}

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				if(p.getPassenger() != null){
					Entity t = p.getPassenger();
					p.eject();
					t.remove();
				}
				p.teleport(lobby);
				p.setAllowFlight(false);
				p.setFlying(false);
				if(p.hasPotionEffect(PotionEffectType.JUMP)){
					p.removePotionEffect(PotionEffectType.JUMP);
				}
				if(p.hasPotionEffect(PotionEffectType.SPEED)){
					p.removePotionEffect(PotionEffectType.SPEED);
				}
				
			}
		}, 5);
		
		Bukkit.getScheduler().runTaskLater(m, new Runnable(){
			public void run(){
				m.giveItemRewards(p, true);
			}
		}, 15L);
		
	}
	
	public void spectate(final Player p){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				if(p.getPassenger() != null){
					Entity t = p.getPassenger();
					p.eject();
					t.remove();
				}
				p.setAllowFlight(true);
				p.setFlying(true);
				p.teleport(spectatorlobby);
				p.getInventory().clear();
			}
		}, 5);
	}
	
	
	public void reset(final Location location){
	}
	
	public void setEnabled(boolean f){
		enabled = f;
		m.getConfig().set("minigames." + name + ".enabled", f);
		m.saveConfig();
	}
	
	public boolean isEnabled(){
		if(!m.getConfig().isSet("minigames." + name + ".enabled")){
			setEnabled(true);
			return true;
		}else{
			return m.getConfig().getBoolean("minigames." + name + ".enabled");
		}
	}
	
}
