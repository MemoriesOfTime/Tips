package tip.bossbar;

import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;
import tip.Main;
import tip.utils.BossMessageBuilder;


/**
 * @author SmallasWater
 */
public class BossBarApi extends DummyBossBar.Builder{

    public long bossId;

    private BossBarApi(Player player){
        super(player);
    }


    public static void createBossBar(Player player){
        if(!Main.getInstance().apis.containsKey(player)) {
            BossBarApi bossBar = new BossBarApi(player);
            bossBar.length(0);
            bossBar.text("加载中");
            Main.getInstance().apis.put(player, bossBar);
            bossBar.bossId = player.createBossBar(Main.getInstance().apis.get(player).build());
        }
    }


    public static void removeBossBar(Player player){
        if(Main.getInstance().apis.containsKey(player)){
            if(player.getDummyBossBar(Main.getInstance().apis.get(player).bossId) != null) {
                player.removeBossBar(Main.getInstance().apis.get(player).bossId);
            }
        }
    }


    public static void showBoss(Player player, String text, BossMessageBuilder builder, int time){
        if(Main.getInstance().apis.get(player) != null){
            if(player.getDummyBossBar(Main.getInstance().apis.get(player).bossId) == null){
                Main.getInstance().apis.remove(player);
                return;
            }
            DummyBossBar bossBar = player.getDummyBossBar(Main.getInstance().apis.get(player).bossId);
            bossBar.setText(text);
            if(builder.isHealth()){
                bossBar.setLength((float)Math.round(player.getHealth() / (float)player.getMaxHealth() * 100.0F));
            }else{
                float m;
                if(time < 0){
                    m = 0;
                }else{
                    m = (float)Math.round(time / (float)builder.getTime() * 100.0F);
                }
                bossBar.setLength(m);
            }
            try {
                Class.forName("cn.nukkit.utils.BossBarColor");
                bossBar.setColor(builder.getBossBarColor());
            }catch (Exception ignore){}
            player.createBossBar(bossBar);

        }
    }
}
