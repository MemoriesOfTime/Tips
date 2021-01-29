package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;

import java.util.LinkedHashMap;


/**
 * @author SmallasWater
 */
public class BroadCastTask {


    private Player player;
    private LinkedHashMap<String,BroadCastPlayerTask> taskLinkedHashMap = new LinkedHashMap<>();

    public BroadCastTask(Player player) {
        this.player = player;
    }

    public void onRun() {

            if(!taskLinkedHashMap.containsKey(player.getName())){
                taskLinkedHashMap.put(player.getName(),new BroadCastPlayerTask(player));
            }
            BroadCastPlayerTask task = taskLinkedHashMap.get(player.getName());
            task.onRun();

    }
}
