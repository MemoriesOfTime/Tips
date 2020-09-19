package tip.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.ChatMessage;

import java.util.List;

/**
 * @author 若水
 */
public class OnListener implements Listener {


    private String getBadWorld(String msg,List<String> badWords){
        for (Object badWord : badWords) {
            if (msg.contains((String) badWord)) {
                msg = msg.replace((String) badWord, "*");
            }
        }
        return msg;
    }


    @EventHandler
    public void onChat(PlayerChatEvent event){

        Player player = event.getPlayer();
        String msg = event.getMessage();
        ChatMessage message = (ChatMessage) Api.getSendPlayerMessage(player.getName(),player.getLevel().getFolderName(), BaseMessage.BaseTypes.CHAT_MESSAGE);
        if(message != null) {
            String s = message.getMessage();
            if(!message.isOpen()){
                return;
            }
            if (!"".equals(s)) {
                Api api = new Api(s, player);
                s = api.strReplace().replace("{msg}",msg);
                if(message.isInWorld()){
                    for(Player player1:event.getPlayer().getLevel().getPlayers().values()){
                        player1.sendMessage(s);
                    }
                }else {
                    Server.getInstance().broadcastMessage(s);
                }
            }
            event.setCancelled();
        }

    }

}
