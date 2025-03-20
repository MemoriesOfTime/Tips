package tip;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.BossBarColor;
import cn.nukkit.utils.Config;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import tip.bossbar.BossBarApi;
import tip.commands.TipsCommand;
import tip.lib.viewcompass.ViewCompassVariable;
import tip.messages.BaseMessage;
import tip.messages.defaults.*;
import tip.tasks.AddPlayerTask;
import tip.tasks.BossBarTask;
import tip.tasks.MotdTask;
import tip.tasks.TipTask;
import tip.utils.*;
import tip.utils.variables.VariableManager;
import tip.utils.variables.defaults.DefaultVariables;
import tip.windows.ListenerWindow;
import updata.AutoData;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 若水

 */
public class Main extends PluginBase {


    private static Main instance;

    private String theme;

    private boolean scoreboard = false;

    private String motd;


    private VariableManager varManager;
    public Set<Player> scoreboards = new HashSet<>();

    public Cache<Player,BossBarTask> tasks = CacheBuilder.newBuilder()
            .expireAfterAccess(60, TimeUnit.MINUTES)
            .build();

    public final LinkedHashMap<Player, BossBarApi> apis = new LinkedHashMap<>();

    private MessageManager showMessages = new MessageManager();

    private final ThemeManager themeManager = new ThemeManager();

    private LinkedList<PlayerConfig> playerConfigs = new LinkedList<>();

    public static ExecutorService executor = null;


    @Override
    public void onLoad() {
        instance = this;
        Api.registerVariables("default", DefaultVariables.class);
        Api.registerVariables("ViewCompass", ViewCompassVariable.class);
    }

    @Override
    public void onEnable() {
        if (executor != null) {
            executor.shutdown();
        }
        executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Tips-Thread-%d").build());

        try {
            if (Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null) {
                if (AutoData.defaultUpDataByMaven(this, this.getFile(), "com.smallaswater.tips", "Tips", null)) {
                    return;
                }
            }
        } catch (Throwable e) {
            this.getLogger().warning("插件自动更新失败！请检查AutoUpData前置插件！");
        }
        init();

        motd = getConfig().getString("自定义MOTD.内容","&l{color}在线 -{online}/{maxplayer}\n {version}");
        if(getConfig().getBoolean("自定义MOTD.是否启用",false)){
            executor.execute(new MotdTask(this));
        }
        this.getServer().getCommandMap().register("tips", new TipsCommand(getConfig().getString("自定义指令.name","tips")));
        this.getServer().getPluginManager().registerEvents(new OnListener(),this);
        this.getServer().getPluginManager().registerEvents(new ListenerWindow(),this);

        if (GameCoreDownload.checkAndDownload() == 1) {
            this.getLogger().error("MemoriesOfTime-GameCore依赖 下载失败，无法使用计分板功能！");
        } else {
            Main.getInstance().getLogger().info("检测到 MemoriesOfTime-GameCore 成功开启计分板功能");
            scoreboard = true;
        }
        AddPlayerTask.add(new TipTask(Main.getInstance(),Main.getInstance().getConfig().getInt("自定义刷新刻度.底部",20)));

        this.getLogger().info("插件加载完成~");
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
        showMessages = new MessageManager();

        this.saveResource("Tips变量.txt","/Tips变量.txt",true); //每次加载时写入，保证是最新的

        if(!new File(this.getDataFolder()+"/theme").exists()){
            if(!new File(this.getDataFolder()+"/theme").mkdirs()){
                getLogger().error("创建 theme 文件夹失败");
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
                this.getLogger().error("玩家文件夹创建失败");
            }
        }
        //加载风格
        loadTheme();
        theme = getConfig().getString("默认样式","default");
        getLogger().info("当前样式已设置为: "+theme);

        showMessages.addAll(getManagerByConfig(getLevelMessage()));
        //开始加载Message
        playerConfigs = new LinkedList<>();
    }

    public void loadPlayerConfig(Player player){
        if(new File(Main.getInstance().getDataFolder()+"/Players/"+player.getName()+".yml").exists()){
            Config config = new Config(Main.getInstance().getDataFolder()+"/Players/"+player.getName()+".yml",2);
            PlayerConfig playerConfig = new PlayerConfig(player.getName(),Main.getInstance().getManagerByConfig(config),config.getString("样式",null));
            Main.getInstance().getPlayerConfigs().add(playerConfig);

        }
    }

    private MessageManager getManagerByConfig(Config config){
        MessageManager messages = new MessageManager();
        if(config == null){
            return messages;
        }
        for(BaseMessage.BaseTypes types: BaseMessage.BaseTypes.values()){
            if(config.exists(types.getConfigName())) {
                LinkedList<BaseMessage> messages1 = addShowMessageByMap((Map<?, ?>) config.get(types.getConfigName()), types.getType());
                if (messages1.size() > 0) {
                    messages.addAll(messages1);
                }
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


    public LinkedList<PlayerConfig> getPlayerConfigs() {
        return playerConfigs;
    }

    public VariableManager getVarManager() {
        return varManager;
    }

    public void setVarManager(VariableManager varManager) {
        this.varManager = varManager;
    }

    public PlayerConfig getPlayerConfig(String playerName) {
        for(PlayerConfig config:playerConfigs) {
            if(config.getPlayerName().equalsIgnoreCase(playerName)) {
                return config;
            }
        }
        return null;
    }

    public PlayerConfig getPlayerConfigInit(String playerName){
        PlayerConfig config = new PlayerConfig(playerName,new MessageManager(),null);
        if(!playerConfigs.contains(config)){
            playerConfigs.add(config);
        }
        return playerConfigs.get(playerConfigs.indexOf(config));
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
                        BossBarMessage message = new BossBarMessage(o.toString(),
                                (boolean) map.get("是否开启"),
                                (int) map.get("间隔时间"),
                                (boolean) map.get("是否根据玩家血量变化"),
                                getList((List) map.get("消息轮播")));
                        if (map.containsKey("显示颜色")) {
                            try {
                                message.setBossBarColor(BossBarColor.valueOf((String) map.get("显示颜色")));
                            } catch (Exception e) {
                                getLogger().error("错误: 无法识别的BossBar颜色: " + map.get("显示颜色"), e);
                            }
                        }
                        messages.add(message);
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
        //保存配置文件
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
        //关闭不再使用的消息
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }

        tasks.invalidateAll();
        for (Player player : new HashSet<>(this.apis.keySet())) {
            BossBarApi.removeBossBar(player);
        }
    }


}
