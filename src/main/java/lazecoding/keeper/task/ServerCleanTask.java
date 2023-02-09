package lazecoding.keeper.task;

import lazecoding.keeper.service.ServerKeepService;
import lazecoding.keeper.util.BeanUtil;

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
        ServerKeepService serverKeepService = BeanUtil.getBean(ServerKeepService.class);
        serverKeepService.clean();
    }
}
