package tip.tasks;

import cn.nukkit.Player;
import tip.Main;
import tip.bossbar.BossBarApi;
import tip.messages.BaseMessage;
import tip.messages.defaults.BossBarMessage;
import tip.utils.Api;
import tip.utils.BossMessageBuilder;

import java.util.LinkedHashMap;

/**
 * @author 若水
 */
public class BossBarTask {

    private final LinkedHashMap<String, BossBarTask.MessageType> type = new LinkedHashMap<>();

    void onRun(Player player) {
        if (player == null) {
            return;
        }
        if (player.isOnline()) {
            if (!Main.getInstance().apis.containsKey(player)) {
                return;
            }
            BossBarMessage message = (BossBarMessage) Api.getSendPlayerMessage(player.getName(), player.getLevel().getFolderName(), BaseMessage.BaseTypes.BOSS_BAR);
            if (message != null) {
                if (message.isOpen()) {
                    if (!type.containsKey(player.getLevel().getFolderName())) {
                        type.put(player.getLevel().getFolderName(), new MessageType());
                    }
                    MessageType m = type.get(player.getLevel().getFolderName());
                    BossMessageBuilder bossMessageBuilder = message.getBuilder();
                    if (m.time == -2) {
                        m.time = bossMessageBuilder.getTime();
                    }
                    if (m.time <= 0) {
                        m.time = bossMessageBuilder.getTime();
                        ++m.i;
                    }
                    if (m.i >= bossMessageBuilder.getStrings().size()) {
                        m.i = 0;
                    }
                    String text = bossMessageBuilder.getStrings().get(m.i);
                    text = Api.strReplace(text, player);
                    BossBarApi.showBoss(player, text, bossMessageBuilder, m.time);
                    m.time--;
                } else {
                    BossBarApi.removeBossBar(player);
                }
            } else {
                BossBarApi.removeBossBar(player);
            }
        }
    }

    private static class MessageType {
        public int i = 0;
        public int time = -2;
    }

}
