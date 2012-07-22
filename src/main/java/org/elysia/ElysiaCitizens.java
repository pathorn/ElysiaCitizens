package org.elysia;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.event.CitizensReloadEvent;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.scripting.EventRegistrar;
import net.citizensnpcs.api.scripting.ObjectProvider;
import net.citizensnpcs.api.scripting.ScriptCompiler;
import net.citizensnpcs.api.trait.TraitFactory;
import net.citizensnpcs.api.util.DataKey;
import net.citizensnpcs.api.util.DatabaseStorage;
import net.citizensnpcs.api.util.NBTStorage;
import net.citizensnpcs.api.util.Storage;
import net.citizensnpcs.api.util.YamlStorage;
/*
import net.citizensnpcs.Settings.Setting;
import net.citizensnpcs.command.CommandManager;
import net.citizensnpcs.command.Injector;
import net.citizensnpcs.command.command.AdminCommands;
import net.citizensnpcs.command.command.EditorCommands;
import net.citizensnpcs.command.command.HelpCommands;
import net.citizensnpcs.command.command.NPCCommands;
import net.citizensnpcs.command.command.ScriptCommands;
import net.citizensnpcs.command.exception.CommandException;
import net.citizensnpcs.command.exception.CommandUsageException;
import net.citizensnpcs.command.exception.ServerCommandException;
import net.citizensnpcs.command.exception.UnhandledCommandException;
import net.citizensnpcs.command.exception.WrappedCommandException;
import net.citizensnpcs.editor.Editor;
import net.citizensnpcs.npc.CitizensNPC;
import net.citizensnpcs.npc.CitizensNPCRegistry;
import net.citizensnpcs.npc.CitizensTraitFactory;
import net.citizensnpcs.npc.NPCSelector;
import net.citizensnpcs.util.Messaging;
import net.citizensnpcs.util.StringHelper;
*/
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
 
//import net.citizensnpcs.api.CitizensManager;

public class ElysiaCitizens extends JavaPlugin implements Listener{
	int mEntityCount=0;
	List<NPC> mEntities = new ArrayList<NPC>();
	public void onEnable() {
		getLogger().info("Enabling...");
		Plugin test = getServer().getPluginManager().getPlugin("Citizens");
		if (test != null) {
			System.out.println("Successfully hooked into Citizens!");
		} else {
			System.out.println("Citizens isn't loaded.");
			// disable your plugin because Citizens was not loaded
			getServer().getPluginManager().disablePlugin(this);
		}
		getLogger().info("Your plugin has been enabled!");
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run () {
				//System.out.println("Tick");
			}
		}, 0, 1);
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable(){
		getLogger().info("Your plugin has been disabled.");
		getServer().getScheduler().cancelTasks(this);
	}
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        System.out.println("Heard Command: "+cmd.getName());
        if(cmd.getName().equalsIgnoreCase("spawn")){ // If the player typed /basic then do the following...
			//String [] args= event.getMessage().split(" ");
			String entityType = "Villager";
			if (args.length>=1) {
				entityType = args[0];
			}
			NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.fromName(entityType), args.length>=2?args[1]:"Entity "+(this.mEntityCount++));
			this.mEntities.add(npc);
			if (sender instanceof Player) {
				Player player = ((Player)sender);
				Vector dir = player.getLocation().getDirection().clone().multiply(2);
				Location loc = new Location(player.getWorld(),player.getLocation().getX()+dir.getX(),player.getLocation().getY()+dir.getY(),player.getLocation().getZ()+dir.getZ());
				npc.spawn(loc);
				return true;
			}
        }
        return false;
    }
	@EventHandler
	public void onPlayerChatEvent(PlayerChatEvent event) {
        System.out.println("MSG:"+event.getMessage());
		if (event.getMessage().indexOf("spawn")==0) {

			String [] args= event.getMessage().split(" ");
			String entityType = "Villager";
			if (args.length>1) {
				entityType = args[1];
			}
			NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.fromName(entityType), args.length>2?args[2]:"Entity "+(this.mEntityCount++));
			this.mEntities.add(npc);
			Player player = event.getPlayer();
			Vector dir = player.getLocation().getDirection().clone().multiply(2);
            Location loc = new Location(player.getWorld(),player.getLocation().getX()+dir.getX(),player.getLocation().getY()+dir.getY(),player.getLocation().getZ()+dir.getZ());
            System.out.println("Spawn at :"+loc);
			npc.spawn(loc);
			
		}
	}
	
}

