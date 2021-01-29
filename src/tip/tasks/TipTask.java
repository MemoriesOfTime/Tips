package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;
import tip.Main;
import tip.utils.SendPlayerClass;


/**
 * @author 若水
 */
public class TipTask extends PluginTask<Plugin> {
    public TipTask(Plugin owner) {
        super(owner);
    }


    @Override
    public void onRun(int i) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            new SendPlayerClass(player, (Main) getOwner());
        }
    }





}
