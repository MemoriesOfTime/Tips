package tip.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import tip.Main;
import tip.bossbar.BossBarApi;
import tip.messages.BaseMessage;
import tip.messages.defaults.ChatMessage;

import java.util.List;

/**
 * @author 若水
 */
public class OnListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Main.getInstance().loadPlayerConfig(event.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerConfig config = Main.getInstance().getPlayerConfig(player.getName());
        if (config != null) {
            config.save();
            Main.getInstance().getPlayerConfigs().remove(config);
        }

        BossBarApi.removeBossBar(player);
        Main.getInstance().apis.remove(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        BossBarApi.removeBossBar(event.getPlayer());
    }

    private String getBadWorld(String msg, List badWords) {
        for (Object badWord : badWords) {
            if (msg.contains((String) badWord)) {
                msg = msg.replace((String) badWord, "*");
            }
        }
        return msg;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        String msg = event.getMessage();

        ChatMessage message = (ChatMessage) Main.getInstance().getShowMessages().getMessageByTypeAndWorld(player.level.getFolderName()
                , BaseMessage.CHAT_MESSAGE_TYPE);
        PlayerConfig config = Main.getInstance().getPlayerConfig(player.getName());
        if (config != null) {
            if (config.getMessage(player.getLevel().getFolderName(), BaseMessage.CHAT_MESSAGE_TYPE) != null) {
                message = (ChatMessage) config.getMessage(player.getLevel().getFolderName(), BaseMessage.CHAT_MESSAGE_TYPE);
            }
        }
        if (message != null) {
            String s = message.getMessage();
            if (!message.isOpen()) {
                return;
            }
            if (!"".equals(s)) {
                String send = Api.strReplace(s, player).replace("{msg}", msg);
                if (message.isInWorld()) {
                    for (Player player1 : player.getLevel().getPlayers().values()) {
                        player1.sendMessage(send);
                    }
                } else {
                    Server.getInstance().broadcastMessage(send);
                }
            }
            event.setCancelled();
        }

    }

}
