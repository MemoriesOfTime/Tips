package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.scoreboard.Scoreboard;
import cn.nukkit.scoreboard.Scoreboard.DisplaySlot;
import cn.nukkit.scoreboard.Scoreboard.SortOrder;
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
    private Scoreboard scoreboard = null;
    private String title = null;

    public ScoreBoardTask(Player player, Main main) {
        this.player = player;
        this.main = main;
    }

    private Main getOwner() {
        return main;
    }

    public void onRun() {
        if (player == null || !player.isOnline()) {
            return;
        }
        ScoreBoardMessage message = (ScoreBoardMessage) Api.getSendPlayerMessage(player.getName(), player.level.getFolderName(), BaseMessage.BaseTypes.SCORE_BOARD);
        if (message == null || !message.isOpen()) {
            close();
            return;
        }

        String title = Api.strReplace(message.getTitle(), player);
        LinkedList<String> messages = message.getMessages();
        ArrayList<String> list = new ArrayList<>();
        for (String ms : messages) {
            list.add(Api.strReplace(ms, player));
        }
        if (this.scoreboard == null || !title.equals(this.title)) {
            close();
            this.scoreboard = new Scoreboard(title, SortOrder.ASCENDING, DisplaySlot.SIDEBAR);
            this.title = title;
            this.scoreboard.showTo(player);
        }

        boolean changed = this.scoreboard.getScores().size() != list.size();
        if (!changed) {
            int index = 0;
            for (String line : list) {
                Scoreboard.Score score = this.scoreboard.getScores().get(line);
                if (score == null || score.getScore() != index) {
                    changed = true;
                    break;
                }
                index++;
            }
        }
        if (changed) {
            this.scoreboard.clear();
            int index = 0;
            for (String line : list) {
                this.scoreboard.setScore(line, index);
                index++;
            }
        }
        Main.getInstance().scoreboards.add(player);
    }

    private void close() {
        if (this.scoreboard != null) {
            this.scoreboard.hideFor(player);
            this.scoreboard = null;
        }
        this.title = null;
        getOwner().scoreboards.remove(player);
    }

}
