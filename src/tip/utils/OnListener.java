package tip.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import tip.Main;
import tip.bossbar.BossBarApi;
import tip.tasks.BossBarTask;
import tip.messages.BaseMessage;
import tip.messages.ChatMessage;

import java.util.List;

/**
 * @author 若水
 */
public class OnListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        BossBarApi.createBossBar(player);
        Server.getInstance().getScheduler().scheduleRepeatingTask(new BossBarTask(player),20);

    }

    private String getBadWorld(String msg,List badWords){
        for (Object badWord : badWords) {
            if (msg.contains((String) badWord)) {
                msg = msg.replace((String) badWord, "*");
            }
        }
        return msg;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(PlayerChatEvent event){
        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        String msg = event.getMessage();

        ChatMessage message = (ChatMessage) BaseMessage.getMessageByTypeAndWorld(player.level.getFolderName()
                ,BaseMessage.CHAT_MESSAGE_TYPE);
        PlayerConfig config = Main.getInstance().getPlayerConfig(player.getName());
        if(config != null){
            if(config.getMessage(player.getLevel().getFolderName(),BaseMessage.CHAT_MESSAGE_TYPE) != null){
                message = (ChatMessage) config.getMessage(player.getLevel().getFolderName(),BaseMessage.CHAT_MESSAGE_TYPE);
            }
        }
        if(message != null) {
            String s = message.getMessage();
            if(!message.isOpen()){
                return;
            }
            if (!"".equals(s)) {
                String send = s.replace("{msg}", msg);
                Api api = new Api(send, player);
                send = api.strReplace();
                if(message.isInWorld()){
                    for(Player player1:event.getPlayer().getLevel().getPlayers().values()){
                        player1.sendMessage(send);
                    }
                }else {
                    Server.getInstance().broadcastMessage(send);
                }
            }
            event.setCancelled();
        }

    }

}
