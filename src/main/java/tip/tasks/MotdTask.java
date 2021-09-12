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
    public MotdTask(Main owner) {
        super(owner);
    }

    @Override
    public void run() {
        String s;
        while (true) {
            s = owner.getMotd();
            String[] strings = new String[]{"§c","§6","§e","§a","§b","§9","§d","§7","§5"};
            s = s.replace("{online}", Server.getInstance().getOnlinePlayers().size()+"");
            s = s.replace("{maxplayer}",Server.getInstance().getMaxPlayers()+"");
            s = s.replace("{换行}","\n");
            s = s.replace("{color}",strings[new Random().nextInt(strings.length)]);
            int maxOnline = 0;
            try{
                Class.forName("ServerInfoMainClass");
                for (ServerInfo info : ServerInfoMainClass.getInstance().getServerInfos()) {
                    if(info.onLine()) {
                        maxOnline += info.getPlayer();
                        s = s.replace("{ServerInfoPlayer@" + info.getCallback() + "}", info.getPlayer() + "");
                        s = s.replace("{ServerInfoMaxPlayer@" + info.getCallback() + "}", info.getMaxPlayer() + "");
                    }else{
                        s = s.replace("{ServerInfoPlayer@" + info.getCallback() + "}", "服务器离线");
                        s = s.replace("{ServerInfoMaxPlayer@" + info.getCallback() + "}", "服务器离线");
                    }

                }
                s = s.replace("{ServerInfoPlayer}",maxOnline+"");
            } catch (ClassNotFoundException ignore) {}

            owner.getServer().getNetwork().setName(TextFormat.colorize('&',s));
            try {
                Thread.sleep(getOwner().getConfig().getInt("自定义刷新刻度.motd",20) * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
