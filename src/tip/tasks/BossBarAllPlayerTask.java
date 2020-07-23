package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
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
            final  Player player1 = player;
            owner.getServer().getScheduler().scheduleAsyncTask(owner, new AsyncTask() {
                @Override
                public void onRun() {
                    if(player1 == null || !player.isOnline()){
                        return;
                    }
                    BossBarApi.createBossBar(player1);
                    if(!Main.getInstance().tasks.containsKey(player1)){
                        Main.getInstance().tasks.put(player1,new BossBarTask());
                    }
                    BossBarTask task = Main.getInstance().tasks.get(player1);
                    task.onRun(player1);
                }
            });
        }
    }
}
