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

    }

    public void updatePlayer(Player player) {
        this.player = player;
    }

    private Main getOwner() {
        return main;
    }

    public void init(){
        if(player == null || !player.isOnline()){
            return;
        }
        
        // 等待玩家完全生成后再发送显示数据
        if(!player.spawned){
            return;
        }
        
        TipMessage tipMessage;
        tipMessage = (TipMessage) Api.getSendPlayerMessage(player.getName(),player.level.getFolderName(),
                BaseMessage.BaseTypes.TIP);
        if (tipMessage != null) {
            if (tipMessage.isOpen()) {
                sendTip(player, Api.strReplace(tipMessage.getMessage(), player), tipMessage.getShowType());
            }
        }
        // 每次都重新创建子Task，确保使用最新的player引用
        bossTask = new BossBarAllPlayerTask(player);
        bossTask.onRun();
        broadtask = new BroadCastTask(player);
        broadtask.onRun();
        nametask = new NameTagTask(player);
        nametask.onRun();
        scoreTask = new ScoreBoardTask(player,getOwner());
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
