package tip;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import gt.creeperface.nukkit.scoreboardapi.scoreboard.*;

import java.util.List;

class ScoreBoardTask extends Task{

    private Player player;
    ScoreBoardTask(Player player) {
        this.player = player;

    }

    @Override
    public void onRun(int i) {
        if(!player.isOnline()) {
            return;
        }
        if(!Main.getInstance().isCanShow()) {
            return;
        }
        FakeScoreboard fakeScoreboard = new FakeScoreboard();
        fakeScoreboard.objective = ScoreBoard.getDisplayObjective(player);
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int i) {
                fakeScoreboard.addPlayer(player);
            }
        },30);
        

        Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                if(!player.isOnline()){
                    this.cancel();
                    return;
                }
                fakeScoreboard.objective = ScoreBoard.getDisplayObjective(player);
                fakeScoreboard.update();
            }
        },20);
    }


}
class ScoreBoard{

    static DisplayObjective getDisplayObjective(Player player){
        Objective objective = new Objective("tips",new ObjectiveCriteria("dummy",true));
        DisplayObjective displayObjective = new DisplayObjective(objective, ObjectiveSortOrder.ASCENDING,ObjectiveDisplaySlot.SIDEBAR);
        objective.setDisplayName(Main.getInstance().getTitle());
        List list = Main.getInstance().getMessages();
        for (int line = 0;line < list.size();line++){
            String s = (String)list.get(line);
            s = Main.strReplace(s,player);
            objective.setScore(line,s,line);
        }
        return displayObjective;
    }
}
