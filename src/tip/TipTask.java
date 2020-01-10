package tip;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

/**
 * @author 若水
 */
public class TipTask extends Task {

    @Override
    public void onRun(int i) {

        for(Player player: Server.getInstance().getOnlinePlayers().values()) {

            String levelName = player.getLevel().getFolderName();
            String tip = Main.getInstance().getConfig().getString("底部");
            if(Main.getInstance().getConfig().getBoolean("是否更改底部",true)){
                if(!"".equals(tip)){
                    if(!Main.getInstance().getConfig().getStringList("限制显示地图").contains(levelName)){
                        sendTip(player,Main.strReplace(tip,player),Main.getInstance().getConfig().getInt("底部显示类型",0));
                    }
                }
            }
            if(Main.getInstance().getConfig().getBoolean("是否更改头部",true)){
                String hand = Main.strReplace(Main.getInstance().getConfig().getString("头部更改"),player);
                if(!"".equals(hand)){
                    player.setNameTag(hand);
                }
            }
        }
    }

    private void sendTip(Player player,String tip,int type){
        switch (type){
            case 1:
                player.sendPopup(tip);
                break;
            case 2:
                player.sendActionBar(tip);
                break;
            default:
                player.sendTip(tip);
                break;
        }
    }
}
