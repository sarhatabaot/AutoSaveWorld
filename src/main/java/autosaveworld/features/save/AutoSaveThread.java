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

package autosaveworld.features.save;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import autosaveworld.utils.SchedulerUtils;
import autosaveworld.utils.threads.IntervalTaskThread;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import autosaveworld.core.AutoSaveWorld;
import autosaveworld.core.logging.MessageLogger;
import autosaveworld.utils.BukkitUtils;
import autosaveworld.utils.CollectionsUtils;
import autosaveworld.utils.ReflectionUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveThread extends IntervalTaskThread {

    public AutoSaveThread() {
        super("AutoSaveThread");
    }

    @Override
    public void onStart() {
        //disable bukkit built-in autosave
        try {
            Server server = Bukkit.getServer();
            Object minecraftserver = ReflectionUtils.getField(server.getClass(), "console").get(server);
            ReflectionUtils.getField(minecraftserver.getClass(), "autosavePeriod").set(minecraftserver, 0);
        } catch (Exception e) {
            AutoSaveWorld.getInstance().getLogger().warning(e.getMessage());
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getInterval() {
        return AutoSaveWorld.getInstance().getMainConfig().saveInterval;
    }

    @Override
    public void doTask() {
        performSave();
    }

    public void performSaveNow() {
        MessageLogger.broadcast(AutoSaveWorld.getInstance().getMessageConfig().messageSaveBroadcastPre, AutoSaveWorld.getInstance().getMainConfig().saveBroadcast);

        MessageLogger.debug("Saving players");
        Bukkit.savePlayers();
        MessageLogger.debug("Saved Players");
        MessageLogger.debug("Saving worlds");
        for (World w : Bukkit.getWorlds()) {
            saveWorld(w);
        }
        MessageLogger.debug("Saved Worlds");

        MessageLogger.broadcast(AutoSaveWorld.getInstance().getMessageConfig().messageSaveBroadcastPost, AutoSaveWorld.getInstance().getMainConfig().saveBroadcast);
    }


    private void savePlayers(){
        MessageLogger.debug("Saving players");
        for (Player player : Bukkit.getOnlinePlayers()) {
            SchedulerUtils.callSyncTaskAndWait(player::saveData);
            //new SavePlayersWaitRunnable(playersPart).runTask(AutoSaveWorld.getInstance());
        }
        MessageLogger.debug("Saved Players");
    }

    private void saveWorlds(){
        MessageLogger.debug("Saving worlds");
        for (final World world : Bukkit.getWorlds()) {
            SchedulerUtils.callSyncTaskAndWait(() -> saveWorld(world));
            //new SaveWorldsWaitRunnable(world).runTask(AutoSaveWorld.getInstance());
        }
        MessageLogger.debug("Saved Worlds");
    }

    public void performSave() {
        MessageLogger.broadcast(AutoSaveWorld.getInstance().getMessageConfig().messageSaveBroadcastPre, AutoSaveWorld.getInstance().getMainConfig().saveBroadcast);

        // Save the players
        savePlayers();

        // Save the worlds
        saveWorlds();
        MessageLogger.broadcast(AutoSaveWorld.getInstance().getMessageConfig().messageSaveBroadcastPost, AutoSaveWorld.getInstance().getMainConfig().saveBroadcast);
    }

    private void saveWorld(World world) {
        if (!world.isAutoSave()) return;

        if (AutoSaveWorld.getInstance().getMainConfig().saveDisableStructureSaving && needSaveWorkAround()) {
            saveWorldDoNoSaveStructureInfo(world);
        } else {
            saveWorldNormal(world);
        }
    }

    private boolean needSaveWorkAround() {
        return true;
    }

    private void saveWorldNormal(World world) {
        world.save();
    }

    private void saveWorldDoNoSaveStructureInfo(World world) {
        try {
            // get worldserver, dataManager, chunkProvider, worldData
            Object worldserver = getNMSWorld(world);
            Object dataManager = ReflectionUtils.getField(worldserver.getClass(), NMSNames.getDataManagerFieldName()).get(worldserver);
            Object chunkProvider = ReflectionUtils.getField(worldserver.getClass(), NMSNames.getChunkProviderFieldName()).get(worldserver);
            Object worldData = ReflectionUtils.getField(worldserver.getClass(), NMSNames.getWorldDataFieldName()).get(worldserver);
            // invoke check session
            ReflectionUtils.getMethod(dataManager.getClass(), NMSNames.getCheckSessionMethodName(), 0).invoke(dataManager);
            // invoke saveWorldData
            ReflectionUtils.getMethod(dataManager.getClass(), NMSNames.getSaveWorldDataMethodName(), 2).invoke(dataManager, worldData, null);
            // invoke saveChunks
            try {
                ReflectionUtils.getMethod(chunkProvider.getClass(), NMSNames.getSaveChunksMethodName(), 2).invoke(chunkProvider, true, null);
            } catch (RuntimeException e) {
                ReflectionUtils.getMethod(chunkProvider.getClass(), "a", 1).invoke(chunkProvider, true);
            }
        } catch (Exception e) {
            MessageLogger.exception("Failed to workaround stucture saving, saving world using normal methods", e);
            saveWorldNormal(world);
        }
    }

    private Object getNMSWorld(World world) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return ReflectionUtils.getMethod(world.getClass(), "getHandle", 0).invoke(world);
    }

    private class SavePlayersWaitRunnable extends BukkitRunnable {
        private final Collection<Player> playersPart;

        public SavePlayersWaitRunnable(final Collection<Player> playersPart) {
            this.playersPart = playersPart;
        }

        @Override
        public void run() {
            for (Player player : playersPart) {
                player.saveData();
            }
        }
    }

    private class SaveWorldsWaitRunnable extends BukkitRunnable {
        private World world;

        public SaveWorldsWaitRunnable(final World world) {
            this.world = world;
        }

        @Override
        public void run() {
            saveWorld(world);
        }
    }

}
