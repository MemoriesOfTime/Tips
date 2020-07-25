package tip.utils;

import AwakenSystem.data.defaultAPI;
import SVIP.VIP;
import cn.nukkit.Achievement;
import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;
import cn.xiaokai.stockings.py.PY;
import com.bc.marryN.load.loadCfg;
import com.bc.marryN.load.loadData;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import com.smallaswater.SociatyMainClass;
import com.smallaswater.data.IDataStore;
import com.task.utils.tasks.taskitems.PlayerTask;
import com.zixuan007.society.utils.PluginUtils;
import healthapi.PlayerHealth;
import me.onebone.economyapi.EconomyAPI;
import money.CurrencyType;
import money.Money;
import net.player.api.Point;
import ore.area.utils.player.PlayerClass;
import tip.Main;
import tip.messages.BaseMessage;
import title.utils.PlayerFile;
import title.utils.Titles;
import top.wetabq.easyapi.api.defaults.MessageFormatAPI;
import weapon.items.Armor;
import weapon.items.Weapon;
import weapon.utils.PlayerAddAttributes;
import world.proficiency.Proficiency;
import world.proficiency.utils.player.PlayerRPG;
import world.proficiency.utils.player.RpgLevel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 若水
 * Tips变量
 * */
public class Api {

    private String string = "";
    private Player player;
    public Api(String string,Player player){
        this.string = string;
        this.player = player;
    }

