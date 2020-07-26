package tip.tasks;

import cn.nukkit.Player;
import tip.Main;
import tip.bossbar.BossBarApi;
import tip.messages.BaseMessage;
import tip.messages.BossBarMessage;
import tip.utils.Api;
import tip.utils.BossMessageBuilder;

/**
 * @author 若水
 */
public class BossBarTask {
    private int i = 0;
    private int time = -2;


    void onRun(Player player) {
        if(player == null){
            return;
        }
        if(player.isOnline()){
            if(!Main.getInstance().apis.containsKey(player)){
                return;
            }
            BossBarMessage message = (BossBarMessage) Api.getSendPlayerMessage(player.getName(),player.getLevel().getFolderName(), BaseMessage.BaseTypes.BOSS_BAR);
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
        }


    }
}
