package tip.utils.variables.defaults;

import AwakenSystem.data.defaultAPI;
import SVIP.VIP;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
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
import tip.utils.variables.BaseVariable;
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
import java.util.LinkedList;


/**
 * 插件变量
 * @author SmallasWater
 */
@ChangeMessage
public class PluginVariables extends BaseVariable {


    public PluginVariables(Player player,String str) {
        super(player);
        this.string = str;
    }


    @Override
    public String getString() {
        return string;
    }

    private void getE(){
        if(Server.getInstance().getPluginManager().getPlugin("EasyAPI") != null){
            string = MessageFormatAPI.INSTANCE.format(string,player,player.getName());
        }

        if(Server.getInstance().getPluginManager().getPlugin("EconomyAPI") != null) {
            addStrReplaceString("{money}", String.format("%.2f", EconomyAPI.getInstance().myMoney(player)));
        }else{
            addStrReplaceString("{money}", TextFormat.RED+"无经济插件");
        }
    }

    private void getH(){
        if(Server.getInstance().getPluginManager().getPlugin("HealthAPI") != null){
            PlayerHealth health = PlayerHealth.getPlayerHealth(player);
            addStrReplaceString("{h}",String.format("%.1f",health.getHealth()));
            addStrReplaceString("{mh}",health.getMaxHealth()+"");
            addStrReplaceString("{hb}",String.format("%.2f",(health.getHealthPercentage() * 100)));
        }
    }

    private void getK(){
        if(Server.getInstance().getPluginManager().getPlugin("KDR") != null){
            addStrReplaceString("{kdrkills}",kdr.Main.plugin.getKills(player)+"");
            addStrReplaceString("{kdrdeath}",kdr.Main.plugin.getDeaths(player)+"");
            addStrReplaceString("{kdr}",String.format("%.2f",kdr.Main.plugin.getKDR(player)));

        }
    }

    private void getL(){
        if(Server.getInstance().getPluginManager().getPlugin("LevelAwakenSystem") != null){
            string = defaultAPI.getStr_replace(player,string);
        }
    }

    private void getM(){
        if (Server.getInstance().getPluginManager().getPlugin("Money") != null) {
            addStrReplaceString("{money-coin}", Money.getInstance().getMoney(player, CurrencyType.FIRST) + "");
            addStrReplaceString("{money-point}", Money.getInstance().getMoney(player, CurrencyType.SECOND) + "");
        }
        String marry = Main.getInstance().getConfig().getString("变量显示.无结婚显示", "§c单身");
        if (Server.getInstance().getPluginManager().getPlugin("MarryN") != null) {
            String player1 = loadData.data.getString(player.getName() + ".Cp");
            if (!"NULL".equalsIgnoreCase(player1)) {
                marry = player1;
            }
            String sex = "w".equalsIgnoreCase(loadData.data.getString(player.getName() + ".Sex")) ? loadCfg.gui_w : loadCfg.gui_m;

            addStrReplaceString("{sex}", sex);
        }
        addStrReplaceString("{marry}", marry);
    }


    private void getO(){
        if (Server.getInstance().getPluginManager().getPlugin("OreArea") != null) {
            PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
            addStrReplaceString("{arealevel}", playerClass.getMaxAreaLevel() + "");
            addStrReplaceString("{nextarealevel}", playerClass.getMaxAreaLevel() + 1 + "");
        }
    }

