package tip.messages;

import tip.utils.BossMessageBuilder;


import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class BossBarMessage extends BaseMessage{


    private int time;

    private LinkedList<String> messages;

    private boolean size;

    public BossBarMessage(String worldName, boolean open,int time,boolean size,LinkedList<String> message) {
        super(worldName, open);
        this.time = time;
        this.messages = message;
        this.size = size;
    }

    public BossMessageBuilder getBuilder(){
        return new BossMessageBuilder(messages,time,size);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setSize(boolean size) {
        this.size = size;
    }


    @Override
    public int getType() {
        return BOSS_BAR_TYPE;
    }

    public boolean isSize() {
        return size;
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
        sub.put("open",isOpen());
        sub.put("time",getTime());
        sub.put("changeByHealth",isSize());
        sub.put("messages",getMessages());
        objectLinkedHashMap.put(getWorldName(),sub);
        return objectLinkedHashMap;
    }
}
