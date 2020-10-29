package tip.messages;


import java.util.LinkedList;

/**
 * @author ZXR
 */
public class MessageManager extends LinkedList<BaseMessage> {

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
            if("default".equalsIgnoreCase(message.getWorldName()) && message.getType() == type){
                baseMessage = message;
            }
            if(worldName.equalsIgnoreCase(message.getWorldName()) && type == message.getType()){
                baseMessage =  message;
            }
        }
        return baseMessage;
    }



}
