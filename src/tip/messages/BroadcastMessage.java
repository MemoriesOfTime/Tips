package tip.messages;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
@Deprecated
public class BroadcastMessage extends tip.messages.defaults.BroadcastMessage {


    public BroadcastMessage(String worldName, boolean open, int time, LinkedList<String> message) {
        super(worldName, open, time, message);
    }
}
