package lazecoding.keeper.task;

import java.io.Serializable;

/**
 * ServerCleanTask
 *
 * @author lazecoding
 */
public class ServerCleanTask implements Runnable, Serializable {

    private static final long serialVersionUID = -1L;

    @Override
    public void run() {
        System.out.println("RScheduledExecutorService doRunnable " + System.currentTimeMillis());
    }
}
