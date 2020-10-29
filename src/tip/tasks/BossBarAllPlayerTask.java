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
        for(Player player: Server.getInstance().getOnlinePlayers().values()){
            owner.getServer().getScheduler().scheduleAsyncTask(owner, new AbstractPlayerAsyncTask(player) {
                @Override
                public void onRun() {
                    if(player == null || !player.isOnline()){
                        return;
                    }
                    BossBarApi.createBossBar(player);
                    if(!Main.getInstance().tasks.containsKey(player)){
                        Main.getInstance().tasks.put(player,new BossBarTask());
                    }
                    BossBarTask task = Main.getInstance().tasks.get(player);
                    task.onRun(player);
                }
            });
        }
    }
}
