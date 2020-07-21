package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.bossbar.BossBarApi;



/**
 * @author SmallasWater
 */
public class BossBarAllPlayerTask extends PluginTask<Main> {


    public BossBarAllPlayerTask(Main owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for(Player player: Server.getInstance().getOnlinePlayers().values()) {
            if (player == null || !player.isOnline()) continue;
            BossBarApi.createBossBar(player);
            if(!owner.tasks.containsKey(player)){
                owner.tasks.put(player,new BossBarTask());
            }
            BossBarTask task = owner.tasks.get(player);
            task.onRun(player);
        }
    }
}
