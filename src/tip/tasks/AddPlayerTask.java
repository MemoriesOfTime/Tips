package tip.tasks;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import tip.Main;

/**
 * @author ZXR
 */
public class AddPlayerTask {

    public static void add(Task task, int clock){
        Server.getInstance().getScheduler().scheduleRepeatingTask(Main.getInstance(), task,clock);
    }
}
