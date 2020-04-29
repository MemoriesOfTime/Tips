package tip.messages;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public class ChatMessage extends BaseMessage{

    private String message;

    private boolean inWorld;

    public ChatMessage(String worldName, boolean open, String message, boolean inWorld) {
        super(worldName, open);
        this.message = message;
        this.inWorld = inWorld;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int getType() {
        return CHAT_MESSAGE_TYPE;
    }

    public boolean isInWorld() {
        return inWorld;
    }

    public void setInWorld(boolean inWorld) {
        this.inWorld = inWorld;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public LinkedHashMap<String,Object> getConfig(){
        LinkedHashMap<String,Object> objectLinkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<String,Object> sub = new LinkedHashMap<>();
        sub.put("是否开启",isOpen());
        sub.put("显示",getMessage());
        sub.put("是否仅在世界内有效",isInWorld());
        objectLinkedHashMap.put(getWorldName(),sub);
        return objectLinkedHashMap;
    }
}
