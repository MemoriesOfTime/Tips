package tip.messages.defaults;


import tip.messages.BaseMessage;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class MessageManager extends LinkedList<BaseMessage> {

    public boolean hasExistsType(BaseMessage.BaseTypes types){
        for(BaseMessage message:this){
            if(message.getType() == types.getType()){
                return true;
            }
        }
        return false;
    }

    public void setMessage(BaseMessage message){
        BaseMessage message1 = getMessageByTypeAndWorld(message.getWorldName(),message.getType());
        if(message1 == null){
            this.add(message);
        }else{
            int i = this.indexOf(message1);
            if(i != -1) {
                this.set(i, message);
            }
        }
    }

    public BaseMessage getMessageByTypeAndWorld(String worldName,int type){
        BaseMessage baseMessage = null;
        for(BaseMessage message: this){
            if(message.getWorldName().split("&").length > 1){
                if(Arrays.asList(message.getWorldName().split("&"))
                        .contains(worldName) && type == message.getType()){
                    baseMessage = message;
                }
            }
            if("default".equalsIgnoreCase(message.getWorldName()) && message.getType() == type){
                baseMessage = message;
            }
            if(worldName.equalsIgnoreCase(message.getWorldName()) && type == message.getType()){
                baseMessage =  message;
            }
        }
        return baseMessage;
    }

    public LinkedHashMap<String,Object> saveConfig(){
        LinkedHashMap<String,Object> config = new LinkedHashMap<>();
        for(BaseMessage message:this){
            BaseMessage.BaseTypes types = BaseMessage.getBaseTypeByInteger(message.getType());
            if(types != null){
                config.put(types.getConfigName(),message.getConfig());
            }
        }
        return config;

    }



}
