package tip.messages.defaults;


import tip.messages.BaseMessage;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class ScoreBoardMessage extends BaseMessage {

    private String title;

    private LinkedList<String> messages;

    public ScoreBoardMessage(String worldName, boolean open,String title,LinkedList<String> message) {
        super(worldName, open);
        this.title = title;
        this.messages = message;
    }

    @Override
    public int getType() {
        return SCOREBOARD_TYPE;
    }

    public String getTitle() {
        return title;
    }

    public LinkedList<String> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<String> messages) {
        this.messages = messages;
    }

    @Override
    public LinkedHashMap<String,Object> getConfig(){
        LinkedHashMap<String,Object> objectLinkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<String,Object> sub = new LinkedHashMap<>();
        sub.put("是否开启",isOpen());
        sub.put("Title",getTitle());
        sub.put("Line",getMessages());
        objectLinkedHashMap.put(getWorldName(),sub);
        return objectLinkedHashMap;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
