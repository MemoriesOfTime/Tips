package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.bossbar.BossBarApi;



/**
 * @author SmallasWater
 */
public class BossBarAllPlayerTask {


    private Player player;
    public BossBarAllPlayerTask(Player player) {
        this.player = player;
    }


    public void onRun() {
//        for(Player player: Server.getInstance().getOnlinePlayers().values()){
            if(player == null || !player.isOnline()){
//                this.cancel();
                return;
            }
            BossBarApi.createBossBar(player);
            if(!Main.getInstance().tasks.containsKey(player)){
                Main.getInstance().tasks.put(player,new BossBarTask());
            }
            BossBarTask task = Main.getInstance().tasks.get(player);
            task.onRun(player);
        }
//    }
}
