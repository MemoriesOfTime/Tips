package tip.utils;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import tip.Main;
import tip.messages.BaseMessage;
import tip.utils.variables.BaseVariable;
import tip.utils.variables.defaults.ChangeMessage;


import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author 若水
 * Tips变量
 * */
public class Api {

    private String string;
    private IPlayer player;

    private static LinkedHashMap<String, Class<? extends BaseVariable>> VARIABLE = new LinkedHashMap<>();
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
        BaseVariable variable;
        String m = string;
        LinkedHashMap<String,String> message = new LinkedHashMap<>();
        for(Class<? extends BaseVariable> var:VARIABLE.values()){
            Constructor[] constructors = var.getConstructors();
            for (Constructor constructor : constructors) {
                try {
                    try {
                        if(constructor.getParameterCount() == 1){
                            variable = (BaseVariable) constructor.newInstance(player);
                        }else{
                            variable = (BaseVariable) constructor.newInstance(player,m);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return string;
                    }
                    variable.strReplace();
                    if(var.isAnnotationPresent(ChangeMessage.class)){
                        m = variable.getString();
                    }
                    message.putAll(variable.getVar());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for(String key:message.keySet()){
            m = m.replace(key,message.get(key));
        }
        return m;
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
