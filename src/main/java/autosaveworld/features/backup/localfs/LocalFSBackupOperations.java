/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package autosaveworld.features.backup.localfs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.lingala.zip4j.ZipFile;
import org.bukkit.World;

import autosaveworld.core.GlobalConstants;
import autosaveworld.core.logging.MessageLogger;
import autosaveworld.features.backup.BackupUtils;
import autosaveworld.features.backup.utils.ZipUtils;
import autosaveworld.utils.FileUtils;

public class LocalFSBackupOperations {

    private boolean zip;
    private String extpath;
    private List<String> excludefolders;
    private List<File> excludeFileFolders;
    private List<String> exclude;

    public LocalFSBackupOperations(boolean zip, String extpath, List<String> excludefolders, List<String> exclude) {
        this.zip = zip;
        this.extpath = extpath;
        this.excludefolders = excludefolders;
        this.excludeFileFolders = convertStringToFiles(excludefolders);
        this.exclude = exclude;
    }

    public void backupWorld(World world, int maxBackupsCount, String latestbackuptimestamp) {
        MessageLogger.debug("Started backup on world= " + world.getWorldFolder().getName());
        try {
            File fromfolder = world.getWorldFolder().getAbsoluteFile();
            String destfolder = extpath + File.separator + "backups" + File.separator + "worlds" + File.separator + world.getWorldFolder().getName();
            backupFolder(fromfolder, destfolder, maxBackupsCount, latestbackuptimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MessageLogger.debug("Finished backup on world= " + world.getWorldFolder().getName());
    }

    public void backupPlugins(int maxBackupsCount, String latestbackuptimestamp) {
        try {
            String destfolder = extpath + File.separator + "backups" + File.separator + "plugins";
            backupFolder(GlobalConstants.getPluginsFolder(), destfolder, maxBackupsCount, latestbackuptimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backupOtherFolders(List<String> folders, int maxBackupsCount, String latestbackuptimestamp) {
        for (String folder : folders) {
            MessageLogger.debug("Backuping folder " + folder);
            try {
                File fromfolder = new File(folder).getAbsoluteFile();
                String destfolder = extpath + File.separator + "backups" + File.separator + "others" + File.separator + fromfolder.getName();
                backupFolder(fromfolder, destfolder, maxBackupsCount, latestbackuptimestamp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            MessageLogger.debug("Backuped folder " + folder);
        }
    }

    private void backupFolder(File fromfolder, String destfolder, int maxBackupsCount, String latestbackuptimestamp) throws IOException {
        String[] folders = FileUtils.safeList(new File(destfolder));
        if ((maxBackupsCount != 0) && new File(destfolder).exists() && (folders.length >= maxBackupsCount)) {
            String oldestBackupName = BackupUtils.findOldestBackupName(folders);
            if (oldestBackupName != null) {
                File oldestBackup = new File(destfolder, oldestBackupName);
                FileUtils.deleteDirectory(oldestBackup);
            }
        }

        String bfolder = destfolder + File.separator + latestbackuptimestamp;
        if (!zip) {
            LocalFSUtils.copyDirectory(fromfolder, new File(bfolder), excludefolders);
        } else {
            ZipFile zipFile = new ZipFile(bfolder + ".zip");
            for(File file: fromfolder.listFiles()){
                if(BackupUtils.isFolderExcluded(excludefolders, file.getAbsolutePath()) || BackupUtils.isFileExcluded(exclude, file.getAbsolutePath()))
                    continue;
                if(file.isDirectory())
                    zipFile.addFolder(file);
                else
                    zipFile.addFile(file);
            }
            //ZipUtils.zipFolder(fromfolder, new File(bfolder + ".zip"), excludefolders);
        }
    }

    public List<File> convertStringToFiles(List<String> list){
        List<File> fileList = new ArrayList<>();
        for(String string: list){
            fileList.add(new File(string));
        }
        return fileList;
    }


}