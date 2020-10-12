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


import autosaveworld.core.GlobalConstants;
import net.sarhatabaot.configloader.Config;
import net.sarhatabaot.configloader.ConfigOption;

public class AutoSaveWorldConfigMSG implements Config {

	@ConfigOption(path = "broadcastbackup.pre")
	public String messageBackupBroadcastPre = "&9Started backup.";
	@ConfigOption(path = "broadcastbackup.post")
	public String messageBackupBroadcastPost = "&9Backup Complete";

	@Override
	public File getFile() {
		return GlobalConstants.getMessageConfigPath();
	}

}
