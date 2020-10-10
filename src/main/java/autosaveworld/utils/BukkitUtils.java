package autosaveworld.utils;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.server.ServerCommandEvent;


public class BukkitUtils {

	public static void dispatchCommandAsConsole(String command) {
		ServerCommandEvent event = new ServerCommandEvent(Bukkit.getConsoleSender(), command);
		Bukkit.getPluginManager().callEvent(event);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getCommand());
	}

	public static void tell(final CommandSender sender,final String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"[ASW] "+ message));
	}

}
