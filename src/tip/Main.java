package tip;



import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import de.theamychan.scoreboard.network.Scoreboard;
import tip.bossbar.BossBarApi;
import tip.commands.TipsCommand;
import tip.lib.viewcompass.ViewCompassVariable;
import tip.messages.*;
import tip.tasks.*;

import tip.utils.Api;
import tip.utils.OnListener;
import tip.utils.PlayerConfig;
import tip.utils.variables.defaults.DefaultVariables;
import tip.utils.variables.defaults.PluginVariables;
import tip.windows.ListenerWindow;
import updata.AutoData;

import java.io.File;
import java.util.*;

/**
 * @author 若水

 */
public class Main extends PluginBase implements Listener {


    private static Main instance;

    public Map<Player, Scoreboard> scoreboards = new HashMap<>();

    public LinkedHashMap<Player,BossBarTask> tasks = new LinkedHashMap<>();

    private LinkedList<BaseMessage> showMessages = new LinkedList<>();

    public LinkedHashMap<Player, BossBarApi> apis = new LinkedHashMap<>();

    private LinkedList<PlayerConfig> playerConfigs = new LinkedList<>();

    private Config levelMessage;

    @Override
    public void onLoad() {
        Api.registerVariables("default", DefaultVariables.class);
        Api.registerVariables("plugin", PluginVariables.class);
        Api.registerVariables("ViewCompass", ViewCompassVariable.class);
    }

    @Override
    public void onEnable() {
        instance = this;
        if(Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null){
            if(AutoData.defaultUpData(this,getFile(),"SmallasWater","Tips")){
                return;
            }
        }
        init();

        this.getServer().getCommandMap().register("tips", new TipsCommand(getConfig().getString("自定义指令.name","tips")));
        this.getServer().getPluginManager().registerEvents(new OnListener(),this);
        this.getServer().getPluginManager().registerEvents(new ListenerWindow(),this);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new TipTask(this),getConfig().getInt("自定义刷新刻度.底部",20), true);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new BossBarAllPlayerTask(this),getConfig().getInt("自定义刷新刻度.Boss血条",20), true);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new NameTagTask(this),getConfig().getInt("自定义刷新刻度.头部",20), true);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new BroadCastTask(this),getConfig().getInt("自定义刷新刻度.聊天栏公告",20), true);

        try {
            Class.forName("de.theamychan.scoreboard.api.ScoreboardAPI");
            Main.getInstance().getLogger().info("检测到 ScoreboardAPI 成功开启计分板功能");
            this.getServer().getScheduler().scheduleRepeatingTask(new ScoreBoardTask(this), getConfig().getInt("自定义刷新刻度.计分板",20));
        } catch (Exception e) {
            Main.getInstance().getLogger().info("未检测到计分板API 无法使用计分板功能");
        }
    }



    public Config getLevelMessage() {
        return levelMessage;
    }

    public void init(){
        this.saveDefaultConfig();
        this.reloadConfig();
        if(!new File(this.getDataFolder()+"/levelMessage.yml").exists()){
            this.saveResource("levelMessage.yml");
        }
        levelMessage = new Config(this.getDataFolder()+"/levelMessage.yml",Config.YAML);
        tasks = new LinkedHashMap<>();
        showMessages = new LinkedList<>();
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
            showMessages.addAll(addShowMessageByMap((Map) getLevelMessage().get(types.getConfigName()),types.getType()));
        }
        playerConfigs = new LinkedList<>();
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
    }

    public LinkedList<PlayerConfig> getPlayerConfigs() {
        return playerConfigs;
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
                case BaseMessage.BROAD_CAST_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new BroadcastMessage(o.toString(),
                                (boolean) map.get("是否开启"),
                                (int) map.get("间隔时间"), getList((List) map.get("消息轮播"))));
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
        LinkedHashMap<String,LinkedHashMap<String,Object>> configs = new LinkedHashMap<>();
        for(BaseMessage message: getShowMessages()){
            BaseMessage.BaseTypes type = BaseMessage.getBaseTypeByInteger(message.getType());
            if(type != null){
                if(configs.containsKey(type.getConfigName())){
                    LinkedHashMap<String,Object> map = configs.get(type.getConfigName());
                    map.putAll(message.getConfig());
                }else {
                    configs.put(type.getConfigName(), message.getConfig());
                }
            }
        }
        Config config = getLevelMessage();
        for(String name: configs.keySet()){
            config.set(name,configs.get(name));
        }
        config.save();
    }


}
