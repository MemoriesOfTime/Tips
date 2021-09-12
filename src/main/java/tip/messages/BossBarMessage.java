package tip.messages;

import tip.utils.BossMessageBuilder;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
@Deprecated
public class BossBarMessage extends tip.messages.defaults.BossBarMessage {


    public BossBarMessage(String worldName, boolean open, int time, boolean size, LinkedList<String> message) {
        super(worldName, open, time, size, message);
    }
}
