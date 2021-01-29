package tip.utils;

import cn.nukkit.Player;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.defaults.TipMessage;
import tip.tasks.BossBarAllPlayerTask;
import tip.tasks.BroadCastTask;
import tip.tasks.NameTagTask;
import tip.tasks.ScoreBoardTask;

/**
 * @author SmallasWater
 * Create on 2021/1/29 21:49
 * Package tip.utils
 */
public class SendPlayerClass {

    private BossBarAllPlayerTask bossTask;

    private BroadCastTask broadtask;

    private NameTagTask nametask;

    private ScoreBoardTask scoreTask;

    private Player player;
    private Main main;
    public SendPlayerClass(Player player,Main main){
        this.player = player;
        this.main = main;
        init();
    }

    private Main getOwner() {
        return main;
    }

    private void init(){
        if(player == null || !player.isOnline()){
            return;
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
            bossTask = new BossBarAllPlayerTask(player);
            broadtask = new BroadCastTask(player);
            nametask = new NameTagTask(player);
            scoreTask = new ScoreBoardTask(player,getOwner());
        }
        bossTask.onRun();
        broadtask.onRun();
        nametask.onRun();
        scoreTask.onRun();
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
