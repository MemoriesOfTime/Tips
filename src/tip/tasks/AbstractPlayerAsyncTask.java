package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.AsyncTask;

/**
 * @author SmallasWater
 */
public abstract class AbstractPlayerAsyncTask extends AsyncTask {

    private Player player;
    AbstractPlayerAsyncTask(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }


}
