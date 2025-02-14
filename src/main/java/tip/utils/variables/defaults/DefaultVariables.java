package tip.utils.variables.defaults;


import cn.nukkit.Achievement;
import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import me.onebone.economyapi.EconomyAPI;
import tip.Main;
import tip.utils.variables.BaseVariable;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

    private void time() {
        Calendar now = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        now.setTimeZone(timeZone);
        now.setTime(new Date());
        addStrReplaceString("{年}", String.valueOf(now.get(Calendar.YEAR)));
        addStrReplaceString("{月}", String.valueOf(now.get(Calendar.MONTH) + 1));
        addStrReplaceString("{日}", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
        addStrReplaceString("{时}", String.valueOf(now.get(Calendar.HOUR_OF_DAY)));
        addStrReplaceString("{分}", String.valueOf(now.get(Calendar.MINUTE)));
        addStrReplaceString("{秒}", String.valueOf(now.get(Calendar.SECOND)));
        addStrReplaceString("{星期}", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        if(player != null) {
            addStrReplaceString("{ms}",player.getPing()+"ms");
            addStrReplaceString("{levelName}",player.getLevel().getFolderName());
            addStrReplaceString("{x}", String.valueOf(Math.round(player.getX())));
            addStrReplaceString("{y}", String.valueOf(Math.round(player.getY())));
            addStrReplaceString("{z}", String.valueOf(Math.round(player.getZ())));
        }

        addStrReplaceString("{tps}", String.valueOf(Server.getInstance().getTicksPerSecond()));

    }

    private void configString() {
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
        if (mode.containsKey(String.valueOf(player.getGamemode()))) {
            m = (String) mode.get(String.valueOf(player.getGamemode()));
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
        if (fly.containsKey(String.valueOf(i))) {
            f = (String) fly.get(String.valueOf(i));
        }
        addStrReplaceString("{fly}", f);
    }

    private void defaultString() {
        String[] strings = new String[]{"§c", "§6", "§e", "§a", "§b", "§9", "§d", "§7", "§5"};
        addStrReplaceString("{online}", String.valueOf(Server.getInstance().getOnlinePlayers().size()));
        addStrReplaceString("{maxplayer}", String.valueOf(Server.getInstance().getMaxPlayers()));
        addStrReplaceString("{换行}", "\n");
        addStrReplaceString("{color}", strings[ThreadLocalRandom.current().nextInt(strings.length)]);
        Optional<Player> playerOptional = Optional.ofNullable(player);
        if (!playerOptional.isPresent()) {
            return;
        }
        Player player = playerOptional.get();
        if (!player.isOnline()) {
            return;
        }
        addStrReplaceString("{ach}", String.valueOf(player.achievements.size()));
        addStrReplaceString("{achCount}", String.valueOf(Achievement.achievements.size()));
        addStrReplaceString("{name}", player.getName());
        addStrReplaceString("{h}", String.valueOf(BigDecimal.valueOf(player.getHealth()).setScale(2, RoundingMode.HALF_UP).doubleValue()));
        addStrReplaceString("{mh}", String.valueOf(player.getMaxHealth()));
        addStrReplaceString("{damage}", String.valueOf(player.getInventory().getItemInHand().getDamage()));
        int id = player.getInventory().getItemInHand().getId();
        String displayId = String.valueOf(id);
        if (id == 255) {//兼容PN/PNX字符串ID
            //字符串ID
            Item item = player.getInventory().getItemInHand();
            //保证能编译通过
            Class<? extends Item> itemClass = item.getClass();
            try {
                Method m = itemClass.getMethod("getNamespaceId");
                displayId = (String) m.invoke(item);
            } catch (Exception ignore) {
            }
        }

        addStrReplaceString("{id}", displayId);
        addStrReplaceString("{food}", String.valueOf(player.getFoodData().getLevel()));
        addStrReplaceString("{mfood}", String.valueOf(player.getFoodData().getMaxLevel()));
        try {
            Class.forName("me.onebone.economyapi.EconomyAPI");
            addStrReplaceString("{money}", String.format("%.2f", EconomyAPI.getInstance().myMoney(player)));
        } catch (Exception ignore) {
        }

        addStrReplaceString("{deviceOS}", this.mapDeviceOSToString(player.getLoginChainData().getDeviceOS()));
        addStrReplaceString("{playerVersion}", player.getLoginChainData().getGameVersion());

        addStrReplaceString("{player_exp}", String.valueOf(player.getExperience()));
        addStrReplaceString("{player_exp_level}", String.valueOf(player.getExperienceLevel()));
        if (player.getExperienceLevel() >= 1) {
            addStrReplaceString("{player_exp_min}", String.valueOf(Player.calculateRequireExperience(player.getExperienceLevel() - 1)));
        } else {
            addStrReplaceString("{player_exp_min}", "0");
        }
        addStrReplaceString("{player_exp_max}", String.valueOf(Player.calculateRequireExperience(player.getExperienceLevel())));

    }

    private String mapDeviceOSToString(int os) {
        switch (os) {
            case 1: return "Android";
            case 2: return "iOS";
            case 3: return "macOS";
            case 4: return "Fire OS";
            case 5: return "Gear VR";
            case 6: return "HoloLens";
            case 7: return "Windows 10";
            case 8: return "Windows";
            case 9: return "Dedicated";
            case 10: return "tvOS";
            case 11: return "PlayStation";
            case 12: return "Switch";
            case 13: return "Xbox";
            case 14: return "Windows Phone";
        }
        return "Unknown";
    }


}
