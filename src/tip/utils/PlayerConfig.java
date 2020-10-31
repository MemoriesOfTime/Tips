package tip.utils;

import cn.nukkit.utils.Config;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.MessageManager;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class PlayerConfig {

    public MessageManager messages;

    private String playerName;

    public PlayerConfig(String playerName,MessageManager  baseMessages,String theme){
        this.playerName = playerName;
        this.messages = baseMessages;
        this.theme = theme;
    }


    private String theme;



    public String getPlayerName() {
        return playerName;
    }

    public void addMessage(BaseMessage message){
        messages.add(message);
    }


    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void removeMessage(BaseMessage message){
        messages.remove(message);
    }

    public void setMessage(BaseMessage message){
        messages.setMessage(message);

    }

    public BaseMessage getMessage(String levelName,int type){
        BaseMessage message = null;
        if(!"default".equalsIgnoreCase(theme)){
            MessageManager message1 = Main.getInstance().getThemeManager().get(theme);
            if(message1 != null){
                message =  message1.getMessageByTypeAndWorld(levelName, type);
            }
        }
        if(message == null){
            message = messages.getMessageByTypeAndWorld(levelName, type);
        }
        return message;
    }

    private MessageManager getMessagesByType(int type){
        MessageManager messages = new MessageManager();
        MessageManager baseMessages = this.messages;
        if(!"default".equalsIgnoreCase(theme)){
            baseMessages = Main.getInstance().getThemeManager().get(theme);
            if(baseMessages == null){
                baseMessages = this.messages;
            }
        }
        for(BaseMessage baseMessage:baseMessages){
            if(baseMessage.getType() == type){
                messages.add(baseMessage);
            }
        }
        return messages;
    }

    public MessageManager getMessages() {
        return messages;
    }

    public void save(){
       Config config = new Config(Main.getInstance().getDataFolder()+"/Players/"+playerName+".yml",2);
       LinkedHashMap<String,Object> map = new LinkedHashMap<>();
       map.put("样式",theme);
       for(BaseMessage.BaseTypes types: BaseMessage.BaseTypes.values()){
           MessageManager linkedList = getMessagesByType(types.getType());
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
