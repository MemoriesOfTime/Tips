package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.NameTagMessage;
import tip.utils.Api;
import tip.utils.PlayerConfig;

public class NameTagTask extends PluginTask<Main> {
    public NameTagTask(Main owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if(player == null){
                continue;
            }
            Server.getInstance().getScheduler().scheduleAsyncTask(getOwner(), new AsyncTask() {
                @Override
                public void onRun() {
                    if (player.isOnline()) {
                        NameTagMessage nameTagMessage;
                        nameTagMessage = (NameTagMessage) BaseMessage.getMessageByTypeAndWorld(player.level.getFolderName(),
                                BaseMessage.NAME_TAG_TYPE);
                        PlayerConfig config = owner.getPlayerConfig(player.getName());
                        if (config != null) {
                            if (config.getMessage(player.getLevel().getFolderName(), BaseMessage.NAME_TAG_TYPE) != null) {
                                nameTagMessage = (NameTagMessage) config.getMessage(player.getLevel().getFolderName(), BaseMessage.NAME_TAG_TYPE);
                            }
                        }
                        if (nameTagMessage != null) {
                            if (nameTagMessage.isOpen()) {
                                Api api1 = new Api(nameTagMessage.getMessage(), player);
                                String hand = api1.strReplace();
                                player.setNameTag(hand);
                            }
                        }
                    }
                }
            });
        }
    }
}
