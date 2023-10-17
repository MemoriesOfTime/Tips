package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.defaults.NameTagMessage;
import tip.utils.Api;

/**
 * @author SmallasWater
 */
public class NameTagTask {
    private Player player;
    public NameTagTask(Player player) {

        this.player = player;
    }

    public void onRun() {
        if(player == null || !player.isOnline()){
            return;
        }
        NameTagMessage nameTagMessage = (NameTagMessage) Api.getSendPlayerMessage(player.getName(),player.level.getFolderName(), BaseMessage.BaseTypes.NAME_TAG);
        if (nameTagMessage != null) {
            if (nameTagMessage.isOpen()) {
                player.setNameTag(Api.strReplace(nameTagMessage.getMessage(),player));
            }
        }
    }
}
