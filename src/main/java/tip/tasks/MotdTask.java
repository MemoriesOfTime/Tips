package tip.tasks;


import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import com.smallaswater.serverinfo.ServerInfoMainClass;
import com.smallaswater.serverinfo.servers.ServerInfo;
import tip.Main;

import java.util.Random;


/**
 * @author SmallasWater
 * Create on 2020/12/2 19:06
 * Package tip.tasks
 */
public class MotdTask extends BaseTipsRunnable {

    private boolean hasServerInfoPlugin = false;

    public MotdTask(Main owner) {
        super(owner);

        try {
            Class.forName("com.smallaswater.serverinfo.ServerInfoMainClass");
            this.hasServerInfoPlugin = true;
        }catch (Exception ignored){

        }
    }

    @Override
    public void run() {
        while (this.owner.isEnabled()) {
            String motd = this.owner.getMotd();
            String[] strings = new String[]{"§c", "§6", "§e", "§a", "§b", "§9", "§d", "§7", "§5"};
            motd = motd.replace("{online}", Server.getInstance().getOnlinePlayers().size() + "");
            motd = motd.replace("{maxplayer}", Server.getInstance().getMaxPlayers() + "");
            motd = motd.replace("{换行}", "\n");
            motd = motd.replace("{color}", strings[new Random().nextInt(strings.length)]);
            int maxOnline = 0;
            if (this.hasServerInfoPlugin) {
                for (ServerInfo info : ServerInfoMainClass.getInstance().getServerInfos()) {
                    if (info.onLine()) {
                        maxOnline += info.getPlayer();
                        motd = motd.replace("{ServerInfoPlayer@" + info.getCallback() + "}", info.getPlayer() + "");
                        motd = motd.replace("{ServerInfoMaxPlayer@" + info.getCallback() + "}", info.getMaxPlayer() + "");
                    } else {
                        motd = motd.replace("{ServerInfoPlayer@" + info.getCallback() + "}", "服务器离线");
                        motd = motd.replace("{ServerInfoMaxPlayer@" + info.getCallback() + "}", "服务器离线");
                    }

                }
                motd = motd.replace("{ServerInfoPlayer}", maxOnline + "");
            }

            owner.getServer().getNetwork().setName(TextFormat.colorize('&', motd));
            try {
                Thread.sleep(getOwner().getConfig().getInt("自定义刷新刻度.motd", 20) * 50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
