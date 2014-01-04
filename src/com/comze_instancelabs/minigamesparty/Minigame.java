package com.comze_instancelabs.minigamesparty;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
	
	public Minigame(String arg1, Main arg2, Location arg3, Location arg4, Location arg5, Location arg6){
		name = arg1;
		m = arg2;
		spawn = arg3;
		lobby = arg4;
		spectatorlobby = arg5;
		finish = arg6;
	}
	
	public void getWinner(){
		for(Player p : m.players){
			if(p.isOnline()){
				if(!lost.contains(p)){
					m.win(p);
				}
			}
		}
		
		lost.clear();
	}
	
	public BukkitTask start(){
		return null;
	}
	
	public void join(final Player p){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.teleport(spawn);
				p.setGameMode(GameMode.SURVIVAL);
				p.setAllowFlight(false);
				p.setFlying(false);
				p.sendMessage(MinigameUtil.nowPlaying(name));
			}
		}, 5);
	}
	
	public void leave(final Player p){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.teleport(lobby);
				p.setAllowFlight(false);
				p.setFlying(false);
			}
		}, 5);
	}
	
	public void spectate(final Player p){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.setAllowFlight(true);
				p.setFlying(true);
				p.teleport(spectatorlobby);
			}
		}, 5);
	}
	
}
