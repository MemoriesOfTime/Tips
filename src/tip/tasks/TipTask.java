package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.NameTagMessage;
import tip.messages.TipMessage;
import tip.utils.Api;
import tip.utils.PlayerConfig;


/**
 * @author 若水
 */
public class TipTask extends PluginTask<Plugin> {


    public TipTask(Plugin owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if(player == null){
                continue;
            }
            Server.getInstance().getScheduler().scheduleAsyncTask(Main.getInstance(), new AsyncTask() {
                @Override
                public void onRun() {
                    if (player.isOnline()) {
                        TipMessage tipMessage;
                        tipMessage = (TipMessage) BaseMessage.getMessageByTypeAndWorld(player.level.getFolderName(),
                                BaseMessage.TIP_MESSAGE_TYPE);
                        PlayerConfig config = Main.getInstance().getPlayerConfig(player.getName());
                        if (config != null) {
                            if (config.getMessage(player.getLevel().getFolderName(), BaseMessage.TIP_MESSAGE_TYPE) != null) {
                                tipMessage = (TipMessage) config.getMessage(player.getLevel().getFolderName(), BaseMessage.TIP_MESSAGE_TYPE);
                            }
                        }
                        if (tipMessage != null) {
                            if (tipMessage.isOpen()) {
                                Api api = new Api(tipMessage.getMessage(), player);
                                sendTip(player, api.strReplace(), tipMessage.getType());
                            }
                        }
                    }
                }

            });
        }

    }



    private void sendTip(Player player,String tip,int type){
        switch (type){
            case TipMessage.POPUP:
                player.sendPopup(tip);
                break;
            case TipMessage.ACTION:
                player.sendActionBar(tip);
                break;
            default:
                player.sendTip(tip);
                break;
        }
    }
}
