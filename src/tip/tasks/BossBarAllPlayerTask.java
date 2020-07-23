package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import tip.Main;
import tip.bossbar.BossBarApi;



/**
 * @author SmallasWater
 */
public class BossBarAllPlayerTask extends Task {



    @Override
    public void onRun(int i) {
        for(Player player: Server.getInstance().getOnlinePlayers().values()){
            BossBarApi.createBossBar(player);
            if(!Main.getInstance().tasks.containsKey(player)){
                Main.getInstance().tasks.put(player,new BossBarTask());
            }
            BossBarTask task = Main.getInstance().tasks.get(player);
            task.onRun(player);
        }
    }
}
