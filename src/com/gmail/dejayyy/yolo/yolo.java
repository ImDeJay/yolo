package com.gmail.dejayyy.yolo;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class yolo extends JavaPlugin implements Listener{

	public static File configFile;
	public static File pluginFolder;
	public FileConfiguration playersFile;
	
	
	public void onEnable(){
		
		this.getServer().getPluginManager().registerEvents(this,  this);
		
		this.loadPlayerYML();
		this.saveDefaultConfig();
		
	}
	
	public void onDisable(){
		
		
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdL, String[] args)  {
		
		if(cmdL.equalsIgnoreCase("yolo")){
			sender.sendMessage(ChatColor.DARK_AQUA + "[YoLo] " + ChatColor.AQUA + "Plugin created by ImDeJay");
		}
		
		return true;
		
	}
	
	
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event){
		
		Player player = event.getPlayer();
		String message = event.getMessage();
		
		String whatToDo = this.getConfig().getString("WhatToDo");
		String banReason = replaceColors(this.getConfig().getString("banReason"));
		String kickReason = replaceColors(this.getConfig().getString("kickReason"));
		String slayReason = replaceColors(this.getConfig().getString("slayReason"));
		
		
		if(message.startsWith("#yolo")){
			
			if(player.hasPermission("yolo.use")){
				
				Random rand = new Random();

				boolean blah = rand.nextBoolean();
				
				if(blah){
					
					if(whatToDo.equalsIgnoreCase("ban")){
						
						player.setBanned(true);
						player.kickPlayer(banReason);
						
						this.playersFile.set(player.getName(), TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()));
						
						this.savePlayerYML();
						
					}else if(whatToDo.equalsIgnoreCase("kick")){
						
						player.kickPlayer(kickReason);
						
					}else if(whatToDo.equalsIgnoreCase("slay")){
						
						player.setHealth(0);
						
						if(!(slayReason.equalsIgnoreCase("disable"))){
							player.sendMessage(slayReason);
						}
						
					}else{
						
						this.getConfig().set("WhatToDo", "slay");
						
						player.setHealth(0);
						
						if(!(slayReason.equalsIgnoreCase("disable"))){
							player.sendMessage(slayReason);
						}
						
					}
					
				event.setCancelled(true);
				
					
				}
				//do some shit here
				
				String spareMSG = replaceColors(this.getConfig().getString("spareMSG"));
				
				if(!(spareMSG.equalsIgnoreCase("disable"))){
					player.sendMessage("you've been spared.");
				}
				
				event.setCancelled(true);
				
			} //permission
			
		} //if #yolo
		
	}
	
	
	@EventHandler
	public void playerJoin(PlayerLoginEvent event){
		
		
		String player = event.getPlayer().getName();
		
		long banTime = this.getConfig().getLong("banTime");
		
		if(this.playersFile.contains(player)){
			
			long curMin = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
			
			
			if(curMin > banTime){
				OfflinePlayer p = this.getServer().getOfflinePlayer(player);
				
				
				p.setBanned(false);

			}
			
		}

	}
	
	public void savePlayerYML(){
		
		try {
			playersFile.save(configFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void loadPlayerYML(){
		pluginFolder = getDataFolder();
	    configFile = new File(getDataFolder(), "players.yml");
	    
	    playersFile = new YamlConfiguration();
	    
		if(getDataFolder().exists() == false){
			
			try{
				getDataFolder().mkdir();
			}catch (Exception ex){
				//something went wrong.
			}
			
		} //plugin folder exists
	
	
		if(configFile.exists() == false){
			
			try{
				configFile.createNewFile();
			}catch (Exception ex){
				//something went wrong.
			}
		} //Configfile exist's
		
		try{ //Load payers.yml
			playersFile.load(configFile);
		}catch (Exception ex){
			//Something went wrong
		} //End load players.yml
	}
	
	static String replaceColors(String message)
	  {
	    return message.replaceAll("(?i)&([a-f0-9])", "§$1");
	  }
}
