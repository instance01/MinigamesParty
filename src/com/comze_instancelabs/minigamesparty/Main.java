package com.comze_instancelabs.minigamesparty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.BlockIterator;

import com.comze_instancelabs.minigamesparty.minigames.ColorMatch;
import com.comze_instancelabs.minigamesparty.minigames.DeadEnd;
import com.comze_instancelabs.minigamesparty.minigames.JumpnRun;
import com.comze_instancelabs.minigamesparty.minigames.MineField;
import com.comze_instancelabs.minigamesparty.minigames.Spleef;
import com.comze_instancelabs.minigamesparty.nms.CraftMassBlockUpdate;
import com.comze_instancelabs.minigamesparty.nms.MassBlockUpdate;

public class Main extends JavaPlugin implements Listener {

	// ATTENTION: VERY RESOURCE INTENSIVE PLUGIN
	
	//TODO:
	// [HIGH] add all other minigames
	// [MEDIUM] add more commands and stats etc.
	// [LOW] add config support
	
	
	/* setup pattern:
	 * 
	 * # - - -
	 * # - - -
	 * # - - -
	 * # # # #
	 * 
	 * #=minigame
	 * 
	 * IMPORTANT: LOBBY SPAWN MUST BE ABOVE SPAWNS
	 */
	
	/*
	 * SETUP
	 * 
	 * 1. build main lobby
	 * 2. /mp setlobby
	 * 3. go to location somewhere UNDER lobby
	 * 4. /mp setup
	 * 5. reload server
	 * 
	 */
	
	public ArrayList<Minigame> minigames = new ArrayList<Minigame>();
	public ArrayList<Player> players = new ArrayList<Player>();
	public HashMap<Player, ItemStack[]> pinv = new HashMap<Player, ItemStack[]>();
	
	public int min_players = 1; //TODO: increment to more like 2 or 3
	public boolean running = false;

	public Location mainlobby = null;
	
