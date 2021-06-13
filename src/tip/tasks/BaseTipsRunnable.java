package tip.tasks;

import cn.nukkit.plugin.Plugin;
import tip.Main;

/**
 * @author SmallasWater
 * Create on 2021/2/26 14:47
 * Package tip.tasks
 */
public abstract class BaseTipsRunnable implements Runnable{
    public Main owner;

    public BaseTipsRunnable(Main owner){
        this.owner = owner;
    }

    public Main getOwner() {
        return owner;
    }
}
