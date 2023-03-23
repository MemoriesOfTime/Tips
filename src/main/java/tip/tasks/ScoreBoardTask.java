package tip.tasks;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.scoreboard.ScoreboardUtil;
import cn.lanink.gamecore.scoreboard.base.IScoreboard;
import cn.lanink.gamecore.scoreboard.ltname.SimpleScoreboard;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.defaults.ScoreBoardMessage;
import tip.utils.Api;


import java.util.ArrayList;
import java.util.LinkedList;


/**
 * @author 若水
 */
public class ScoreBoardTask {

    private final Player player;
    private final Main main;
    private IScoreboard scoreboard = null;

    public ScoreBoardTask(Player player,Main main) {
        this.player = player;
        this.main = main;

        try {
            Class.forName("cn.lanink.gamecore.scoreboard.ScoreboardUtil");
            scoreboard = ScoreboardUtil.getScoreboard();
        } catch (Exception ignored) {

        }
    }

    private Main getOwner() {
        return main;
    }

    public void onRun() {
        if (this.scoreboard == null) {
            return;
        }
//        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if (player == null || !player.isOnline()) {
                return;
            }
            ScoreBoardMessage message = (ScoreBoardMessage) Api.getSendPlayerMessage(player.getName(), player.level.getFolderName(), BaseMessage.BaseTypes.SCORE_BOARD);
            if (message == null || !message.isOpen()) {
                if (getOwner().scoreboards.contains(player)) {
                    this.scoreboard.closeScoreboard(player);
                    getOwner().scoreboards.remove(player);
                }
                return;
            }

            if (player.isOnline()) {
                try {
                    String title = Api.strReplace(message.getTitle(), player);
                    ArrayList<String> list = new ArrayList<>();
                    for (String ms : message.getMessages()) {
                        list.add(Api.strReplace(ms, player));
                    }
                    this.scoreboard.showScoreboard(player, title, list);
                    Main.getInstance().scoreboards.add(player);
                } catch (Exception ignored) {
                }
            }
//        }
    }

}
