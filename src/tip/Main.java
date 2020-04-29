package tip;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import de.theamychan.scoreboard.network.Scoreboard;
import tip.bossbar.BossBarApi;
import tip.messages.*;
import tip.tasks.ScoreBoardTask;
import tip.tasks.TipTask;

import tip.utils.OnListener;
import tip.utils.PlayerConfig;
import tip.windows.CreateWindow;
import tip.windows.ListenerWindow;

import java.io.File;
import java.util.*;

/**
 * @author 若水

 */
public class Main extends PluginBase implements Listener {


    private static Main instance;

    public Map<Player, Scoreboard> scoreboards = new HashMap<>();


    private LinkedList<BaseMessage> showMessages = new LinkedList<>();


    public LinkedHashMap<Player, BossBarApi> apis = new LinkedHashMap<>();

    private LinkedList<PlayerConfig> playerConfigs = new LinkedList<>();

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.reloadConfig();
        this.getServer().getCommandMap().register("tips", new TipsCommand(getConfig().getString("自定义指令.name","tips")));
        this.getServer().getPluginManager().registerEvents(new OnListener(),this);
        this.getServer().getPluginManager().registerEvents(new ListenerWindow(),this);
        this.getServer().getScheduler().scheduleRepeatingTask(new TipTask(this),getTick());
        this.getLogger().info("加载成功");
        if(!new File(this.getDataFolder()+"/Tips变量参考.txt").exists()){
            this.saveResource("Tips变量.txt","/Tips变量参考.txt",false);
        }
        if(!new File(this.getDataFolder()+"/Players").exists()){
            if(!new File(this.getDataFolder()+"/Players").mkdirs()){
                this.getLogger().info("玩家文件夹创建失败");
            }
        }
        //开始加载Message
        for(BaseMessage.BaseTypes types: BaseMessage.BaseTypes.values()){
            showMessages.addAll(addShowMessageByMap((Map) getConfig().get(types.getConfigName()),types.getType()));
        }
        //加载玩家覆盖..
        Config config;
        for(String playerName:getPlayerFiles()){
            LinkedList<BaseMessage> messages = new LinkedList<>();
            config = new Config(this.getDataFolder()+"/Players/"+playerName+".yml",2);
            for(BaseMessage.BaseTypes types: BaseMessage.BaseTypes.values()){
                LinkedList<BaseMessage> messages1 = addShowMessageByMap((Map) config.get(types.getConfigName()),types.getType());
                if(messages1.size() > 0){
                    messages.addAll(messages1);
                }
            }
            playerConfigs.add(new PlayerConfig(playerName,messages));
        }
        try {
            Class.forName("de.theamychan.scoreboard.api.ScoreboardAPI");
            Main.getInstance().getLogger().info("检测到 ScoreboardAPI 成功开启计分板功能");
            Server.getInstance().getScheduler().scheduleRepeatingTask(new ScoreBoardTask(), 20);
        } catch (Exception e) {
            Main.getInstance().getLogger().info("未检测到计分板API 无法使用计分板功能");
        }
    }

    public LinkedList<PlayerConfig> getPlayerConfigs() {
        return playerConfigs;
    }

    private class TipsCommand extends Command{
        TipsCommand(String name) {
            super(name);
            this.setPermission("tips.ts");
            this.setAliases(getConfig().getStringList("自定义指令.aliases").toArray(new String[0]));
            this.description = getConfig().getString("自定义指令.description","自定义玩家提示");

        }
        @Override
        public boolean execute(CommandSender commandSender, String s, String[] strings) {
            if (commandSender.isOp()) {
                if (commandSender instanceof Player) {
                    CreateWindow.sendSetting((Player) commandSender);
                } else {
                    commandSender.sendMessage("请不要在控制台执行");
                }
                return true;
            }
            return false;
        }

    }

    public PlayerConfig getPlayerConfig(String playerName){
        for(PlayerConfig config:playerConfigs){
            if(config.getPlayerName().equalsIgnoreCase(playerName)){
                return config;
            }
        }
        return null;
    }

    public LinkedList<BaseMessage> getShowMessages() {
        return showMessages;
    }

    private LinkedList<BaseMessage> addShowMessageByMap(Map map1, int type){

        LinkedList<BaseMessage> messages = new LinkedList<>();
        if(map1 != null && map1.size() > 0) {
            switch (type) {
                case BaseMessage.BOSS_BAR_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new BossBarMessage(o.toString(),
                                (boolean) map.get("是否开启"),
                                (int) map.get("间隔时间"), (boolean) map.get("是否根据玩家血量变化"), getList((List) map.get("消息轮播"))));
                    }
                    break;
                case BaseMessage.CHAT_MESSAGE_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new ChatMessage(o.toString(), (boolean) map.get("是否开启"), (String) map.get("显示"), (boolean) map.get("是否仅在世界内有效")));
                    }
                    break;
                case BaseMessage.NAME_TAG_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new NameTagMessage(o.toString(), (boolean) map.get("是否开启"), (String) map.get("显示")));
                    }
                    break;
                case BaseMessage.SCOREBOARD_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new ScoreBoardMessage(o.toString(),
                                (boolean) map.get("是否开启"),
                                (String) map.get("Title"),
                                getList((List) map.get("Line"))));
                    }
                    break;
                case BaseMessage.TIP_MESSAGE_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new TipMessage(o.toString(),
                                (boolean) map.get("是否开启"), (int) map.get("显示类型"),
                                (String) map.get("显示")));
                    }
                    break;
                default:
                    break;
            }
        }
        return messages;
    }

    private LinkedList<String> getList(List list){
        LinkedList<String> strings = new LinkedList<>();
        for(Object o:list){
            strings.add(o.toString());
        }
        return strings;
    }

    public void setShowMessages(LinkedList<BaseMessage> showMessages) {
        this.showMessages = showMessages;
    }



    public static Main getInstance() {
        return instance;
    }

    private int getTick(){
        return getConfig().getInt("刷新刻度",20);
    }

    private String[] getPlayerFiles() {
        List<String> names = new ArrayList<>();
        File files = new File(getDataFolder()+ "/Players");
        if(files.isDirectory()){
            File[] filesArray = files.listFiles();
            if(filesArray != null){
                if(filesArray.length>0){
                    for(File file : filesArray){
                        names.add( file.getName().substring(0, file.getName().lastIndexOf(".")));
                    }
                }
            }
        }
        return names.toArray(new String[0]);
    }

    @Override
    public void onDisable() {
        for(PlayerConfig config:playerConfigs){
            config.save();
        }
    }
}
