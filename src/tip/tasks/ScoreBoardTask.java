package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.Task;
import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.ScoreBoardMessage;
import tip.utils.Api;
import tip.utils.PlayerConfig;


import java.util.LinkedList;


/**
 * @author 若水
 */
public class ScoreBoardTask extends Task{



    @Override
    public void onRun(int i) {
        for(Player player:Server.getInstance().getOnlinePlayers().values()) {
            if(player == null){
                continue;
            }
            Server.getInstance().getScheduler().scheduleAsyncTask(Main.getInstance(), new AsyncTask() {
                @Override
                public void onRun() {
                    ScoreBoardMessage message = (ScoreBoardMessage) BaseMessage.getMessageByTypeAndWorld(player.level.getFolderName(), BaseMessage.SCOREBOARD_TYPE);
                    PlayerConfig config = Main.getInstance().getPlayerConfig(player.getName());
                    if (config != null) {
                        if (config.getMessage(player.getLevel().getFolderName(), 3) != null) {
                            message = (ScoreBoardMessage) config.getMessage(player.getLevel().getFolderName(), BaseMessage.SCOREBOARD_TYPE);
                        }
                    }
                    if (message == null) {
                        if (Main.getInstance().scoreboards.containsKey(player)) {
                            ScoreboardAPI.removeScorebaord(player,
                                    Main.getInstance().scoreboards.get(player));
                        }
                        return;
                    } else if (!message.isOpen()) {
                        if (Main.getInstance().scoreboards.containsKey(player)) {
                            ScoreboardAPI.removeScorebaord(player,
                                    Main.getInstance().scoreboards.get(player));
                        }
                        return;
                    }

                    if (player.isOnline()) {
                        try {
                            Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
                            ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR,
                                    "dumy", message.getTitle());
                            LinkedList<String> list = message.getMessages();
                            for (int line = 0; line < list.size(); line++) {
                                String s = list.get(line);
                                Api api = new Api(s, player);
                                s = api.strReplace();
                                scoreboardDisplay.addLine(s, line);
                            }
                            try {
                                Main.getInstance().scoreboards.get(player).hideFor(player);
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