	Main m;
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		m = this;
		int id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run(){
				if(isValidMinigame("ColorMatch") && isValidMinigame("Spleef") && isValidMinigame("MineField")){
					ColorMatch cm = new ColorMatch(m, m.getComponentForMinigame("ColorMatch", "spawn"), m.getLobby(), m.getComponentForMinigame("ColorMatch", "spectatorlobby"));
					minigames.add(cm);
					getServer().getPluginManager().registerEvents(cm, m);
					Spleef sp = new Spleef(m, m.getComponentForMinigame("Spleef", "spawn"), m.getLobby(), m.getComponentForMinigame("Spleef", "spectatorlobby"));
					minigames.add(sp);
					getServer().getPluginManager().registerEvents(sp, m);
					MineField mf = new MineField(m, m.getComponentForMinigame("MineField", "spawn"), m.getLobby(), m.getComponentForMinigame("MineField", "spectatorlobby"), m.getComponentForMinigame("MineField", "finishline"));
					minigames.add(mf);
					getServer().getPluginManager().registerEvents(mf, m);
					JumpnRun jr = new JumpnRun(m, m.getComponentForMinigame("JumpnRun", "spawn"), m.getLobby(), m.getComponentForMinigame("JumpnRun", "spectatorlobby"), m.getComponentForMinigame("JumpnRun", "finishline"));
					minigames.add(jr);
					getServer().getPluginManager().registerEvents(jr, m);
					DeadEnd de = new DeadEnd(m, m.getComponentForMinigame("DeadEnd", "spawn"), m.getLobby(), m.getComponentForMinigame("DeadEnd", "spectatorlobby"));
					minigames.add(de);
					getServer().getPluginManager().registerEvents(de, m);
				}
			}
		}, 40);
		
		getConfig().options().header("I recommend you to set auto_updating to true for possible future bugfixes.");
		getConfig().addDefault("config.auto_updating", true);
		/*getConfig().addDefault("config.rounds_per_game", 10);
		getConfig().addDefault("config.min_players", 4);
		getConfig().addDefault("config.use_economy", true);
		getConfig().addDefault("config.money_reward", 30);
		getConfig().addDefault("config.itemid", 264); // diamond
		getConfig().addDefault("config.itemamount", 1);*/
		getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		try{
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) { }
		
		if(getConfig().getBoolean("config.auto_updating")){
        	Updater updater = new Updater(this, 71596, this.getFile(), Updater.UpdateType.DEFAULT, false);
        }
	}
	
	
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("test")){
    		Location s = new Location(Bukkit.getWorld("void"), 0, 1, 0);
    		//ColorMatch.setBlockSuperFast(s.getWorld(), s.getWorld().getBlockAt(s), 35, (byte)1);
    	
    		final MassBlockUpdate mbu = CraftMassBlockUpdate.createMassBlockUpdater(this, s.getWorld());
    		 
    		mbu.setRelightingStrategy(MassBlockUpdate.RelightingStrategy.NEVER);
    		
    		mbu.setBlock(0, 1, 0, 35);
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("minigamesparty") || cmd.getName().equalsIgnoreCase("mp")){
    		if(args.length > 0){
    			if(args[0].equalsIgnoreCase("setup")){
    				// setup all arenas and spawns and lobbies and spectatorlobbies and what not
    				if(sender.hasPermission("mp.setup")){
	    				final Player p = (Player) sender;
	    				Bukkit.getServer().getScheduler().runTask(this, new Runnable(){
	    					public void run(){
	    						setupAll(p.getLocation());
	    					}
	    				});
    				}
    			}else if(args[0].equalsIgnoreCase("setlobby")){
    				if(sender.hasPermission("mp.setlobby")){
	    				Player p = (Player)sender;
	    				getConfig().set("lobby.world", p.getLocation().getWorld().getName());
	    				getConfig().set("lobby.location.x", p.getLocation().getBlockX());
	    				getConfig().set("lobby.location.y", p.getLocation().getBlockY());
	    				getConfig().set("lobby.location.z", p.getLocation().getBlockZ());
	    				this.saveConfig();
	    				p.sendMessage(ChatColor.GREEN + "Saved Main lobby.");	
    				}
    			}else if(args[0].equalsIgnoreCase("setcomponent")){
    				// /mp setcomponent [minigame] [component]
    				if(sender.hasPermission("mp.setcomponent")){
    					Player p = (Player)sender;
	    				if(args.length > 2){
	    					this.saveComponentForMinigame(args[1], args[2], p.getLocation());
	    					p.sendMessage(ChatColor.GREEN + "Saved component");
	    				}else{
	    					p.sendMessage(ChatColor.DARK_AQUA + "Possible components: spectatorlobby, spawn");
	    				}
    				}
    			}else if(args[0].equalsIgnoreCase("stats")){
    				if(sender instanceof Player){
	    				sender.sendMessage(ChatColor.DARK_AQUA + "-- " + ChatColor.GOLD + "Statistics " + ChatColor.DARK_AQUA + "--");
	    				if(args.length > 1){
	    					String player = args[1];
	    					sender.sendMessage(ChatColor.GREEN + "You have " + Integer.toString(this.getPlayerStats(player, "credits")) + " Credits.");
	    				}else{
	    					sender.sendMessage(ChatColor.GREEN + "You have " + Integer.toString(this.getPlayerStats(sender.getName(), "credits")) + " Credits.");
	    				}
    				}
    			}else if(args[0].equalsIgnoreCase("list")){
    				sender.sendMessage(ChatColor.DARK_AQUA + "-- " + ChatColor.GOLD + "Minigames: " + ChatColor.DARK_AQUA + "--");
    				for(Minigame m : minigames){
    					sender.sendMessage(ChatColor.DARK_AQUA + m.name);
    				}
    			}else if(args[0].equalsIgnoreCase("leave")){
    				final Player p = (Player)sender;
    				p.teleport(getLobby());
    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
    					public void run(){
    						p.teleport(getLobby());
    					}
    				}, 5);
    				p.getInventory().clear();
    				p.updateInventory();
    				p.getInventory().setContents(pinv.get(p));
    				p.updateInventory();
    				if(currentmg > -1){
    					minigames.get(currentmg).leave(p);
    				}
    				players.remove(p);
    				p.sendMessage(ChatColor.RED + "You left the game.");
    				if(players.size() < min_players){
    					stopFull();
    				}
    			}else{
    				sender.sendMessage(ChatColor.DARK_AQUA + "Help: ");
    				sender.sendMessage(ChatColor.DARK_AQUA + "/mp setlobby");
    				sender.sendMessage(ChatColor.DARK_AQUA + "/mp setup");
    				sender.sendMessage(ChatColor.DARK_AQUA + "/mp stats");
    				sender.sendMessage(ChatColor.DARK_AQUA + "/mp list");
    				sender.sendMessage(ChatColor.DARK_AQUA + "/mp leave");
    				sender.sendMessage(ChatColor.DARK_AQUA + "/mp setcomponent");
    			}
    		}
    		return true;
    	}
    	return false;
    }

    
    //TODO: player quits and rejoins -> still in arena!
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
    	if(players.contains(event.getPlayer())){
    		players.remove(event.getPlayer());
    	}
    	
    	if(players.size() < min_players){
    		stopFull();
    	}
    }
    
	@EventHandler
	public void onSignUse(PlayerInteractEvent event)
	{	
	    if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
	    {
	    	if(event.hasBlock()){
		    	if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)
		        {
		            final Sign s = (Sign) event.getClickedBlock().getState();
	                if (s.getLine(1).equalsIgnoreCase(ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "[PARTY]")){
	                	if(players.contains(event.getPlayer())){
	                		event.getPlayer().sendMessage(ChatColor.GOLD + "Use /mp leave to leave!");
	                	}else{
		                	players.add(event.getPlayer());
		                	// if its the first player to join, start the whole minigame
		                	if(players.size() < min_players + 1){
		                		pinv.put(event.getPlayer(), event.getPlayer().getInventory().getContents());
		                		startNew();
		                	}else{ // else: just join the minigame
		                		try{
		                			pinv.put(event.getPlayer(), event.getPlayer().getInventory().getContents());
		                			minigames.get(currentmg).join(event.getPlayer());
		                		}catch(Exception e){
		                			
		                		}
		                	}	
	                	}
	                }
		        }	
	    	}
	    }else if(event.getAction().equals(Action.PHYSICAL)){
	    	if(event.getClickedBlock().getType() == Material.STONE_PLATE){
	    		if(players.contains(event.getPlayer())){
	    			final Player p = event.getPlayer();
	    			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						@Override
						public void run() {
							p.teleport(minigames.get(currentmg).spawn);
						}
					}, 5);
	    			event.getClickedBlock().setType(Material.AIR);
	    		}
	    	}
	    }
	}
	
	@EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player p = event.getPlayer();
        if(event.getLine(0).toLowerCase().contains("[party]") || event.getLine(1).toLowerCase().contains("[party]")){
        	if(event.getPlayer().hasPermission("mp.sign")){
        		event.setLine(0, "");
	        	event.setLine(1, ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "[PARTY]");
        	}
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event){
		try{
			if(m.players.contains(event.getPlayer())){
				if(currentmg > -1){
					final Minigame current = minigames.get(currentmg);
					if(!current.lost.contains(event.getPlayer())){
						if(started){
							if(current.name.equalsIgnoreCase("DeadEnd")){
								World w = event.getPlayer().getWorld();
								Location under = new Location(w, event.getPlayer().getLocation().getBlockX(), event.getPlayer().getLocation().getBlockY() - 1, event.getPlayer().getLocation().getBlockZ());
								if(w.getBlockAt(under).getType() == Material.LAPIS_BLOCK){
									w.getBlockAt(under).setType(Material.AIR);
								}
							}
							if(event.getPlayer().getLocation().getBlockY() + 2 < current.spawn.getBlockY()){
								if(current.name.equalsIgnoreCase("JumpnRun")){
									final Player p = event.getPlayer();
									Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
										public void run(){
											p.teleport(current.spawn);
										}
									}, 5);
									return;
								}
								current.lost.add(event.getPlayer());
								current.spectate(event.getPlayer());
							}
						}
					}else if(current.lost.contains(event.getPlayer())){
						if(started){
							if(event.getPlayer().getLocation().getBlockY() < current.spectatorlobby.getBlockY() || event.getPlayer().getLocation().getBlockY() > current.spectatorlobby.getBlockY()){
								//current.spectate(event.getPlayer());
								final Player p = event.getPlayer();
								final Minigame mg = current;
								final float b = p.getLocation().getYaw();
								final float c = p.getLocation().getPitch();
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										p.setAllowFlight(true);
										p.setFlying(true);
										p.teleport(new Location(p.getWorld(), p.getLocation().getBlockX(), mg.spectatorlobby.getBlockY(), p.getLocation().getBlockZ(), b, c));
										//p.getLocation().setYaw(b);
										//p.getLocation().setPitch(c);
									}
								}, 5);
							}
						}
					}	
				}
				
			}	
		}catch(Exception e){
			for(StackTraceElement et : e.getStackTrace()){
				System.out.println(et);
			}
		}
		
	}
	
	@EventHandler
    public void onEntityDamage(EntityDamageEvent event){
    	if(event.getEntity() instanceof Player){
    		Player p = (Player)event.getEntity();
    		if(players.contains(p)){
    			event.setCancelled(true);
    		}
    	}
    }
    
    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
    	if(event.getEntity() instanceof Player){
    		Player p = (Player)event.getEntity();
    		if(players.contains(p)){
    			event.setCancelled(true);
    		}
    	}
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
    	if(players.contains(event.getPlayer())){
    		//SPLEEF
    		if(event.getBlock().getType() == Material.SNOW_BLOCK){
    			event.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL, 2));
    			event.getPlayer().updateInventory();
    			event.getBlock().setType(Material.AIR);
    			event.setCancelled(true);
    		}else{
    			event.setCancelled(true);
    		}
    	}
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
    	if(players.contains(event.getPlayer())){
    		event.setCancelled(true);
    	}
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
    	if(players.contains(((Player)event.getWhoClicked()))){
    		event.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
    	if(players.contains(event.getPlayer())){
			event.getItemDrop().remove();
			event.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onSnowballLand(ProjectileHitEvent e) {   
        if (e.getEntity().getShooter() instanceof Player) {
            if (e.getEntity() instanceof Snowball) {
                    
                Player player = (Player) e.getEntity().getShooter();
                if(players.contains(player)){
	            	BlockIterator bi = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
	                Block hit = null;
	                while (bi.hasNext()) {
	                        hit = bi.next();
	                        if (hit.getTypeId() != 0) {
	                                break;
	                        }
	                }
	                
	                if (hit.getLocation().getBlockY() < minigames.get(currentmg).spawn.getBlockY() && hit.getType() == Material.SNOW_BLOCK) {
	
	                    hit.setTypeId(0);
	                    
	                    player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
	                    /*for (Player sp : players) {
	                    	
	                            sp.getPlayer().playEffect(new Location(hit.getWorld(), hit.getLocation().getBlockX(), hit.getLocation().getBlockY() + 1.0D, hit.getLocation().getBlockZ()), Effect.MOBSPAWNER_FLAMES, 25);
	                    }*/
	    
	                }	
            	}
            }
        }
    }
    
	
	/*public void nextMinigame(Player p){
		// get current minigame and make winners
		// get new minigame and tp all to the new one
		p.setAllowFlight(false);
		p.setFlying(false);
		minigames.get(currentmg).getWinner();
		if(currentmg < minigames.size() - 1){
			currentmg += 1;
		}else{
			stop(currentid);
		}
		for(Minigame mg : minigames){
			mg.lost.clear();
		}
		minigames.get(currentmg).join(p);
	}*/
	
	public void win(Player p){
		//TODO: add winning of stars and statistics and scoreboard
		p.sendMessage(ChatColor.GOLD + "You won this round!");
		this.updatePlayerStats(p.getName(), "wins", getPlayerStats(p.getName(), "wins") + 1);
		Random r = new Random();
		int reward = r.nextInt(21) + 10; // between 10 and 30
		this.updatePlayerStats(p.getName(), "credits", getPlayerStats(p.getName(), "credits") + reward);		
		
		updateScoreboardOUTGAME(p.getName());
	}
	
	
	/**
	 * NEW TIMER PART
	 */
	int c = 0; // count
	int c_ = 0;
	boolean started = false;
	BukkitTask t = null;
	int currentmg = 0;
	BukkitTask currentid = null;
	public void secondsTick(){
		// update scoreboard
		updateScoreboard(60 - c);
		
		// stop the whole party after some rounds
		if(c_ > minigames.size() * 60 - 3){
			Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable(){
				public void run(){
					startNew();
				}
			}, 30 * 20); // 30 secs
			t.cancel();
			started = false;
			
			ArrayList<Player> remove = new ArrayList<Player>();
			for(Player p : players){
				if(p.isOnline()){
					minigames.get(minigames.size() - 1).leave(p);
					p.sendMessage(ChatColor.GOLD + "Next round in 30 seconds!");
					p.getInventory().clear();
					p.updateInventory();
				}else{
					remove.add(p);
				}
			}
			
			// removes players that aren't online anymore
			for(Player p : remove){
				players.remove(p);
			}
			
			remove.clear();
			
			currentmg = -1;
			currentid = null;
			
			// reset all:
			ColorMatch.reset(this.getComponentForMinigame("ColorMatch", "spawn"));
			Spleef.reset(this.getComponentForMinigame("Spleef", "spawn"));
			MineField.reset(this.getComponentForMinigame("MineField", "spawn"));
			
			c = 0;
			c_ = 0;
			if(currentid != null){
				currentid.cancel();
			}
		}
		
		// start the next minigame after 60 seconds
		if(c == 60){
			c = 0;
			if(currentid != null){
				currentid.cancel();
			}
			currentid = nextMinigame();
		}
		

		c += 1;
		c_ += 1;
	}

	public BukkitTask nextMinigame(){
		if(currentmg > -1){
			minigames.get(currentmg).getWinner();
			//TODO: add winners for score based minigames like MineField or JumpnRun
			// get top three of players who got most far
		}
		currentscore.clear();
		if(currentmg < minigames.size() - 1){
			currentmg += 1;
		}else{
			if(currentid != null){
				stop(currentid);
			}
		}
		for(Minigame mg : minigames){
			mg.lost.clear();
		}
		for(Player p : players){
			if(p.isOnline()){
				p.setAllowFlight(false);
				p.setFlying(false);
				
				minigames.get(currentmg).join(p);
			}
		}
		if(currentmg > -1){
			return minigames.get(currentmg).start();
		}else{
			return null;
		}
	}
	
	public void startNew(){
		if(!started){
			if(players.size() > min_players - 1){
				// reset all
				for(Minigame m : minigames){
					m.lost.clear();
				}
				currentmg = -1;
				currentid = null;
				
				// start first minigame
				currentid = nextMinigame();
				
				// start main timer
				t = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable(){
					public void run(){
						secondsTick();
					}
				}, 1, 20);
				
				started = true;
			}	
		}
	}
	/**
	 * NEW TIMER PART
	 */
	
	public HashMap<Player, Integer> currentscore = new HashMap<Player, Integer>();
	
	public void updateScoreboard(int c){
		ScoreboardManager manager = Bukkit.getScoreboardManager();
	    
		boolean isNeeded = false;
		if(minigames.get(currentmg).name.equalsIgnoreCase("MineField") || minigames.get(currentmg).name.equalsIgnoreCase("JumpnRun")){
			isNeeded = true;
		}
		
	    for(Player p : players){
	    	Scoreboard board = manager.getNewScoreboard();
	    	
	    	Objective objective = board.registerNewObjective("test", "dummy");
	        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

	        objective.setDisplayName("[" + Integer.toString(currentmg + 1) + "/" + Integer.toString(minigames.size()) + "] [" + Integer.toString(c) + "]");

	        for(Player p_ : players){
	        	if(isNeeded){
	        		int score = p_.getLocation().getBlockZ() - minigames.get(currentmg).finish.getBlockZ();
	        		if(currentscore.containsKey(p_)){
	        			int oldscore = currentscore.get(p_);
	        			if(score > oldscore){
	        				currentscore.put(p_, score);
	        			}else{
	        				score = oldscore;
	        			}
	        		}else{
	        			currentscore.put(p_, score);
	        		}
	        		objective.getScore(p_).setScore(score);
	        	}else{
	        		objective.getScore(p_).setScore(0);
	        	}
	        }

	        p.setScoreboard(board);
	    }
	}
	
	
	public void updateScoreboardOUTGAME(String player){
		ScoreboardManager manager = Bukkit.getScoreboardManager();

		Player p = Bukkit.getPlayer(player);
		
		Scoreboard board = manager.getNewScoreboard();
    	
    	Objective objective = board.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.setDisplayName(ChatColor.GOLD + "MinigamesParty!");

        objective.getScore(Bukkit.getOfflinePlayer("Credits")).setScore(this.getPlayerStats(player, "credits"));

        p.setScoreboard(board);
	}
	
	
	 /*public void start(){
		// if not running -> start
		// else just join current game
		//    if no current game, join into waiting lobby
		
		if(players.size() > min_players - 1){
			// reset all
			for(Minigame m : minigames){
				m.lost.clear();
			}
			currentmg = 0;
			currentid = 0;
			
			// every player joins again (or maybe first time)
			for(Player p : players){
				minigames.get(0).join(p);
			}
			final int stopid = minigames.get(0).start();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run(){
					Bukkit.getServer().getScheduler().cancelTask(stopid);
				}
			}, 1200);
			
			// main running timer
			if(!running){
				final int id__ = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
					@Override
					public void run(){
						int count = 0;
						ArrayList<Player> remove = new ArrayList<Player>();
						for(Player p : players){
							if(p.isOnline()){
								nextMinigame(p);
								count += 1;
							}else{
								remove.add(p);
							}
						}
						
						for(Player p : remove){
							players.remove(p);
						}
						
						remove.clear();
						
						if(count < min_players){ // one player left
							stopFull();
						}
					}
				}, 1200, 1200); // each 60 seconds -> change minigame	
				
				currentid = id__;
				
				int id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run(){
						start();
					}
				}, minigames.size() * 1200 + 20 * 30); // 20 * 30: wait 30 seconds after all games	
				
				int id_ = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run(){
						stop(id__);
					}
				}, minigames.size() * 1200 - 40);
				
				running = true;
			}
		}
	}*/
	
	public void stop(BukkitTask id){
		id.cancel();
		running = false;
		
		ArrayList<Player> remove = new ArrayList<Player>();
		for(Player p : players){
			if(p.isOnline()){
				minigames.get(minigames.size() - 1).leave(p);
				p.sendMessage(ChatColor.GOLD + "Next round in 30 seconds!");
				p.getInventory().clear();
				p.updateInventory();
			}else{
				remove.add(p);
			}
		}
		
		// removes players that arent online anymore
		for(Player p : remove){
			players.remove(p);
		}
		
		remove.clear();
		
		currentmg = 0;
		
		Bukkit.getScheduler().runTask(this, new Runnable(){
			public void run(){
				resetAll();
			}
		});
		
	}
	
	public void stopFull(){
		Bukkit.getServer().getScheduler().cancelAllTasks();
		
		for(Player p : players){
			if(p.isOnline()){
				minigames.get(minigames.size() - 1).leave(p);
				p.sendMessage(ChatColor.DARK_RED + "Stopping minigame.");
			}
		}
		
		running = false;
		started = false;
		players.clear();
		currentmg = 0;
		
		Bukkit.getScheduler().runTask(this, new Runnable(){
			public void run(){
				resetAll();
			}
		});
	}
	
	public Location getLobby(){
		return new Location(getServer().getWorld(getConfig().getString("lobby.world")), getConfig().getInt("lobby.location.x"), getConfig().getInt("lobby.location.y"), getConfig().getInt("lobby.location.z"));
	}
	
	public Location getComponentForMinigame(String minigame, String component, String count){
		if(isValidMinigame(minigame)){
			String base = "minigames." + minigame + "." + component + count;
			return new Location(Bukkit.getWorld(getConfig().getString(base + ".world")), getConfig().getInt(base + ".location.x"), getConfig().getInt(base + ".location.y"), getConfig().getInt(base + ".location.z"));
		}
		return null;
	}
	
	public Location getComponentForMinigame(String minigame, String component){
		if(isValidMinigame(minigame)){
			String base = "minigames." + minigame + "." + component;
			return new Location(Bukkit.getWorld(getConfig().getString(base + ".world")), getConfig().getInt(base + ".location.x"), getConfig().getInt(base + ".location.y"), getConfig().getInt(base + ".location.z"));
		}
		return null;
	}
	
	public void saveComponentForMinigame(String minigame, String component, Location comploc){
		String base = "minigames." + minigame + "." + component;
		getConfig().set(base + ".world", comploc.getWorld().getName());
		getConfig().set(base + ".location.x", comploc.getBlockX());
		getConfig().set(base + ".location.y", comploc.getBlockY());
		getConfig().set(base + ".location.z", comploc.getBlockZ());
		this.saveConfig();
	}
	
	public boolean isValidMinigame(String minigame){
		if(getConfig().isSet("minigames." + minigame) && getConfig().isSet("minigames." + minigame + ".lobby") && getConfig().isSet("minigames." + minigame + ".spawn") && getConfig().isSet("minigames." + minigame + ".spectatorlobby")){
			return true;
		}
		return false;
	}
	
	@Deprecated
	public void setupAll(Location start){
		int x = start.getBlockX();
		int y = start.getBlockY();
		int z = start.getBlockZ();
		
		ColorMatch.setup(start, this, "ColorMatch");
		Spleef.setup(new Location(start.getWorld(), x, y, z + 64 + 20), this, "Spleef");
		MineField.setup(new Location(start.getWorld(), x, y, z + 64 * 2 + 20 * 2), this, "MineField");
		JumpnRun.setup(new Location(start.getWorld(), x, y, z + 64 * 3 + 20 * 3), this, "JumpnRun");
		DeadEnd.setup(new Location(start.getWorld(), x + 64 + 20, y, z), this, "DeadEnd");
		
		/*
		 * next minigame locations: (TODO FOR LATER USE)
		 * 
		 * new Location(start.getWorld(), x, y, z + 64 * 2 + 20 * 2) [MINEFIELD]
		 * new Location(start.getWorld(), x, y, z + 64 * 3 + 20 * 3) [JUMPNRUN]
		 * new Location(start.getWorld(), x + 64 + 20, y, z) [DEADEND]
		 * new Location(start.getWorld(), x + 64 * 2 + 20 * 2, y, z)
		 * new Location(start.getWorld(), x + 64 * 3 + 20 * 3, y, z)
		 * 
		 * would create the following pattern:
		 * 
		 * # - - -
		 * # - - -
		 * # - - -
		 * # # # #
		 * 
		 * #=minigame
		 * 
		 * IMPORTANT: LOBBY SPAWN MUST BE ABOVE SPAWNS!
		 */ 
		
		minigames.clear();
		minigames.add(new ColorMatch(this, this.getComponentForMinigame("ColorMatch", "spawn"), this.getComponentForMinigame("ColorMatch", "lobby"), this.getComponentForMinigame("ColorMatch", "spectatorlobby")));
		minigames.add(new Spleef(this, this.getComponentForMinigame("Spleef", "spawn"), this.getComponentForMinigame("Spleef", "lobby"), this.getComponentForMinigame("Spleef", "spectatorlobby")));
		minigames.add(new MineField(this, this.getComponentForMinigame("MineField", "spawn"), this.getComponentForMinigame("MineField", "lobby"), this.getComponentForMinigame("MineField", "spectatorlobby"), m.getComponentForMinigame("MineField", "finishline")));
		minigames.add(new JumpnRun(this, this.getComponentForMinigame("JumpnRun", "spawn"), this.getComponentForMinigame("JumpnRun", "lobby"), this.getComponentForMinigame("JumpnRun", "spectatorlobby"), m.getComponentForMinigame("JumpnRun", "finishline")));
		minigames.add(new DeadEnd(this, this.getComponentForMinigame("DeadEnd", "spawn"), this.getComponentForMinigame("DeadEnd", "lobby"), this.getComponentForMinigame("DeadEnd", "spectatorlobby")));
		
		getLogger().info("Finished Setup");
	}
	
	public void resetAll(){
		ColorMatch.reset(this.getComponentForMinigame("ColorMatch", "spawn"));
		Spleef.reset(this.getComponentForMinigame("Spleef", "spawn"));
		Location t = this.getComponentForMinigame("MineField", "spawn");
		MineField.reset(new Location(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ() + 30));
		DeadEnd.reset(this.getComponentForMinigame("DeadEnd", "spawn"));
	}
	

	/***
	 * saves player statistics
	 * @param player
	 * @param component component to be updated; can be "wins" or "credits"
	 * @param value value to be saved
	 */
	public void updatePlayerStats(String player, String component, int value){
		getConfig().set(player + "." + component, value);
	}
	
	public int getPlayerStats(String player, String component){
		int ret = 0;
		if(getConfig().isSet(player + "." + component)){
			ret = getConfig().getInt(player + "." + component);
		}
		return ret;
	}
	
	

}