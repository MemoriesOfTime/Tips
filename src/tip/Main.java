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
import tip.messages.defaults.*;
import tip.messages.defaults.BossBarMessage;
import tip.messages.defaults.BroadcastMessage;
import tip.messages.defaults.ChatMessage;
import tip.messages.defaults.NameTagMessage;
import tip.messages.defaults.ScoreBoardMessage;
import tip.messages.defaults.TipMessage;
import tip.tasks.*;

import tip.utils.Api;
import tip.utils.OnListener;
import tip.utils.PlayerConfig;
import tip.utils.ThemeManager;
import tip.utils.variables.BaseVariable;
import tip.utils.variables.VariableManager;
import tip.utils.variables.defaults.DefaultVariables;
import tip.utils.variables.defaults.PluginVariables;
import tip.windows.ListenerWindow;
import updata.AutoData;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author 若水

 */
public class Main extends PluginBase implements Listener {


    private static Main instance;

    private String theme;

    private boolean scoreboard = false;

    private String motd;


    private VariableManager varManager;
    public Map<Player, Scoreboard> scoreboards = new HashMap<>();

    public LinkedHashMap<Player,BossBarTask> tasks = new LinkedHashMap<>();

    private MessageManager showMessages = new MessageManager();

    private ThemeManager themeManager = new ThemeManager();

    public LinkedHashMap<Player, BossBarApi> apis = new LinkedHashMap<>();

    private LinkedList<PlayerConfig> playerConfigs = new LinkedList<>();


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

        motd = getConfig().getString("自定义MOTD.内容","&l{color}在线 -{online}/{maxplayer}\n {version}");
        if(getConfig().getBoolean("自定义MOTD.是否启用",false)){
            this.getServer().getScheduler().scheduleRepeatingTask(this,new MotdTask(this),getConfig().getInt("自定义刷新刻度.motd",20));
        }
        this.getServer().getCommandMap().register("tips", new TipsCommand(getConfig().getString("自定义指令.name","tips")));
        this.getServer().getPluginManager().registerEvents(new OnListener(),this);
        this.getServer().getPluginManager().registerEvents(new ListenerWindow(),this);

        try {
            Class.forName("de.theamychan.scoreboard.api.ScoreboardAPI");
            Main.getInstance().getLogger().info("检测到 ScoreboardAPI 成功开启计分板功能");
            scoreboard = true;

        } catch (Exception e) {
            Main.getInstance().getLogger().info("未检测到计分板API 无法使用计分板功能");
        }
    }

    public String getMotd() {
        return motd;
    }

    public boolean isScoreboard() {
        return scoreboard;
    }

    public String getTheme() {
        return theme;
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    private Config getLevelMessage() {
        return themeManager.getConfig(theme);
    }

    public void init(){
        this.saveDefaultConfig();
        this.reloadConfig();
        theme = getConfig().getString("默认样式","default");
        getLogger().info("当前样式为: "+theme);
        tasks = new LinkedHashMap<>();
        showMessages = new MessageManager();
        this.getLogger().info("加载成功");
        if(!new File(this.getDataFolder()+"/Tips变量.txt").exists()){
            this.saveResource("Tips变量.txt","/Tips变量.txt",false);
        }
        if(!new File(this.getDataFolder()+"/theme").exists()){
            if(!new File(this.getDataFolder()+"/theme").mkdirs()){
                getLogger().info("创建 theme 文件夹失败");
            }
            if(!new File(this.getDataFolder()+"/theme/easy.yml").exists()){
                this.saveResource("theme/easy.yml","/theme/easy.yml",false);
            }
        }

        if(!new File(this.getDataFolder()+"/theme/default.yml").exists()){
            this.saveResource("theme/default.yml","/theme/default.yml",false);
        }

        if(!new File(this.getDataFolder()+"/Players").exists()){
            if(!new File(this.getDataFolder()+"/Players").mkdirs()){
                this.getLogger().info("玩家文件夹创建失败");
            }
        }
        //加载风格
        loadTheme();

        showMessages.addAll(getManagerByConfig(getLevelMessage()));
        //开始加载Message
        playerConfigs = new LinkedList<>();

        // 初始化注册类
        initVariable();

    }

    public MessageManager getManagerByConfig(Config config){
        MessageManager messages = new MessageManager();
        if(config == null){
            return messages;
        }
        for(BaseMessage.BaseTypes types: BaseMessage.BaseTypes.values()){
            LinkedList<BaseMessage> messages1 = addShowMessageByMap((Map<?,?>) config.get(types.getConfigName()),types.getType());
            if(messages1.size() > 0){
                messages.addAll(messages1);
            }
        }
        return messages;
    }

    private void loadTheme(){
        String[] strings = getFileNames("theme");
        if(strings.length > 0){
            for(String file:strings){
                Config config = new Config(this.getDataFolder()+"/theme/"+file+".yml",2);
                themeManager.put(file,getManagerByConfig(config),config);
                getLogger().info("加载样式: "+file);
            }
        }else{
            getLogger().info("未加载任何样式");
        }
    }
    private void initVariable(){
        varManager = new VariableManager();
        BaseVariable variable;
        for(Class<? extends BaseVariable> var:Api.VARIABLE.values()){
            for (Constructor<?> constructor : var.getConstructors()) {
                try {
                    if(constructor.getParameterCount() == 1) {
                        variable = (BaseVariable) constructor.newInstance((Object) null);
                    }else{
                        variable = (BaseVariable) constructor.newInstance((Object) null,null);
                    }
                    varManager.addVariableClass(variable);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    public LinkedList<PlayerConfig> getPlayerConfigs() {
        return playerConfigs;
    }

    public VariableManager getVarManager() {
        return varManager;
    }

    public PlayerConfig getPlayerConfig(String playerName){
        for(PlayerConfig config:playerConfigs){
            if(config.getPlayerName().equalsIgnoreCase(playerName)){
                return config;
            }
        }
        return null;
    }

    public MessageManager getShowMessages() {
        return showMessages;
    }

    public MessageManager addShowMessageByMap(Map map1, int type){
        MessageManager messages = new MessageManager();
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

    public void setShowMessages(MessageManager showMessages) {
        this.showMessages = showMessages;
    }



    public static Main getInstance() {
        return instance;
    }

    private String[] getFileNames(String fileName) {
        List<String> names = new ArrayList<>();
        File files = new File(getDataFolder()+ "/"+fileName);
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

    private String[] getPlayerFiles() {
       return getFileNames("Players");
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
        if(config != null) {
            for (String name : configs.keySet()) {
                config.set(name, configs.get(name));
            }
            config.save();
        }
    }


}
