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
public class NameTagTask extends PluginTask<Main> {
    private Player player;
    public NameTagTask(Player player,Main owner) {
        super(owner);
        this.player = player;
    }

    @Override
    public void onRun(int i) {
        if(player == null || !player.isOnline()){
            this.cancel();
            return;
        }
        NameTagMessage nameTagMessage;
        nameTagMessage = (NameTagMessage) Api.getSendPlayerMessage(player.getName(),player.level.getFolderName(),
                BaseMessage.BaseTypes.NAME_TAG);
        if (nameTagMessage != null) {
            if (nameTagMessage.isOpen()) {
                Api api1 = new Api(nameTagMessage.getMessage(), player);
                String hand = api1.strReplace();
                player.setNameTag(hand);
            }
        }
    }
}
