package tip.bossbar;

import cn.nukkit.Player;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DummyBossBar;
import tip.Main;


/**
 * @author SmallasWater
 */
public class BossBarApi extends DummyBossBar.Builder{

    private BossBarApi(Player player){
        super(player);

    }


    public static void createBossBar(Player player){
        if(!Main.getInstance().apis.containsKey(player)) {
            BossBarApi bossBar = new BossBarApi(player);
            bossBar.length(0);
            bossBar.text("加载中");
            Main.getInstance().apis.put(player, bossBar);
            player.createBossBar(Main.getInstance().apis.get(player).build());
        }

    }


    public static void removeBossBar(Player player){
        if(Main.getInstance().apis.containsKey(player)){
            player.removeBossBar(Main.getInstance().apis.get(player).build().getBossBarId());
        }
    }


    public static void showBoss(Player player, String text, boolean health){
        if(Main.getInstance().apis.get(player) != null){
            DummyBossBar bossBar = Main.getInstance().apis.get(player).build();
            bossBar.setText(text);
            if(health){
                bossBar.setLength((float)Math.round(player.getHealth() / (float)player.getMaxHealth() * 100.0F));
            }

            bossBar.setColor(new BlockColor(0,205,102));
            player.createBossBar(bossBar);
        }
    }
}
