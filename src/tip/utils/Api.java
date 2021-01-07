package tip.utils;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.defaults.MessageManager;
import tip.utils.variables.BaseVariable;
import java.util.*;

/**
 * @author 若水
 * Tips变量
 * */
public class Api {

    private final String string;
    private final IPlayer player;

    public static final LinkedHashMap<String, Class<? extends BaseVariable>> VARIABLE = new LinkedHashMap<>();
    public Api(String string, IPlayer player){
        this.string = string;
        this.player = player;
    }

    public static void registerVariables(String name, Class<? extends BaseVariable> variable){
        if(VARIABLE.containsKey(name)){
            return;
        }
        VARIABLE.put(name, variable);
    }

    public String strReplace(){
        String m = string;
        if(player instanceof Player){
            m = Main.getInstance().getVarManager().format((Player) player,m);
        }
        return TextFormat.colorize('&',m);
    }

    /**
     * 增加单个变量
     * */
    public static void addVariable(String var,String message){
        Main.getInstance().getVarManager().addVariable(var, message);
    }



    public static BaseMessage getSendPlayerMessage(String playerName,String levelName, BaseMessage.BaseTypes baseTypes){
        PlayerConfig config = Main.getInstance().getPlayerConfig(playerName);
        BaseMessage message = null;
        if(config != null){
            message = config.getMessage(levelName,baseTypes.getType());
        }
        if(message == null){
            message = getLevelDefaultMessage(levelName, baseTypes);
        }

        return message;
    }




    public static BaseMessage getLevelDefaultMessage(String levelName, BaseMessage.BaseTypes baseTypes){
        return Main.getInstance().getShowMessages().getMessageByTypeAndWorld(levelName,baseTypes.getType());
    }

    public static void setLevelMessage(BaseMessage message){
        Main.getInstance().getShowMessages().setMessage(message);
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
            config = new PlayerConfig(playerName, new MessageManager(),Main.getInstance().getTheme());
        }
        config.setMessage(message);
        if(!Main.getInstance().getPlayerConfigs().contains(config)) {
            Main.getInstance().getPlayerConfigs().add(config);
        }

    }

    public static void removePlayerShowMessage(String playerName,BaseMessage message){
        PlayerConfig config = Main.getInstance().getPlayerConfig(playerName);
        if(config == null){
            config = new PlayerConfig(playerName,new MessageManager(),Main.getInstance().getTheme());
        }
        if(config.messages.contains(message)){
            config.removeMessage(message);
        }
        if(config.messages.size() == 0){
            Main.getInstance().getPlayerConfigs().remove(config);
        }
    }
}
