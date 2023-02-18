package tip.utils.variables.defaults;


import cn.nukkit.Achievement;
import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import me.onebone.economyapi.EconomyAPI;
import tip.Main;
import tip.utils.variables.BaseVariable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 默认变量
 * @author SmallasWater
 */
public class DefaultVariables extends BaseVariable {


    public DefaultVariables(Player player) {
        super(player);

    }

    @Override
    public void strReplace() {
        time();
        if(player != null) {
            configString();
        }
        defaultString();

    }

    private void time(){
        Calendar now = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        now.setTimeZone(timeZone);
        now.setTime(new Date());
        addStrReplaceString("{年}",now.get(Calendar.YEAR)+"");
        addStrReplaceString("{月}",(now.get(Calendar.MONTH) + 1)+"");
        addStrReplaceString("{日}",now.get(Calendar.DAY_OF_MONTH) +"");
        addStrReplaceString("{时}",(now.get(Calendar.HOUR_OF_DAY)) +"");
        addStrReplaceString("{分}",now.get(Calendar.MINUTE) +"");
        addStrReplaceString("{秒}",now.get(Calendar.SECOND) +"");
        addStrReplaceString("{星期}",now.get(Calendar.WEEK_OF_MONTH) +"");
        if(player != null){
            addStrReplaceString("{ms}",player.getPing()+"ms");
            addStrReplaceString("{levelName}",player.getLevel().getFolderName());
            addStrReplaceString("{x}",Math.round(player.getX())+"");
            addStrReplaceString("{y}",Math.round(player.getY())+"");
            addStrReplaceString("{z}",Math.round(player.getZ())+"");
        }

        addStrReplaceString("{tps}",Server.getInstance().getTicksPerSecond()+"");

    }

    private void configString(){
        Map op = Main.getInstance().getConfig().get("变量显示.玩家权限", new LinkedHashMap<String, String>() {
            {
                put("op", "§c[§e管理员§c]§f");
                put("player", "§c[§b玩家§c]§f");
            }
        });
        String o = (String) op.get("player");
        if (player.isOp()) {
            o = (String) op.get("op");
        }
        addStrReplaceString("{op}", o);
        Map mode = Main.getInstance().getConfig().get("变量显示.游戏模式", new LinkedHashMap<String, String>() {
            {
                put("0", "生存");
                put("1", "创造");
                put("2", "冒险");
                put("3", "旁观");
            }
        });
        String m = (String) mode.get("0");
        if (mode.containsKey(player.getGamemode()+"")) {
            m = (String) mode.get(player.getGamemode()+"");
        }
        addStrReplaceString("{gm}", m);
        Map fly = Main.getInstance().getConfig().get("变量显示.飞行", new LinkedHashMap<String, String>() {
            {
                put("0", "飞行开启");
                put("1", "飞行关闭");
            }
        });
        String f = (String) mode.get("1");
        int i = 1;
        if (player.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT)) {
            i = 0;
        }
        if (fly.containsKey(i+"")) {
            f = (String) fly.get(i+"");
        }
        addStrReplaceString("{fly}", f);
    }

    private void defaultString() {
        String[] strings = new String[]{"§c","§6","§e","§a","§b","§9","§d","§7","§5"};
        addStrReplaceString("{online}",Server.getInstance().getOnlinePlayers().size()+"");
        addStrReplaceString("{maxplayer}",Server.getInstance().getMaxPlayers()+"");
        addStrReplaceString("{换行}","\n");
        addStrReplaceString("{color}",strings[new Random().nextInt(strings.length)]);
        Optional<Player> playerOptional = Optional.ofNullable(player);
        if(!playerOptional.isPresent()){
            return;
        }
        Player player = playerOptional.get();
        if(!playerOptional.get().isOnline()){
            return;
        }
        int ach = player.achievements.size();
        addStrReplaceString("{ach}",ach+"");
        addStrReplaceString("{achCount}", Achievement.achievements.size()+"");
        addStrReplaceString("{name}",player.getName());
        addStrReplaceString("{h}", BigDecimal.valueOf(player.getHealth()).setScale(2, RoundingMode.HALF_UP).doubleValue() + "");
        addStrReplaceString("{mh}",player.getMaxHealth()+"");
        addStrReplaceString("{damage}",player.getInventory().getItemInHand().getDamage()+"");
        //兼容新版的 字符串ID
        int id = player.getInventory().getItemInHand().getId();
        String displayId = id+"";
        if(id == 255){
            //字符串ID
            Item item = player.getInventory().getItemInHand();
            //保证能编译通过
            Class<? extends Item> itemClass = item.getClass();
            try {
                Method m = itemClass.getMethod("getNamespaceId");
                displayId = (String) m.invoke(item);
            } catch (Exception ignore) {}

        }

        addStrReplaceString("{id}",displayId);
        addStrReplaceString("{food}",player.getFoodData().getLevel()+"");
        addStrReplaceString("{mfood}",player.getFoodData().getMaxLevel()+"");
        addStrReplaceString("{money}", String.format("%.2f",EconomyAPI.getInstance().myMoney(player)));

    }


}
