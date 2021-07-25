package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.defaults.ScoreBoardMessage;
import tip.utils.Api;


import java.util.LinkedList;


/**
 * @author 若水
 */
public class ScoreBoardTask {

    private Player player;
    private Main main;

    public ScoreBoardTask(Player player,Main main) {
        this.player = player;
        this.main = main;
    }

    private Main getOwner() {
        return main;
    }

    public void onRun() {
        try{
            Class.forName("de.theamychan.scoreboard.api.ScoreboardAPI");
        }catch (Exception e){
            player.sendActionBar(TextFormat.RED+"计分板未安装\n\n\n\n");
            return;
        }
//        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if (player == null || !player.isOnline()) {
                return;
            }
            ScoreBoardMessage message = (ScoreBoardMessage) Api.getSendPlayerMessage(player.getName(), player.level.getFolderName(), BaseMessage.BaseTypes.SCORE_BOARD);
            if (message == null) {
                if (getOwner().scoreboards.containsKey(player)) {
                    ScoreboardAPI.removeScorebaord(player,
                            Main.getInstance().scoreboards.get(player));
                }
                return;
            } else if (!message.isOpen()) {
                if (getOwner().scoreboards.containsKey(player)) {
                    ScoreboardAPI.removeScorebaord(player,
                            Main.getInstance().scoreboards.get(player));
                }
                return;
            }

            if (player.isOnline()) {
                try {
                    Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
                    String title = message.getTitle();

                    ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR,
                            "dumy", Api.strReplace(title, player));
                    LinkedList<String> list = message.getMessages();
                    for (int line = 0; line < list.size(); line++) {
                        String s = list.get(line);

                        scoreboardDisplay.addLine(Api.strReplace(s,player), line);
                    }
                    try {
                        getOwner().scoreboards.get(player).hideFor(player);
                    } catch (Exception ignored) {
                    }
                    scoreboard.showFor(player);
                    Main.getInstance().scoreboards.put(player, scoreboard);
                } catch (Exception ignored) {
                }
            }
//        }
    }

}
