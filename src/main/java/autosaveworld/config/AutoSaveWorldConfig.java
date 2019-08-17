/*
  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 3
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 */

package autosaveworld.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autosaveworld.config.loader.Config;
import autosaveworld.config.loader.ConfigOption;
import autosaveworld.config.loader.postload.AstListAppend;
import autosaveworld.config.loader.postload.DefaultCountdown;
import autosaveworld.config.loader.postload.DefaultDestFolder;
import autosaveworld.config.loader.transform.ConfSectIntHashMap;
import autosaveworld.config.loader.transform.ConfSectStringHashMap;
import autosaveworld.config.loader.transform.ListClone;
import autosaveworld.core.GlobalConstants;

public class AutoSaveWorldConfig implements Config {

	// some global variables
	@ConfigOption(path = "var.debug")
	public boolean varDebug = false;
	@ConfigOption(path = "var.commandsonlyfromconsole")
	public boolean commandOnlyFromConsole = false;
	// save
	@ConfigOption(path = "save.interval")
	public int saveInterval = 900;
	@ConfigOption(path = "save.broadcast")
	public boolean saveBroadcast = true;
	@ConfigOption(path = "save.disablestructuresaving")
	public boolean saveDisableStructureSaving = false;
	/*@ConfigOption(path = "save.forceregioncachedump")
	public boolean saveDumpRegionCache = true;*/
	@ConfigOption(path = "save.onplugindisable")
	public boolean saveOnASWDisable = true;
	// restart
	@ConfigOption(path = "restart.juststop")
	public boolean restartJustStop = false;
	@ConfigOption(path = "restart.oncrash.enabled", legacypath = "crashrestart.enabled")
	public boolean restartOncrashEnabled = false;
	@ConfigOption(path = "restart.oncrash.scriptpath", legacypath = "crashrestart.scriptpath")
	public String restartOnCrashScriptPath = "";
	@ConfigOption(path = "restart.oncrash.timeout", legacypath = "crashrestart.timeout")
	public long restartOnCrashTimeout = 60;
	@ConfigOption(path = "restart.oncrash.checkerstartdelay", legacypath = "crashrestart.startdelay")
	public int restartOnCrashCheckerStartDelay = 20;
	@ConfigOption(path = "restart.oncrash.runonnonpluginstop", legacypath = "crashrestart.runonnonpluginstop")
	public boolean restartOnCrashOnNonAswStop = false;
	@ConfigOption(path = "restart.auto.enabled", legacypath = "autorestart.enabled")
	public boolean autoRestart = false;
	@ConfigOption(path = "restart.auto.broadcast", legacypath = "autorestart.broadcast")
	public boolean autoRestartBroadcast = true;
	@ConfigOption(path = "restart.auto.scriptpath", legacypath = "autorestart.scriptpath")
	public String autoRestartScriptPath = "";
	@ConfigOption(path = "restart.auto.time", legacypath = "autorestart.time")
	public List<String> autoRestartTimes = new ArrayList<>();
	@ConfigOption(path = "restart.auto.countdown.enabled", legacypath = "autorestart.countdown.enabled")
	public boolean autoRestartCountdown = true;
	@ConfigOption(path = "restart.auto.countdown.broadcastonsecond", legacypath = "autorestart.countdown.broadcastonsecond", transform = ListClone.class, postload = DefaultCountdown.class)
	public List<Integer> autoRestartCountdownSeconds = new ArrayList<>();
	@ConfigOption(path = "restart.auto.commands", transform = ListClone.class, legacypath = "autorestart.commands")
	public List<String> autoRestartPreStopCommmands = new ArrayList<>();
	// localfs backup
	@ConfigOption(path = "backup.localfs.enabled")
	public boolean backupLFSEnabled = true;
	@ConfigOption(path = "backup.localfs.destinationfolders", transform = ListClone.class, postload = DefaultDestFolder.class)
	public List<String> backupLFSExtFolders = new ArrayList<>();
	@ConfigOption(path = "backup.localfs.worlds", transform = ListClone.class, postload = AstListAppend.class)
	public List<String> backupLFSBackupWorldsList = new ArrayList<>();
	@ConfigOption(path = "backup.localfs.MaxNumberOfWorldsBackups")
	public int backupLFSMaxNumberOfWorldsBackups = 15;
	@ConfigOption(path = "backup.localfs.pluginsfolder")
	public boolean backupLFSPluginsFolder = false;
	@ConfigOption(path = "backup.localfs.MaxNumberOfPluginsBackups")
	public int backupLFSMaxNumberOfPluginsBackups = 15;
	@ConfigOption(path = "backup.localfs.otherfolders", transform = ListClone.class)
	public List<String> backupLFSOtherFolders = new ArrayList<>();
	@ConfigOption(path = "backup.localfs.MaxNumberOfOtherFoldersBackups")
	public int backupLFSMaxNumberOfOtherBackups = 15;
	@ConfigOption(path = "backup.localfs.excludefolders", transform = ListClone.class)
	public List<String> backupLFSExcludeFolders = new ArrayList<>();
	@ConfigOption(path = "backup.localfs.zip")
	public boolean backupLFSZipEnabled = false;
	// consolecmmand
	@ConfigOption(path = "consolecommand.timemode.enabled")
	public boolean ccTimesModeEnabled = false;
	@ConfigOption(path = "consolecommand.timemode.times", transform = ConfSectStringHashMap.class)
	public Map<String, List<String>> ccTimesModeCommands = new HashMap<>();
	@ConfigOption(path = "consolecommand.intervalmode.enabled")
	public boolean ccIntervalsModeEnabled = false;
	@ConfigOption(path = "consolecommand.intervalmode.intervals", transform = ConfSectIntHashMap.class)
	public Map<Integer, List<String>> ccIntervalsModeCommands = new HashMap<>();
	// network watcher
	@ConfigOption(path = "networkwatcher.mainthreadnetaccess.warn")
	public boolean networkWatcherWarnMainThreadAcc = true;
	@ConfigOption(path = "networkwatcher.mainthreadnetaccess.interrupt")
	public boolean networkWatcherInterruptMainThreadNetAcc = false;

	@Override
	public File getFile() {
		return GlobalConstants.getMainConfigPath();
	}

}