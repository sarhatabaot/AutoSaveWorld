package autosaveworld.commands;

import autosaveworld.config.loader.ConfigLoader;
import autosaveworld.core.AutoSaveWorld;
import autosaveworld.utils.BukkitUtils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author sarhatabaot
 */
@CommandAlias("asw|autosaveworld")
@Description("Main autosaveworld command.")
public class AutoSaveWorldCommand extends BaseCommand {
	private AutoSaveWorld plugin;

	public AutoSaveWorldCommand(final AutoSaveWorld plugin) {
		this.plugin = plugin;
	}

	@Getter
	private static volatile boolean stoppedByAsw;

	@CommandAlias("version")
	@CommandPermission("asw.version")
	@Description("Shows the plugin's version.")
	public void onVersion(CommandSender sender){
		BukkitUtils.tell(sender, plugin.getDescription().getName() + " " + plugin.getDescription().getVersion());
	}

	@CommandAlias("stop")
	@CommandPermission("asw.stop")
	@Description("Stops the server.")
	public void onStop(){
		AutoSaveWorldCommand.stoppedByAsw = true;
		Bukkit.shutdown();
	}

	@CommandAlias("status")
	@CommandPermission("asw.status")
	@Description("Shows the server status.")
	public void onServerStatus(CommandSender sender){
		DecimalFormat df = new DecimalFormat("0.00");
		// processor (if available)
		try {
			OperatingSystemMXBean systemBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
			double cpuusage = systemBean.getSystemLoadAverage() * 100;
			if (cpuusage > 0) {
				sender.sendMessage(ChatColor.GOLD + "Cpu usage: " + ChatColor.RED + df.format(cpuusage) + "%");
			} else {
				sender.sendMessage(ChatColor.GOLD + "Cpu usage: " + ChatColor.RED + "not available");
			}
		} catch (Exception e) {
			plugin.getLogger().warning(e.getMessage());
		}
		// memory
		Runtime runtime = Runtime.getRuntime();
		long maxmemmb = runtime.maxMemory() / 1024 / 1024;
		long freememmb = (runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory())) / 1024 / 1024;
		sender.sendMessage(ChatColor.GOLD + "Memory usage: " + ChatColor.RED + df.format(((maxmemmb - freememmb) * 100) / maxmemmb) + "% " + ChatColor.DARK_AQUA + "(" + ChatColor.DARK_GREEN + (maxmemmb - freememmb) + "/" + maxmemmb + " MB" + ChatColor.DARK_AQUA + ")" + ChatColor.RESET);
		// hard drive
		File file = new File(".");
		long maxspacegb = file.getTotalSpace() / 1024 / 1024 / 1024;
		long freespacegb = file.getFreeSpace() / 1024 / 1024 / 1024;
		sender.sendMessage(ChatColor.GOLD + "Disk usage: " + ChatColor.RED + df.format(((maxspacegb - freespacegb) * 100) / maxspacegb) + "% " + ChatColor.DARK_AQUA + "(" + ChatColor.DARK_GREEN + (maxspacegb - freespacegb) + "/" + maxspacegb + " GB" + ChatColor.DARK_AQUA + ")" + ChatColor.RESET);
	}

	@CommandAlias("restart")
	@CommandPermission("asw.restart")
	@Description("Restarts the server.")
	public void onRestart(){
		plugin.getAutoRestartThread().triggerRestart(false);
	}

	@CommandAlias("reloadall")
	@CommandPermission("asw.reload.all")
	@Description("Reloads all configurations.")
	public void onReloadAll(CommandSender sender){
		ConfigLoader.loadAndSave(plugin.getMainConfig());
		ConfigLoader.loadAndSave(plugin.getMessageConfig());
		BukkitUtils.tell(sender, "All configurations reloaded.");
	}

	@CommandAlias("reload")
	@CommandPermission("asw.reload")
	@Description("Reloads the main config file.")
	public void onReloadConfig(CommandSender sender){
		ConfigLoader.loadAndSave(plugin.getMainConfig());
		BukkitUtils.tell(sender, "Main configuration reloaded");
	}
	@CommandAlias("reloadmsg")
	@CommandPermission("asw.reload.msg")
	@Description("Reloads the messages config file.")
	public void onReloadConfigMsg(CommandSender sender){
		ConfigLoader.loadAndSave(plugin.getMessageConfig());
		BukkitUtils.tell(sender, "Messages file reloaded");
	}

	@HelpCommand
	@CommandAlias("help")
	@Description("Shows the help menu.")
	public void onHelp(CommandHelp help){
		help.showHelp();
	}

	//TODO: Doesn't work.
	@CommandAlias("forcerestart")
	@CommandPermission("asw.forcerestart")
	@Description("Force restart the server.")
	public void onForceRestart(final CommandSender sender){
		sender.sendMessage("This command is currently unsupported.");
		//should check for script.bat and create it if it doesn't exist.
		//plugin.getAutoRestartThread().triggerRestart(true);
	}

	@CommandAlias("backup")
	@CommandPermission("asw.backup")
	@Description("Backs up the worlds and the plugins folder.")
	public void onBackup(){
		plugin.getBackupThread().triggerTaskRun();
	}

	@CommandAlias("forcegc")
	@CommandPermission("asw.forcegc")
	@Description("Force the GC to run.")
	public void onForceGc(CommandSender sender) {
		BukkitUtils.tell(sender, "&cAre you sure you want to use this command?");
		BukkitUtils.tell(sender, "&6Forcing the VM to use the garbage collector &c&bmight&r&6 help, or it may do nothing at all.");
		BukkitUtils.tell(sender, "&6Type &9/asw forcegc confirm &6to run the command.");
	}

	@CommandAlias("forcegc confirm")
	@CommandPermission("asw.forcegc")
	public void onConfirm(CommandSender sender){
		List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
		if (arguments.contains("-XX:+DisableExplicitGC")) {
			BukkitUtils.tell(sender,"&4Your JVM is configured to ignore GC calls, can't force gc");
			return;
		}
		BukkitUtils.tell(sender, "&9Forcing GC");
		System.gc();
		System.gc();
		BukkitUtils.tell(sender, "&9GC finished");
	}

}
