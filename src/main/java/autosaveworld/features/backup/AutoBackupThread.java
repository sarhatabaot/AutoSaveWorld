/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package autosaveworld.features.backup;

import java.text.MessageFormat;
import java.util.ArrayList;

import autosaveworld.config.AutoSaveWorldConfig;
import autosaveworld.core.AutoSaveWorld;
import autosaveworld.core.logging.MessageLogger;
import autosaveworld.features.backup.localfs.LocalFSBackup;
import autosaveworld.utils.threads.IntervalTaskThread;

public class AutoBackupThread extends IntervalTaskThread {

    public AutoBackupThread() {
        super("AutoBackupThread");
    }

    private boolean backupRunning = false;

    public boolean isBackupInProcess() {
        return backupRunning;
    }

    @Override
    public boolean isEnabled() {
        return AutoSaveWorld.getInstance().getMainConfig().backupEnabled;
    }

    @Override
    public int getInterval() {
        return AutoSaveWorld.getInstance().getMainConfig().backupInterval;
    }

    @Override
    public void doTask() throws ThreadDeath {
        backupRunning = true;
        try {
            performBackup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            backupRunning = false;
        }
    }

    public void performBackup() throws Exception {
        AutoSaveWorldConfig config = AutoSaveWorld.getInstance().getMainConfig();

        long timestart = System.currentTimeMillis();

        MessageLogger.broadcast(AutoSaveWorld.getInstance().getMessageConfig().messageBackupBroadcastPre, config.backupBroadcast);

        InputStreamFactory.setRateLimit(config.backupRateLimit);

        ArrayList<Backup> backups = new ArrayList<>();

        if (config.backupLFSEnabled) {
            backups.add(new LocalFSBackup());
        }

        for (Backup backup : backups) {
            MessageLogger.debug(MessageFormat.format("Starting {0} backup", backup.getName()));
            try {
                backup.performBackup();
                MessageLogger.debug(MessageFormat.format("Finished {0} backup", backup.getName()));
            } catch (Throwable t) {
                MessageLogger.exception(MessageFormat.format("Failed {0} backup", backup.getName()), t);
            }
        }

        MessageLogger.debug(MessageFormat.format("Backup took {0} milliseconds", System.currentTimeMillis() - timestart));

        MessageLogger.broadcast(AutoSaveWorld.getInstance().getMessageConfig().messageBackupBroadcastPost, config.backupBroadcast);

    }


}