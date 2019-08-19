package autosaveworld.utils.threads;

import autosaveworld.core.AutoSaveWorld;
import autosaveworld.core.logging.MessageLogger;
import autosaveworld.utils.ReflectionUtils;

/**
 * @author sarhatabaot
 */
public abstract class SIntervalTaskThread extends Thread {

    public SIntervalTaskThread(String threadname) {
        super(AutoSaveWorld.getInstance().getName() + " " + threadname);
    }

    protected volatile boolean run = true;

    public boolean isRunning() {
        return run;
    }

    public void stopThread() {
        run = false;
    }

    @Override
    public void run() {
        MessageLogger.debug(getName()+" started");
        onStart();
        while (run) {
            if (isEnabled()) {
                try {
                    doTask();
                } catch (Throwable t) {
                    MessageLogger.exception("Exception while performing interval task", t);
                    if (t instanceof ThreadDeath) {
                        ReflectionUtils.throwException(t);
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        onStop();
        MessageLogger.debug(getName()+" stopped");
    }

    protected void onStart() {
    }

    protected void onStop() {
    }

    public abstract boolean isEnabled();

    public abstract void doTask() throws ThreadDeath;

}
