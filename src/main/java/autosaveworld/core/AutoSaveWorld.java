/*
  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 3
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 */

package autosaveworld.core;

import java.io.File;

import autosaveworld.commands.AutoSaveWorldCommand;
import autosaveworld.features.backup.AutoBackupThread;
import autosaveworld.utils.threads.SIntervalTaskThread;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.sarhatabaot.configloader.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import autosaveworld.config.AutoSaveWorldConfig;
import autosaveworld.config.AutoSaveWorldConfigMSG;
import autosaveworld.core.logging.MessageLogger;
import autosaveworld.features.consolecommand.AutoConsoleCommandThread;
import autosaveworld.utils.FileUtils;
import autosaveworld.utils.ReflectionUtils;
import autosaveworld.utils.StringUtils;

@Getter
public class AutoSaveWorld extends JavaPlugin {
	@Setter (AccessLevel.PRIVATE)
	private static AutoSaveWorld instance;

	private final AutoSaveWorldConfig mainConfig;
	private final AutoSaveWorldConfigMSG messageConfig;

	private final AutoBackupThread backupThread;
	private final AutoConsoleCommandThread consolecommandThread;



	public static AutoSaveWorld getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Instance access before init");
		}
		return instance;
	}

	public AutoSaveWorld() {
		if (!Bukkit.isPrimaryThread()) {
			throw new IllegalStateException("Init not fom main thread");
		}
		if (instance != null) {
			MessageLogger.warn("Instance wasn't null when enabling, this is not a good sign");
		}
		setInstance(this);
		//important to create instance here
		mainConfig = new AutoSaveWorldConfig();
		messageConfig = new AutoSaveWorldConfigMSG();
		backupThread = new AutoBackupThread();
		consolecommandThread = new AutoConsoleCommandThread();
	}

	@Override
	public void onEnable() {
		PaperCommandManager manager = new PaperCommandManager(this);
		manager.enableUnstableAPI("help");
		manager.registerCommand(new AutoSaveWorldCommand(this));

		ConfigLoader.loadAndSave(mainConfig);
		ConfigLoader.loadAndSave(messageConfig);
		preloadClasses();

		backupThread.start();
		consolecommandThread.start();

		getLogger().info(getName()+ " v"+getDescription().getVersion()+" enabled!");
	}

	private static void preloadClasses() {
		//preload core classes, so replacing jar file won't break plugin completely (Some core functions should work)
		ReflectionUtils.init();
		FileUtils.init();
		StringUtils.init();
	}

	@Override
	public void onDisable() {
		ConfigLoader.save(mainConfig);
		ConfigLoader.save(messageConfig);
		stopThread(consolecommandThread);
		stopThread(backupThread);
	}

	private static void stopThread(SIntervalTaskThread tt) {
		tt.stopThread();
		try {
			tt.join(2000);
		} catch (InterruptedException e) {
			getInstance().getLogger().warning(e.getMessage());
		}
	}

}
