package autosaveworld.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import autosaveworld.core.AutoSaveWorld;

public class BukkitUtils {

	public static void dispatchCommandAsConsole(String command) {
		ServerCommandEvent event = new ServerCommandEvent(Bukkit.getConsoleSender(), command);
		Bukkit.getPluginManager().callEvent(event);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getCommand());
	}

	public static void registerListener(Listener l) {
		Bukkit.getPluginManager().registerEvents(l, AutoSaveWorld.getInstance());
	}

	public static void unregisterListener(Listener l) {
		HandlerList.unregisterAll(l);
	}

	public static void tell(final CommandSender sender,final String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"[ASW] "+ message));
	}

}
