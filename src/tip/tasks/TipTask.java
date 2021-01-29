package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.defaults.TipMessage;
import tip.utils.Api;


/**
 * @author 若水
 */
public class TipTask extends PluginTask<Plugin> {


    public TipTask(Plugin owner) {
        super(owner);


    }

    private BossBarAllPlayerTask bossTask;

    private BroadCastTask broadtask;

    private NameTagTask nametask;

    private ScoreBoardTask scoreTask;

    @Override
    public void onRun(int i) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if(player == null || !player.isOnline()){
                continue;
            }
            TipMessage tipMessage;
            tipMessage = (TipMessage) Api.getSendPlayerMessage(player.getName(),player.level.getFolderName(),
                    BaseMessage.BaseTypes.TIP);
            if (tipMessage != null) {
                if (tipMessage.isOpen()) {
                    Api api = new Api(tipMessage.getMessage(), player);
                    sendTip(player, api.strReplace(), tipMessage.getShowType());
                }
            }

            if(bossTask == null || broadtask == null || nametask == null || scoreTask == null) {
                bossTask = new BossBarAllPlayerTask(player, (Main) getOwner());
                broadtask = new BroadCastTask(player, (Main) getOwner());
                nametask = new NameTagTask(player, (Main) getOwner());
                scoreTask = new ScoreBoardTask(player, (Main) getOwner());
            }
            bossTask.onRun(i);
            broadtask.onRun(i);
            nametask.onRun(i);
            scoreTask.onRun(i);
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
