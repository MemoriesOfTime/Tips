package tip.utils;

import cn.nukkit.utils.Config;
import tip.Main;
import tip.messages.BaseMessage;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class PlayerConfig {

    public LinkedList<BaseMessage> messages;

    private String playerName;

    public PlayerConfig(String playerName,LinkedList<BaseMessage>  baseMessages){
        this.playerName = playerName;
        this.messages = baseMessages;
    }



    public String getPlayerName() {
        return playerName;
    }

    public void addMessage(BaseMessage message){
        messages.add(message);
    }



    public void removeMessage(BaseMessage message){
        messages.remove(message);
    }

    public void setMessage(BaseMessage message){
        BaseMessage message1 = getMessage(message.getWorldName(),message.getType());
        if(message1 == null){
            messages.add(message);
        }else{
            int i = messages.indexOf(message1);
            if(i != -1) {
                messages.set(i, message);
            }else{
                messages.add(message);
            }
        }
    }

    public BaseMessage getMessage(String levelName,int type){
        BaseMessage getMessage = null;
        for(BaseMessage baseMessage:messages){
            if("default".equalsIgnoreCase(baseMessage.getWorldName()) && baseMessage.getType() == type){
                getMessage = baseMessage;
            }
            if(baseMessage.getWorldName().equalsIgnoreCase(levelName) && baseMessage.getType() == type){
                getMessage = baseMessage;
            }
        }

        return getMessage;
    }

    private LinkedList<BaseMessage> getMessagesByType(int type){
        LinkedList<BaseMessage> messages = new LinkedList<>();
        for(BaseMessage baseMessage:this.messages){
            if(baseMessage.getType() == type){
                messages.add(baseMessage);
            }
        }
        return messages;
    }

    public LinkedList<BaseMessage> getMessages() {
        return messages;
    }

    public void save(){
       Config config = new Config(Main.getInstance().getDataFolder()+"/Players/"+playerName+".yml",2);
       LinkedHashMap<String,Object> map = new LinkedHashMap<>();
       for(BaseMessage.BaseTypes types: BaseMessage.BaseTypes.values()){
           LinkedList<BaseMessage> linkedList = getMessagesByType(types.getType());
           LinkedHashMap<String,Object> sub = new LinkedHashMap<>();
           if(linkedList.size() > 0){
               for(BaseMessage message:linkedList){
                   sub.putAll(message.getConfig());
               }
               map.put(types.getConfigName(),sub);
           }
       }
       config.setAll(map);
       config.save();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlayerConfig){
            return ((PlayerConfig) obj).playerName.equals(playerName);
        }
        return false;
    }
}
