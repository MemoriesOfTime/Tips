package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.TipMessage;
import tip.utils.Api;


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
            final  Player player1 = player;
            Server.getInstance().getScheduler().scheduleAsyncTask(Main.getInstance(), new AsyncTask() {
                @Override
                public void onRun() {
                    if(player1 == null || !player.isOnline()){
                        return;
                    }
                    TipMessage tipMessage;
                    tipMessage = (TipMessage) Api.getSendPlayerMessage(player1.getName(),player1.level.getFolderName(),
                            BaseMessage.BaseTypes.TIP);
                    if (tipMessage != null) {
                        if (tipMessage.isOpen()) {
                            Api api = new Api(tipMessage.getMessage(), player1);
                            sendTip(player1, api.strReplace(), tipMessage.getShowType());
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
