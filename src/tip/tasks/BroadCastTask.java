package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.BroadcastMessage;
import tip.utils.Api;

/**
 * @author SmallasWater
 */
public class BroadCastTask extends PluginTask<Main> {

    private int i = 0;

    private int time = -1;

    public BroadCastTask(Main owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            BroadcastMessage message;
            message = (BroadcastMessage) Api.getSendPlayerMessage(player.getName(),player.level.getFolderName(),
                    BaseMessage.BaseTypes.BROADCAST);
            if (message != null) {
                if (message.isOpen()) {
                    if (time == -1) {
                        time = message.getTime();
                    }
                    if (time <= 0) {
                        this.i++;
                    }
                    time--;
                    if (this.i >= message.getMessages().size()) {
                        this.i = 0;
                    }
                    String text = message.getMessages().get(this.i);
                    Api api = new Api(text, player);
                    text = api.strReplace();
                    player.sendMessage(text);
                }
            }
        }
    }
}
