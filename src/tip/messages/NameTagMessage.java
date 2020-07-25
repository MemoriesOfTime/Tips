package tip.messages;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public class NameTagMessage extends BaseMessage {

    private String message;

    public NameTagMessage(String worldName, boolean open,String message) {
        super(worldName, open);
        this.message = message;
    }

    @Override
    public int getType() {
        return NAME_TAG_TYPE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public LinkedHashMap<String,Object> getConfig(){
        LinkedHashMap<String,Object> objectLinkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<String,Object> sub = new LinkedHashMap<>();
        sub.put("open",isOpen());
        sub.put("message",getMessage());
        objectLinkedHashMap.put(getWorldName(),sub);
        return objectLinkedHashMap;
    }
}
