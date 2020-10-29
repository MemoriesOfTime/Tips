package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
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
public class ScoreBoardTask extends PluginTask<Main> {


    public ScoreBoardTask(Main owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for(Player player:Server.getInstance().getOnlinePlayers().values()) {
            Server.getInstance().getScheduler().scheduleAsyncTask(Main.getInstance(), new AbstractPlayerAsyncTask(player) {
                @Override
                public void onRun() {
                    if(player == null || !player.isOnline()){
                        return;
                    }
                    ScoreBoardMessage message = (ScoreBoardMessage) Api.getSendPlayerMessage(player.getName(),player.level.getFolderName(), BaseMessage.BaseTypes.SCORE_BOARD);
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
                            Api api = new Api(title,player);
                            ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR,
                                    "dumy", api.strReplace());
                            LinkedList<String> list = message.getMessages();
                            for (int line = 0; line < list.size(); line++) {
                                String s = list.get(line);
                                api = new Api(s, player);
                                s = api.strReplace();
                                scoreboardDisplay.addLine(s, line);
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
                }
            });
        }
    }

}
