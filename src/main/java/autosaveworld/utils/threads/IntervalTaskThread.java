package autosaveworld.utils.threads;

import autosaveworld.core.logging.MessageLogger;

public abstract class IntervalTaskThread extends SIntervalTaskThread {

    public IntervalTaskThread(String threadname) {
        super(threadname);
    }

    private int currentTick = 0;

    protected volatile boolean runnow = false;

    public void triggerTaskRun() {
        runnow = true;
    }

    @Override
    public void run() {
        MessageLogger.debug(getName()+" started");
        onStart();
        while (run) {
            int interval = getInterval();
            boolean shouldrun = runnow;
            if ((currentTick > interval) || shouldrun) {
                runnow = false;
                currentTick = 0;
                if (isEnabled() || shouldrun) {
                    try {
                        doTask();
                    } catch (Exception t) {
                        MessageLogger.exception("Exception while performing interval task", t);
                    }
                }
            }
            currentTick++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        onStop();
        MessageLogger.debug(getName()+" stopped");
    }

    public abstract int getInterval();

}