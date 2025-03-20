package tip.tasks;

import cn.nukkit.Player;
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
        if (player == null || !player.isOnline()) {
            return;
        }
        BossBarApi.createBossBar(player);
        BossBarTask task = Main.getInstance().tasks.getIfPresent(player);
        if (task == null) {
            task = new BossBarTask();
            Main.getInstance().tasks.put(player, task);
        }
        task.onRun(player);
    }
}
