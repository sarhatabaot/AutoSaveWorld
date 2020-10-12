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


import autosaveworld.core.GlobalConstants;
import net.sarhatabaot.configloader.Config;
import net.sarhatabaot.configloader.ConfigOption;
import net.sarhatabaot.configloader.postload.AstListAppend;
import net.sarhatabaot.configloader.postload.DefaultDestFolder;
import net.sarhatabaot.configloader.transform.ConfSectIntHashMap;
import net.sarhatabaot.configloader.transform.ConfSectStringHashMap;
import net.sarhatabaot.configloader.transform.ListClone;

public class AutoSaveWorldConfig implements Config {

	// some global variables
	@ConfigOption(path = "var.debug")
	public boolean varDebug = false;
	// backup
	@ConfigOption(path = "backup.enabled")
	public boolean backupEnabled = false;
	@ConfigOption(path = "backup.interval")
	public int backupInterval = 60 * 60 * 6;
	@ConfigOption(path = "backup.broadcast")
	public boolean backupBroadcast = true;
	@ConfigOption(path = "backup.rateLimit")
	public long backupRateLimit = -1;
	// localfs backup
	@ConfigOption(path = "backup.localfs.enabled")
	public boolean backupLFSEnabled = true;
	@ConfigOption(path = "backup.localfs.destinationfolders", transform = ListClone.class, postLoad = DefaultDestFolder.class)
	public List<String> backupLFSExtFolders = new ArrayList<>();
	@ConfigOption(path = "backup.localfs.worlds", transform = ListClone.class, postLoad = AstListAppend.class)
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
	@ConfigOption(path = "backup.localfs.excludefiles", transform = ListClone.class)
	public List<String> backupLFSExcludeFiles = new ArrayList<>();
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


	@Override
	public File getFile() {
		return GlobalConstants.getMainConfigPath();
	}

}