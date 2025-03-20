package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import tip.Main;
import tip.utils.SendPlayerClass;

import java.util.concurrent.TimeUnit;

/**
 * @author 若水
 */
public class TipTask extends BaseTipsRunnable {

    private static final Cache<Player, SendPlayerClass> sendPlayerClassCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build();

    private final int sleep;

    public TipTask(Main owner, int sleep) {
        super(owner);
        this.sleep = sleep * 50;
    }

    @Override
    public void run() {
        while (this.owner.isEnabled()) {
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                try {
                    SendPlayerClass sendPlayerClass;
                    if ((sendPlayerClass = sendPlayerClassCache.getIfPresent(player)) == null) {
                        sendPlayerClass = new SendPlayerClass(player, getOwner());
                        sendPlayerClassCache.put(player, sendPlayerClass);
                    }
                    sendPlayerClass.init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
