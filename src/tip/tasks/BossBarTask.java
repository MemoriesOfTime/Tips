package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import tip.Main;
import tip.bossbar.BossBarApi;
import tip.messages.BaseMessage;
import tip.messages.BossBarMessage;
import tip.utils.Api;
import tip.utils.BossMessageBuilder;
import tip.utils.PlayerConfig;

/**
 * @author 若水
 */
public class BossBarTask extends Task {
    private int i = 0;
    private int time = -2;

    private Player player;
    public BossBarTask(Player player){
        this.player = player;
    }

    @Override
    public void onRun(int i) {
        if(player.isOnline()){
            BossBarMessage message = (BossBarMessage) BaseMessage.getMessageByTypeAndWorld(player.level.getFolderName(),0);
            PlayerConfig config = Main.getInstance().getPlayerConfig(player.getName());
            if(config != null){
                if(config.getMessage(player.getLevel().getFolderName(),0) != null){
                    message = (BossBarMessage) config.getMessage(player.getLevel().getFolderName(),0);
                }
            }
            if(message != null) {
                if (message.isOpen()) {
                    BossMessageBuilder bossMessageBuilder = message.getBuilder();
                    if (time == -2) {
                        time = bossMessageBuilder.getTime();
                    }
                    if (time <= 0) {
                        time = bossMessageBuilder.getTime();
                        this.i++;
                    }
                    time--;
                    if (this.i >= bossMessageBuilder.getStrings().size()) {
                        this.i = 0;
                    }
                    String text = bossMessageBuilder.getStrings().get(this.i);
                    Api api = new Api(text, player);
                    text = api.strReplace();
                    BossBarApi.showBoss(player, text, bossMessageBuilder.isHealth());
                } else {
                    BossBarApi.removeBossBar(player);
                }
            }else{
                BossBarApi.removeBossBar(player);
            }
        }else{
            this.cancel();
        }


    }
}