    private void getP(){
        if (Server.getInstance().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                PlaceholderAPI api = PlaceholderAPI.getInstance();
                string = api.translateString(string,player);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (Server.getInstance().getPluginManager().getPlugin("Proficiency") != null) {
            PlayerRPG playerRpgC = PlayerRPG.getPlayer(player, "采集");
            PlayerRPG playerRpgM = PlayerRPG.getPlayer(player, "制作");
            PlayerRPG playerRpgF = PlayerRPG.getPlayer(player, "战斗");
            if (playerRpgC != null && playerRpgM != null && playerRpgF != null) {
                RpgLevel pRpg = playerRpgC.getLevel();
                int rpg = pRpg.getLevel();
                int rpgMin = (int) pRpg.getExpMin();
                int rpgMax = pRpg.getExpMax();
                int rpgExp = (int) (Proficiency.getInstance().getDefault("采集", true) * (float) rpg);
                addStrReplaceString("{采集熟练度}", rpg + "");
                addStrReplaceString("{采集经验}", rpgMin + "");
                addStrReplaceString("{采集技能加成}", rpgExp + "");
                addStrReplaceString("{采集最大经验}", rpgMax + "");
                pRpg = playerRpgM.getLevel();
                rpg = pRpg.getLevel();
                rpgMin = (int) pRpg.getExpMin();
                rpgMax = pRpg.getExpMax();
                rpgExp = (int) (Proficiency.getInstance().getDefault("制作", true) * (float) rpg);
                addStrReplaceString("{制作熟练度}", rpg + "");
                addStrReplaceString("{制作经验}", rpgMin + "");
                addStrReplaceString("{制作技能加成}", rpgExp + "");
                addStrReplaceString("{制作最大经验}", rpgMax + "");
                pRpg = playerRpgF.getLevel();
                rpg = pRpg.getLevel();
                rpgMin = (int) pRpg.getExpMin();
                rpgMax = pRpg.getExpMax();
                rpgExp = (int) (Proficiency.getInstance().getDefault("战斗", true) * (float) rpg);
                addStrReplaceString("{战斗熟练度}", rpg + "");
                addStrReplaceString("{战斗经验}", rpgMin + "");
                addStrReplaceString("{战斗技能加成}", rpgExp + "");
                addStrReplaceString("{战斗最大经验}", rpgMax + "");
            }
        }
        if (Server.getInstance().getPluginManager().getPlugin("playerPoints") != null) {
            addStrReplaceString("{point}", String.format("%.2f", Point.myPoint(player)));
        }
    }


    private void getQ(){
        if (Server.getInstance().getPluginManager().getPlugin("Qwetitle") != null) {
            String ch = PY.getPlayerSign(player.getName());
            if (ch == null) {
                ch = Main.getInstance().getConfig().getString("变量显示.无称号显示");
            }
            addStrReplaceString("{qt_ch}", ch);
        }
    }
    private void getR1(){
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

                addStrReplaceString("{头盔}", hetName);addStrReplaceString("{头盔宝石}", hSize + "");
                addStrReplaceString("{胸甲}", chestName);addStrReplaceString("{胸甲宝石}", cSize + "");
                addStrReplaceString("{护腿}", leggingName);addStrReplaceString("{护腿宝石}", lSize + "");
                addStrReplaceString("{靴子}", bootName);addStrReplaceString("{靴子宝石}", bSize + "");
                addStrReplaceString("{武器名称}", name);
                addStrReplaceString("{宝石个数}", c + "");
            }
        }catch (Exception ignore){}
    }

    private void getR(){
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
            addStrReplaceString("{task-name}", taskName);
            addStrReplaceString("{task-count}", file.getCount() + "");
        }
        getR1();
    }


    private void getS(){
        String gh = "§c无公会";
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
            } catch (ClassNotFoundException ignored) {}
        }
        try {
            String vip = Main.getInstance().getConfig().getString("变量显示.无VIP显示");
            if (Server.getInstance().getPluginManager().getPlugin("SVIP") != null) {
                if (VIP.getVip().isVIP(player)) {
                    vip = VIP.getVip().getVIPLevel(player.getName());
                }
            }
            addStrReplaceString("{vip}", vip);
        }catch (Exception ignore){}
        addStrReplaceString("{gh}", gh);

    }



    private void getT(){
        try {
            String title = Main.getInstance().getConfig().getString("变量显示.无称号显示");
            if (Server.getInstance().getPluginManager().getPlugin("Titles") != null) {
                title.utils.PlayerFile file = PlayerFile.getPlayerFile(player);
                if(file != null){
                    Titles ch = file.getShowTitle();
                    if(ch != null){
                        title = ch.getName();
                    }
                }
            }
            addStrReplaceString("{ch}", title);
        }catch (Exception ignore){}
    }

    private void getU(){
        String gh = Main.getInstance().getConfig().getString("变量显示.无公会显示", "§c无公会");

        if (Server.getInstance().getPluginManager().getPlugin("union") != null) {
            try {
                addStrReplaceString("{gh}", nitu.main.getInstance().getGHname(player.getName()));
            } catch (NullPointerException e) {
                addStrReplaceString("{gh}", gh);
            }
            try {
                addStrReplaceString("{zw}", nitu.main.getInstance().getZWname(player.getName()));
            } catch (NullPointerException e) {
                addStrReplaceString("{zw}", "无");
            }
        }
    }

    private void getZ(){
        if(Server.getInstance().getPluginManager().getPlugin("ZSociety") != null) {
            try {
                string = PluginUtils.formatText(string,player);
            } catch (Exception ignore) {
            }
        }
    }




    @Override
    public void strReplace() {
        getE();
        getH();
        getK();
        getL();
        getM();
        getO();
        getP();
        getQ();
        getR();
        getS();
        getT();
        getU();
        getZ();
    }
}
