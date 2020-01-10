package tip.bossbar;

import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;
import tip.Main;

public class BossAPI extends DummyBossBar.Builder{

    private BossAPI(Player player){
        super(player);

    }


    public static void createBossBar(Player player){
        BossAPI bossBar = (new BossAPI(player));
        bossBar.length(0);
        bossBar.text("加载中");
        Main.getInstance().apis.put(player, bossBar);
        player.createBossBar(Main.getInstance().apis.get(player).build());
    }


    static void showBoss(Player player, String text){
        if(Main.getInstance().apis.get(player) != null){
            DummyBossBar bossBar = Main.getInstance().apis.get(player).build();
            bossBar.setText(text);
            player.createBossBar(bossBar);
        }
    }
}
