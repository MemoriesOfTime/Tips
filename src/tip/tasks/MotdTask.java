package tip.tasks;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import tip.Main;

import java.util.Random;

/**
 * @author SmallasWater
 * Create on 2020/12/2 19:06
 * Package tip.tasks
 */
public class MotdTask extends PluginTask<Main> {
    public MotdTask(Main owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        int online = owner.getServer().getOnlinePlayers().size();
        int maxPlayer = owner.getServer().getMaxPlayers();
        String[] strings = new String[]{"§c","§6","§e","§a","§b","§9","§d","§7","§5"};
        owner.getServer().getNetwork().setName(TextFormat.colorize('&',
                owner.getMotd()
                        .replace("{online}",online+"")
                        .replace("{maxplayer}",maxPlayer+"")
                        .replace("{color}",strings[new Random().nextInt(strings.length)])
                        .replace("{version}",owner.getServer().getVersion()))
        );
    }
}
