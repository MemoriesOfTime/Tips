package tip.bossbar;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import tip.Main;

public class BossBarTask extends Task {
    private Player player;
    public BossBarTask(Player player){
        this.player = player;
    }

    @Override
    public void onRun(int i) {
        if(player.isOnline()){
            String text = Main.getInstance().getConfig().getString("Boss血条显示");
            text = Main.strReplace(text,player);
            BossAPI.showBoss(player,text);
        }else{
            this.cancel();
        }
    }
}
