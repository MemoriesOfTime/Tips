package tip.messages;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class BroadcastMessage extends BaseMessage{

    private int time;

    private LinkedList<String> messages;
    public BroadcastMessage(String worldName, boolean open, int time, LinkedList<String> message) {
        super(worldName, open);
        this.time = time;
        this.messages = message;
    }

    @Override
    public int getType() {
        return BROAD_CAST_TYPE;
    }

    public LinkedList<String> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<String> message) {
        this.messages = message;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public LinkedHashMap<String, Object> getConfig() {
        LinkedHashMap<String,Object> objectLinkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<String,Object> sub = new LinkedHashMap<>();
        sub.put("是否开启",isOpen());
        sub.put("间隔时间",getTime());
        sub.put("消息轮播",getMessages());
        objectLinkedHashMap.put(getWorldName(),sub);
        return objectLinkedHashMap;
    }
}
