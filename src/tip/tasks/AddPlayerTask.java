package tip.tasks;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import tip.Main;

/**
 * @author ZXR
 */
public class AddPlayerTask {

    public static void add(BaseTipsRunnable task){
        Main.executor.execute(task);
    }
}
