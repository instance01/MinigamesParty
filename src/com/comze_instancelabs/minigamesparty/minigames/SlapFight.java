package com.comze_instancelabs.minigamesparty.minigames;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.comze_instancelabs.minigamesparty.Main;
import com.comze_instancelabs.minigamesparty.Minigame;
import com.comze_instancelabs.minigamesparty.Shop;

public class SlapFight extends Minigame implements Listener{
	
	public SlapFight(Main arg2, Location arg3, Location arg4, Location arg5) {
		super("SlapFight", MinigameUtil.getDescription(arg2, "SlapFight"), arg2, arg3, arg4, arg5, null);
	}
	
	@Override
	public void join(final Player p){
		super.join(p);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				p.getInventory().clear();
				p.updateInventory();
				ItemStack item = new ItemStack(Material.STICK);
				ItemMeta meta = item.getItemMeta();
				int temp = Shop.getPlayerShopComponent(m, p.getName(), "slapfight_knockback");
				if(temp > 0){
					meta.addEnchant(Enchantment.KNOCKBACK, 10, true);
					meta.setDisplayName(ChatColor.RED + "ULTRA SLAPPER");
					Shop.removeFromPlayerShopComponent(m, p.getName(), "slapfight_knockback", 1);
				}else{
					meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
					meta.setDisplayName(ChatColor.RED + "MEDIUM SLAPPER");
				}
				item.setItemMeta(meta);
				p.getInventory().addItem(item);
				p.updateInventory();
			}
		}, 5);
	}
	
	static Random r = new Random();
	
	public static void setup(Location start, Main main, String name_){
		int y = start.getBlockY();

		main.saveComponentForMinigame(name_, "spawn", new Location(start.getWorld(), start.getBlockX(), y + 2, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "spectatorlobby", new Location(start.getWorld(), start.getBlockX(), y + 30, start.getBlockZ()));
		main.saveComponentForMinigame(name_, "lobby", main.getLobby());
		
		
		createCircle(9, start, 0, 0, 0);
		createCircle(7, start, 7, 1, 7);
		createCircle(8, start, -7, 1, -6);
		createCircle(5, start, 7, 1, -6);
		createCircle(6, start, 11, 2, -11);
		createCircle(5, start, 1, 2, -11);
		createCircle(7, start, -17, 2, -17);
		createCircle(6, start, -15, 2, 9);
		createCircle(6, start, -20, 3, 20);
		createCircle(6, start, 20, 3, -20);
		createCircle(6, start, 20, 3, 20);
		
		/*for(int i = 0; i < 64; i++){
			for(int j = 0; j < 64; j++){
				Block b_ = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y - 4, z + j));
				b_.setType(Material.GLOWSTONE);
				Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y, z + j));
				b.setType(Material.STONE);
			}
		}*/
	}
	
	public static void createCircle(int radius, Location start, int xmod, int ymod, int zmod){
		int x = start.getBlockX() + xmod;
		int y = start.getBlockY() + ymod;
		int z = start.getBlockZ() + zmod;
		
		int radiusSquared = radius * radius;
		for(int x_ = -radius; x_ <= radius; x_++) {
		    for(int z_ = -radius; z_ <= radius; z_++) {
		        if( (x_*x_) + (z_*z_) <= radiusSquared) {
		        	Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x - x_, y, z - z_));
					b.setType(Material.WOOL);
					b.setData((byte)r.nextInt(15));
		        }
		    }
		}
	}

}
