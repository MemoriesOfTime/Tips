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
        if (player == null || !player.isOnline() || !player.spawned) {
            return;
        }
        // 检查是否需要重新创建BossBar（跨服后apis中可能没有当前player）
        if (!Main.getInstance().apis.containsKey(player)) {
            BossBarApi.createBossBar(player);
        }
        BossBarTask task = Main.getInstance().tasks.getIfPresent(player);
        if (task == null) {
            task = new BossBarTask();
            Main.getInstance().tasks.put(player, task);
        }
        task.onRun(player);
    }
}
