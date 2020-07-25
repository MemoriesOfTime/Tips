package tip;



import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import de.theamychan.scoreboard.network.Scoreboard;
import tip.bossbar.BossBarApi;
import tip.commands.TipsCommand;
import tip.messages.*;
import tip.tasks.*;

import tip.utils.OnListener;
import tip.utils.PlayerConfig;
import tip.windows.ListenerWindow;

import java.io.File;
import java.util.*;

/**
 * @author 若水

 */
public class Main extends PluginBase implements Listener {


    private static Config language;

    private static Main instance;

    public Map<Player, Scoreboard> scoreboards = new HashMap<>();

    public LinkedHashMap<Player,BossBarTask> tasks = new LinkedHashMap<>();

    private LinkedList<BaseMessage> showMessages = new LinkedList<>();


    public LinkedHashMap<Player, BossBarApi> apis = new LinkedHashMap<>();

    private LinkedList<PlayerConfig> playerConfigs = new LinkedList<>();

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.reloadConfig();
        this.getServer().getCommandMap().register("tips", new TipsCommand(getConfig().getString("Commands.name","tips")));
        this.getServer().getPluginManager().registerEvents(new OnListener(),this);
        this.getServer().getPluginManager().registerEvents(new ListenerWindow(),this);
        this.getServer().getScheduler().scheduleRepeatingTask(new TipTask(this),getTick("Tip"));
        this.getServer().getScheduler().scheduleRepeatingTask(new NameTagTask(this),getTick("NameTag"));
        Server.getInstance().getScheduler().scheduleRepeatingTask(new BossBarAllPlayerTask(this),getTick("BossHealth"));
        init();
        try {
            Class.forName("de.theamychan.scoreboard.api.ScoreboardAPI");
            Main.getInstance().getLogger().info(getLanguage("chunk-ScoreboardAPI-Plugin-Success"));
            Server.getInstance().getScheduler().scheduleRepeatingTask(new ScoreBoardTask(), getTick("NameTag"));
        } catch (Exception e) {
            Main.getInstance().getLogger().info(getLanguage("chunk-ScoreboardAPI-Plugin-Fail"));
        }
    }

    public static String getLanguage(String key) {
        return language.getString(key);
    }

    public void init(){
        tasks = new LinkedHashMap<>();
        showMessages = new LinkedList<>();
        this.getLogger().info("load success");
        if(!new File(this.getDataFolder()+"/language.properties").exists()){
            this.saveResource("language.properties","/language.properties",false);
        }
        language = new Config(this.getDataFolder()+"/language.properties",Config.PROPERTIES);
        if(!new File(this.getDataFolder()+"/Players").exists()){
            if(!new File(this.getDataFolder()+"/Players").mkdirs()){
                this.getLogger().info(getLanguage("create-File-Player-Fail"));
            }
        }
        //开始加载Message
        for(BaseMessage.BaseTypes types: BaseMessage.BaseTypes.values()){
            showMessages.addAll(addShowMessageByMap((Map) getConfig().get(types.getConfigName()),types.getType()));
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
                                (boolean) map.get("open"),
                                (int) map.get("time"), (boolean) map.get("changeByHealth"), getList((List) map.get("messages"))));
                    }
                    break;
                case BaseMessage.CHAT_MESSAGE_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new ChatMessage(o.toString(), (boolean) map.get("open"), (String) map.get("message"), (boolean) map.get("onlyWorld")));
                    }
                    break;
                case BaseMessage.NAME_TAG_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new NameTagMessage(o.toString(), (boolean) map.get("open"), (String) map.get("message")));
                    }
                    break;
                case BaseMessage.SCOREBOARD_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new ScoreBoardMessage(o.toString(),
                                (boolean) map.get("open"),
                                (String) map.get("Title"),
                                getList((List) map.get("Line"))));
                    }
                    break;
                case BaseMessage.TIP_MESSAGE_TYPE:
                    for (Object o : map1.keySet()) {
                        Map map = (Map) map1.get(o);
                        messages.add(new TipMessage(o.toString(),
                                (boolean) map.get("open"), (int) map.get("type"),
                                (String) map.get("message")));
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

    private int getTick(String key){
        return getConfig().getInt("Custom-Refresh-Scale."+key,20);
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