    private String getD(Player player) {
        try {
            if (Server.getInstance().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                try {
                    PlaceholderAPI api = PlaceholderAPI.getInstance();
                    string = api.translateString(string,player);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (Server.getInstance().getPluginManager().getPlugin("Qwetitle") != null) {
                String ch = PY.getPlayerSign(player.getName());
                if (ch == null) {
                    ch = Main.getInstance().getConfig().getString("Variable.not-title");
                }
                string = string.replace("{qt_ch}", ch);
            }
            String gh = "§cnot Guild";
            if (Server.getInstance().getPluginManager().getPlugin("Sociaty") != null) {
                try {
                    Class.forName("com.smallaswater.SociatyMainClass");
                    try {
                        Class socitaty = SociatyMainClass.class;
                        Field moted = socitaty.getDeclaredField("dataStorager");
                        moted.setAccessible(true);
                        Object o = moted.get(socitaty.newInstance());
                        if (o instanceof IDataStore) {
                            gh = ((IDataStore) o).getPlayerSociaty(player.getName()).getName();
                        }
                    } catch (IllegalAccessException | InstantiationException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException ignored) {
                }
            }
            string = string.replace("{gh}", gh);
            Map op = Main.getInstance().getConfig().get("Variable.player-permissions", new HashMap<String, String>() {
                {
                    put("op", "§c[§Admin§c]§f");
                    put("player", "§c[§bPlayer§c]§f");
                }
            });
            String o = (String) op.get("player");
            if (player.isOp()) {
                o = (String) op.get("op");
            }
            string = string.replace("{op}", o);
            Map mode = Main.getInstance().getConfig().get("Variable.game-mode", new HashMap<String, String>() {
                {
                    put("0", "Survival");
                    put("1", "Create");
                    put("2", "Adventure");
                    put("3", "Watch");
                }
            });
            String m = (String) mode.get("0");
            if (mode.containsKey(player.getGamemode()+"")) {
                m = (String) mode.get(player.getGamemode()+"");
            }
            string = string.replace("{gm}", m);
            Map fly = Main.getInstance().getConfig().get("Variable.fly", new HashMap<String, String>() {
                {
                    put("0", "fly open");
                    put("1", "fly close");
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
            string = string.replace("{fly}", f);

        }catch (Exception ignore){}
        return string;

    }


    private String getB(Player player){
        String title = "no plugin..";
        String vip = "no plugin";
        try {
            title = Main.getInstance().getConfig().getString("Variable.not-title");
            if (Server.getInstance().getPluginManager().getPlugin("Titles") != null) {
                title.utils.PlayerFile file = PlayerFile.getPlayerFile(player);
                if(file != null){
                    Titles ch = file.getShowTitle();
                    if(ch != null){
                        title = ch.getName();
                    }
                }
            }
        }catch (Exception ignore){}
        try {
            vip = Main.getInstance().getConfig().getString("Variable.not-vip");
            if (Server.getInstance().getPluginManager().getPlugin("SVIP") != null) {
                if (VIP.getVip().isVIP(player)) {
                    vip = VIP.getVip().getVIPLevel(player.getName());
                }
            }
        }catch (Exception ignore){}
        try {
            if (Server.getInstance().getPluginManager().getPlugin("RSTask") != null) {
                com.task.utils.tasks.PlayerFile file = com.task.utils.tasks.PlayerFile.getPlayerFile(player.getName());
                LinkedList<PlayerTask> tasks = file.getInviteTasks();
                String taskName = "暂无";
                if (tasks.size() > 0) {
                    PlayerTask task = tasks.get(0);
                    if (task != null) {
                        taskName = task.getTaskName();
                    }
                }
                string = string.replace("{task-name}", taskName);
                string = string.replace("{task-count}", file.getCount() + "");
            }
        } catch (Exception ignore) {}
        string = string.replace("{ch}", title);
        string = string.replace("{vip}", vip);

        if(Server.getInstance().getPluginManager().getPlugin("MurderMystery") != null){
            string = string.replace("%MurderRoomMode%", name.murdermystery.api.Api.getRoomMode(player));
            string = string.replace("%MurderPlayerMode%", name.murdermystery.api.Api.getPlayerMode(player));
            string = string.replace("%MurderTime%", name.murdermystery.api.Api.getTime(player));
            string = string.replace("%MurderSurvivorNumber%", name.murdermystery.api.Api.getSurvivor(player));
        }
        return string;
    }

    private String getC(Player player){
        try {
            if (Server.getInstance().getPluginManager().getPlugin("OreArea") != null) {
                PlayerClass playerClass = PlayerClass.getPlayerClass(player);
                string = string.replace("{arealevel}", playerClass.getMaxAreaLevel() + "");
                string = string.replace("{nextarealevel}", playerClass.getMaxAreaLevel() + 1 + "");
            }
        }catch (Exception ignore){}
        try {
            if (Server.getInstance().getPluginManager().getPlugin("Proficiency") != null) {
                PlayerRPG playerRPGC = PlayerRPG.getPlayer(player, "采集");
                PlayerRPG playerRPGM = PlayerRPG.getPlayer(player, "制作");
                PlayerRPG playerRPGF = PlayerRPG.getPlayer(player, "战斗");
                if (playerRPGC != null && playerRPGM != null && playerRPGF != null) {
                    RpgLevel pRPG = playerRPGC.getLevel();
                    int rpg = pRPG.getLevel();
                    int rpgMin = (int) pRPG.getExpMin();
                    int rpgMax = pRPG.getExpMax();
                    int rpgExp = (int) (Proficiency.getInstance().getDefault("采集", true) * (float) rpg);
                    string = string.replace("{采集熟练度}", rpg + "").replace("{采集经验}", rpgMin + "").replace("{采集技能加成}", rpgExp + "").replace("{采集最大经验}", rpgMax + "");
                    pRPG = playerRPGM.getLevel();
                    rpg = pRPG.getLevel();
                    rpgMin = (int) pRPG.getExpMin();
                    rpgMax = pRPG.getExpMax();
                    rpgExp = (int) (Proficiency.getInstance().getDefault("制作", true) * (float) rpg);
                    string = string.replace("{制作熟练度}", rpg + "").replace("{制作经验}", rpgMin + "").replace("{制作技能加成}", rpgExp + "").replace("{制作最大经验}", rpgMax + "");
                    pRPG = playerRPGF.getLevel();
                    rpg = pRPG.getLevel();
                    rpgMin = (int) pRPG.getExpMin();
                    rpgMax = pRPG.getExpMax();
                    rpgExp = (int) (Proficiency.getInstance().getDefault("战斗", true) * (float) rpg);
                    string = string.replace("{战斗熟练度}", rpg + "").replace("{战斗经验}", rpgMin + "").replace("{战斗技能加成}", rpgExp + "").replace("{战斗最大经验}", rpgMax + "");
                }
            }
        }catch (Exception ignore){}

            if (Server.getInstance().getPluginManager().getPlugin("Money") != null) {
                string = string.replace("{money-coin}", Money.getInstance().getMoney(player, CurrencyType.FIRST) + "");
                string = string.replace("{money-point}", Money.getInstance().getMoney(player, CurrencyType.SECOND) + "");
            }
            if (Server.getInstance().getPluginManager().getPlugin("playerPoints") != null) {
                string = string.replace("{point}", String.format("%.2f", Point.myPoint(player)));
            }
            String gh = Main.getInstance().getConfig().getString("Variable.not-guild", "§c无公会");
            if(Server.getInstance().getPluginManager().getPlugin("ZSociety") != null) {
                try {
                    string = PluginUtils.formatText(string,player);
                } catch (Exception ignore) {
                }
            }
            String marry = Main.getInstance().getConfig().getString("Variable.not-marry", "§c单身");
            if (Server.getInstance().getPluginManager().getPlugin("MarryN") != null) {
                String player1 = loadData.data.getString(player.getName() + ".Cp");
                if (!"NULL".equalsIgnoreCase(player1)) {
                    marry = player1;
                }
                String sex = "w".equalsIgnoreCase(loadData.data.getString(player.getName() + ".Sex")) ? loadCfg.gui_w : loadCfg.gui_m;

                string = string.replace("{sex}", sex);
            }
            string = string.replace("{marry}", marry);

            if (Server.getInstance().getPluginManager().getPlugin("union") != null) {
                try {
                    string = string.replace("{gh}", nitu.main.getInstance().getGHname(player.getName()));
                } catch (NullPointerException e) {
                    string = string.replace("{gh}", gh);
                }
                try {
                    string = string.replace("{zw}", nitu.main.getInstance().getZWname(player.getName()));
                } catch (NullPointerException e) {
                    string = string.replace("{zw}", "无");
                }
            }
        return string;

    }

    private String getA(Player player){
        try {

            Item item = player.getInventory().getItemInHand();
            if (Server.getInstance().getPluginManager().getPlugin("RSWeapon") != null) {
                Weapon weapon = Weapon.getInstance(item);
                String name = TextFormat.RED + "无神器";
                int c = 0;
                if (weapon != null) {
                    name = weapon.getName();
                    if (!weapon.canUse(player)) {
                        name += TextFormat.RED + " (无法使用)";
                    }
                    c = weapon.getGemStones().size();
                }
                Item het = player.getInventory().getHelmet();
                Item chest = player.getInventory().getChestplate();
                Item legging = player.getInventory().getLeggings();
                Item boot = player.getInventory().getBoots();
                int hSize = 0, cSize = 0, lSize = 0, bSize = 0;
                String hetName = TextFormat.RED + "无头盔";
                String chestName = TextFormat.RED + "无胸凯";
                String leggingName = TextFormat.RED + "无护腿";
                String bootName = TextFormat.RED + "无靴子";
                Armor armorHet = Armor.getInstance(het);
                Armor armorChest = Armor.getInstance(chest);
                Armor armorLegging = Armor.getInstance(legging);
                Armor armorBoot = Armor.getInstance(boot);
                if (armorHet != null) {
                    hetName = armorHet.getName();
                    if (!armorHet.canUse(player)) {
                        hetName += TextFormat.RED + " (无法使用)";
                    }
                    hSize = armorHet.getGemStones().size();
                }
                if (armorChest != null) {
                    chestName = armorChest.getName();
                    if (!armorChest.canUse(player)) {
                        chestName += TextFormat.RED + " (无法使用)";
                    }
                    cSize = armorChest.getGemStones().size();
                }
                if (armorLegging != null) {
                    leggingName = armorLegging.getName();
                    if (!armorLegging.canUse(player)) {
                        leggingName += TextFormat.RED + " (无法使用)";
                    }
                    lSize = armorLegging.getGemStones().size();
                }
                if (armorBoot != null) {
                    bootName = armorBoot.getName();
                    if (!armorBoot.canUse(player)) {
                        bootName += TextFormat.RED + " (无法使用)";
                    }
                    bSize = armorBoot.getGemStones().size();
                }
                try{
                    PlayerAddAttributes playerAddAttributes = new PlayerAddAttributes();
                    string = playerAddAttributes.getStrReplace(player,string);
                }catch (Exception ignore){}

                string = string.replace("{头盔}", hetName).replace("{头盔宝石}", hSize + "");
                string = string.replace("{胸甲}", chestName).replace("{胸甲宝石}", cSize + "");
                string = string.replace("{护腿}", leggingName).replace("{护腿宝石}", lSize + "");
                string = string.replace("{靴子}", bootName).replace("{靴子宝石}", bSize + "");
                string = string.replace("{武器名称}", name);
                string = string.replace("{宝石个数}", c + "");


            }
        }catch (Exception ignore){}
        return string;
    }


    public String strReplace(){
        Optional<Player> playerOptional = Optional.ofNullable(player);
        if(!playerOptional.isPresent()){
            return string;
        }
        Player player = playerOptional.get();
        if(!playerOptional.get().isOnline()){
            return string;
        }

        if("".equals(string)){
            return string;
        }
        if(Server.getInstance().getPluginManager().getPlugin("HealthAPI") != null){
            PlayerHealth health = PlayerHealth.getPlayerHealth(playerOptional.get());
            string = string.replace("{h}",String.format("%.1f",health.getHealth())).replace("{mh}",health.getMaxHealth()+"");
            string = string.replace("{hb}",String.format("%.2f",(health.getHealthPercentage() * 100)));
        }
        int ach = player.achievements.size();
        string = string.replace("{ach}",ach+"").replace("{achCount}", Achievement.achievements.size()+"");
        if(Server.getInstance().getPluginManager().getPlugin("EconomyAPI") != null) {
            string = string.replace("{money}", String.format("%.2f", EconomyAPI.getInstance().myMoney(player)));
        }else{
            string = string.replace("{money}",TextFormat.RED+"no EconomyAPI");
        }
        if(Server.getInstance().getPluginManager().getPlugin("LevelAwakenSystem") != null){
            string = defaultAPI.getStr_replace(player,string);
        }else {
            string = string
                    .replace("{name}",player.getName())
                    .replace("{换行}","\n")
                    .replace("{h}",player.getHealth()+"")
                    .replace("{mh}",player.getMaxHealth()+"")

                    .replace("{online}",Server.getInstance().getOnlinePlayers().size()+"")
                    .replace("{maxplayer}",Server.getInstance().getMaxPlayers()+"")
                    .replace("{damage}",player.getInventory().getItemInHand().getDamage()+"")
                    .replace("{id}",player.getInventory().getItemInHand().getId()+"");

        }
        string = string.replace("{food}",player.getFoodData().getLevel()+"")
                .replace("{mfood}",player.getFoodData().getMaxLevel()+"");


        string = getA(player);
        string = getB(player);
        string = getC(player);
        string = getD(player);
        if(Server.getInstance().getPluginManager().getPlugin("EasyAPI") != null){
            string = MessageFormatAPI.INSTANCE.format(string,player,player.getName());
        }

        if(Server.getInstance().getPluginManager().getPlugin("KDR") != null){
            string = string.replace("{kdrkills}",kdr.Main.plugin.getKills(player)+"");
            string = string.replace("{kdrdeath}",kdr.Main.plugin.getDeaths(player)+"");
            string = string.replace("{kdr}",String.format("%.2f",kdr.Main.plugin.getKDR(player)));

        }
        Calendar now = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        now.setTimeZone(timeZone);
        now.setTime(new Date());
        string = string.replace("{年}",now.get(Calendar.YEAR)+"");
        string = string.replace("{月}",(now.get(Calendar.MONTH) + 1)+"");
        string = string.replace("{日}",now.get(Calendar.DAY_OF_MONTH) +"");
        string = string.replace("{时}",(now.get(Calendar.HOUR_OF_DAY)) +"");
        string = string.replace("{分}",now.get(Calendar.MINUTE) +"");
        string = string.replace("{秒}",now.get(Calendar.SECOND) +"");
        string = string.replace("{星期}",now.get(Calendar.WEEK_OF_MONTH) +"");
        string = string.replace("{ms}",player.getPing()+"ms");
        string = string.replace("{levelName}",player.getLevel().getFolderName());
        string = string.replace("{x}",Math.round(player.getX())+"");
        string = string.replace("{y}",Math.round(player.getY())+"");
        string = string.replace("{z}",Math.round(player.getZ())+"");
        string = string.replace("{tps}",Server.getInstance().getTicksPerSecond()+"");



        return string;
    }

    public static LinkedList<String> getSettingLevels(){
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("default");
        for(Level level:Server.getInstance().getLevels().values()){
            if(!"default".equalsIgnoreCase(level.getFolderName())) {
                linkedList.add(level.getFolderName());
            }
        }
        return linkedList;
    }

    public static void setPlayerShowMessage(String playerName,BaseMessage message){
        PlayerConfig config = Main.getInstance().getPlayerConfig(playerName);
        if(config == null){
            config = new PlayerConfig(playerName,new LinkedList<>());
        }
        config.setMessage(message);
        if(!Main.getInstance().getPlayerConfigs().contains(config)) {
            Main.getInstance().getPlayerConfigs().add(config);
        }

    }

    public static void removePlayerShowMessage(String playerName,BaseMessage message){
        PlayerConfig config = Main.getInstance().getPlayerConfig(playerName);
        if(config == null){
            config = new PlayerConfig(playerName,new LinkedList<>());
        }
        if(config.messages.contains(message)){
            config.removeMessage(message);
        }
        if(config.messages.size() == 0){
            Main.getInstance().getPlayerConfigs().remove(config);
        }
    }
}
