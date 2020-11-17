package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;

import java.util.LinkedHashMap;


/**
 * @author SmallasWater
 */
public class BroadCastTask extends PluginTask<Main> {


    private Player player;
    private LinkedHashMap<String,BroadCastPlayerTask> taskLinkedHashMap = new LinkedHashMap<>();

    public BroadCastTask(Player player,Main owner) {
        super(owner);
        this.player = player;
    }

    @Override
    public void onRun(int i) {

            if(!taskLinkedHashMap.containsKey(player.getName())){
                taskLinkedHashMap.put(player.getName(),new BroadCastPlayerTask(player));
            }
            BroadCastPlayerTask task = taskLinkedHashMap.get(player.getName());
            task.onRun();

    }
}
