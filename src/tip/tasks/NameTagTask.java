package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.NameTagMessage;
import tip.utils.Api;

/**
 * @author SmallasWater
 */
public class NameTagTask extends PluginTask<Main> {
    public NameTagTask(Main owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            final  Player player1 = player;
            Server.getInstance().getScheduler().scheduleAsyncTask(getOwner(), new AsyncTask() {
                @Override
                public void onRun() {
                    if(player1 == null || !player.isOnline()){
                        return;
                    }
                    NameTagMessage nameTagMessage;
                    nameTagMessage = (NameTagMessage) Api.getSendPlayerMessage(player1.getName(),player1.level.getFolderName(),
                            BaseMessage.BaseTypes.NAME_TAG);
                    if (nameTagMessage != null) {
                        if (nameTagMessage.isOpen()) {
                            Api api1 = new Api(nameTagMessage.getMessage(), player1);
                            String hand = api1.strReplace();
                            player1.setNameTag(hand);
                        }
                    }
                }

            });
        }
    }
}
