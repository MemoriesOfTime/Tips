package tip;

import AwakenSystem.data.defaultAPI;
import SVIP.VIP;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import me.onebone.economyapi.EconomyAPI;
import money.CurrencyType;
import money.Money;
import net.player.api.Point;
import tip.bossbar.BossAPI;
import tip.bossbar.BossBarTask;
import title.utils.PlayerFile;
import title.utils.Titles;
import weapon.items.Armor;
import weapon.items.Weapon;
import world.proficiency.Proficiency;
import world.proficiency.utils.player.PlayerRPG;
import world.proficiency.utils.player.RpgLevel;


import java.util.*;

/**
 * @author 若水
 */
public class Main extends PluginBase implements Listener {


    private static Main instance;

    private boolean canShow = false;

    private String title = "Server Name";

    private List messages = new ArrayList<String>();

    public LinkedHashMap<Player, BossAPI> apis = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.reloadConfig();
        this.getServer().getPluginManager().registerEvents(this,this);
        this.getServer().getScheduler().scheduleRepeatingTask(new TipTask(),20);
        this.getLogger().info("加载成功");
        if(Server.getInstance().getPluginManager().getPlugin("ScoreboardAPI") != null){
            try {
                Class.forName("gt.creeperface.nukkit.scoreboardapi.ScoreboardAPI");
                Map configMap  = (Map) getConfig().get("计分板");
                if(configMap != null){
                    this.canShow = (boolean) configMap.get("canShow");
                    this.messages = (List) configMap.get("Line");
                    this.title = (String) configMap.get("Title");
                }
            } catch (ClassNotFoundException e) {
                getLogger().info("未检测到计分板API 无法使用计分板功能");
            }

        }

    }

    String getTitle() {
        return title;
    }

    List getMessages() {
        return messages;
    }

    boolean isCanShow() {
        return canShow;
    }

    public static Main getInstance() {
        return instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Server.getInstance().getScheduler().scheduleDelayedTask(new ScoreBoardTask(player),20);
        if(getConfig().getBoolean("是否显示Boss血条",false)){
            BossAPI.createBossBar(player);
            Server.getInstance().getScheduler().scheduleRepeatingTask(new BossBarTask(player),20);
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){
        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        String msg = event.getMessage();
        String s = getConfig().getString("聊天更改");
        if(Main.getInstance().getConfig().getBoolean("是否更改聊天",true)) {
            if (!"".equals(s)) {
                String send = s.replace("{msg}", msg);
                this.getServer().broadcastMessage(strReplace(send, player));
                event.setCancelled();
            }
        }
    }

    public static String strReplace(String string, Player player){
        if("".equals(string)){
            return string;
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
                    .replace("{id}",player.getInventory().getItemInHand().getId()+"")
                    .replace("{money}", EconomyAPI.getInstance().myMoney(player)+"");

        }
        string = string.replace("{food}",player.getFoodData().getLevel()+"")
                .replace("{mfood}",player.getFoodData().getMaxLevel()+"");
        String title = Main.getInstance().getConfig().getString("无称号显示");
        if(Server.getInstance().getPluginManager().getPlugin("Titles") != null){
            Titles ch = PlayerFile.getPlayerFile(player).getShowTitle();
            if(ch != null){
                title = ch.getName();
            }
        }
        String vip = Main.getInstance().getConfig().getString("无VIP显示");
        if(Server.getInstance().getPluginManager().getPlugin("SVIP") != null){
            if (VIP.getVip().isVIP(player)) {
                vip = VIP.getVip().getVIPLevel(player.getName());
            }
        }
        string = string.replace("{ch}",title);
        string = string.replace("{vip}",vip);
        Item item = player.getInventory().getItemInHand();
        if(Server.getInstance().getPluginManager().getPlugin("RSWeapon") != null){
            Weapon weapon = Weapon.getInstance(item);
            String name = TextFormat.RED+ "无神器";
            int c = 0;
            if(weapon != null){
                name = weapon.getName();
                if(!weapon.canUse(player)){
                    name += TextFormat.RED+" (无法使用)";
                }
                c = weapon.getGemStones().size();
            }
            Item het = player.getInventory().getHelmet();
            Item chest = player.getInventory().getChestplate();
            Item legging = player.getInventory().getLeggings();
            Item boot = player.getInventory().getBoots();
            int hSize = 0,cSize = 0,lSize = 0,bSize = 0;
            String hetName = TextFormat.RED+"无头盔";
            String chestName = TextFormat.RED+"无胸凯";
            String leggingName = TextFormat.RED+"无护腿";
            String bootName = TextFormat.RED+"无靴子";
            Armor armorHet = Armor.getInstance(het);
            Armor armorChest = Armor.getInstance(chest);
            Armor armorLegging = Armor.getInstance(legging);
            Armor armorBoot = Armor.getInstance(boot);
            if(armorHet != null){
                hetName = armorHet.getName();
                if(!armorHet.canUse(player)){
                    hetName += TextFormat.RED+" (无法使用)";
                }
                hSize = armorHet.getGemStones().size();
            }
            if(armorChest != null){
                chestName = armorChest.getName();
                if(!armorChest.canUse(player)){
                    chestName += TextFormat.RED+" (无法使用)";
                }
                cSize = armorChest.getGemStones().size();
            }
            if(armorLegging != null){
                leggingName = armorLegging.getName();
                if(!armorLegging.canUse(player)){
                    leggingName += TextFormat.RED+" (无法使用)";
                }
                lSize = armorLegging.getGemStones().size();
            }
            if(armorBoot != null){
                bootName = armorBoot.getName();
                if(!armorBoot.canUse(player)){
                    bootName += TextFormat.RED+" (无法使用)";
                }
                bSize = armorBoot.getGemStones().size();
            }


            string = string.replace("{头盔}",hetName).replace("{头盔宝石}",hSize+"");
            string = string.replace("{胸甲}",chestName).replace("{胸甲宝石}",cSize+"");
            string = string.replace("{护腿}",leggingName).replace("{护腿宝石}",lSize+"");
            string = string.replace("{靴子}",bootName).replace("{靴子宝石}",bSize+"");
            string = string.replace("{武器名称}",name);
            string = string.replace("{宝石个数}",c +"");

        }
        if(Server.getInstance().getPluginManager().getPlugin("Proficiency") != null){
            PlayerRPG playerRPGC = PlayerRPG.getPlayer(player, "采集");
            PlayerRPG playerRPGM = PlayerRPG.getPlayer(player, "制作");
            PlayerRPG playerRPGF = PlayerRPG.getPlayer(player, "战斗");
            if (playerRPGC != null && playerRPGM != null && playerRPGF != null) {
                RpgLevel pRPG = playerRPGC.getLevel();
                int rpg = pRPG.getLevel();
                int rpgMin = (int)pRPG.getExpMin();
                int rpgMax = pRPG.getExpMax();
                int rpgExp = (int)(Proficiency.getInstance().getDefault("采集", true) * (float)rpg);
                string = string.replace("{采集熟练度}", rpg + "").replace("{采集经验}", rpgMin + "").replace("{采集技能加成}", rpgExp + "").replace("{采集最大经验}", rpgMax + "");
                pRPG = playerRPGM.getLevel();
                rpg = pRPG.getLevel();
                rpgMin = (int)pRPG.getExpMin();
                rpgMax = pRPG.getExpMax();
                rpgExp = (int)(Proficiency.getInstance().getDefault("制作", true) * (float)rpg);
                string = string.replace("{制作熟练度}", rpg + "").replace("{制作经验}", rpgMin + "").replace("{制作技能加成}", rpgExp + "").replace("{制作最大经验}", rpgMax + "");
                pRPG = playerRPGF.getLevel();
                rpg = pRPG.getLevel();
                rpgMin = (int)pRPG.getExpMin();
                rpgMax = pRPG.getExpMax();
                rpgExp = (int)(Proficiency.getInstance().getDefault("战斗", true) * (float)rpg);
                string = string.replace("{战斗熟练度}", rpg + "").replace("{战斗经验}", rpgMin + "").replace("{战斗技能加成}", rpgExp + "").replace("{战斗最大经验}", rpgMax + "");
            }
        }

        if(Server.getInstance().getPluginManager().getPlugin("Money") != null){
            string = string.replace("{money-coin}", Money.getInstance().getMoney(player, CurrencyType.FIRST)+"");
            string = string.replace("{money-point}", Money.getInstance().getMoney(player,CurrencyType.SECOND)+"");
        }
        if(Server.getInstance().getPluginManager().getPlugin("playerPoints") != null){
            string = string.replace("{point}", String.format("%.2f",Point.myPoint(player)));
        }
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getDefault());
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
        return string;
    }
}
