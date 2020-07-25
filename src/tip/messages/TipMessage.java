package tip.messages;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public class TipMessage extends BaseMessage{

    public static final int TIP = 0;

    public static final int POPUP = 1;

    public static final int ACTION = 2;


    private int type;

    private String message;

    public TipMessage(String worldName, boolean open,int type,String message) {
        super(worldName, open);
        this.message = message;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int getType() {
        return TIP_MESSAGE_TYPE;
    }

    public int getShowType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public LinkedHashMap<String,Object> getConfig(){
        LinkedHashMap<String,Object> objectLinkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<String,Object> sub = new LinkedHashMap<>();
        sub.put("open",isOpen());
        sub.put("type",getShowType());
        sub.put("message",getMessage());
        objectLinkedHashMap.put(getWorldName(),sub);
        return objectLinkedHashMap;
    }
}
