package tip.messages;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
@Deprecated
public class ChatMessage extends tip.messages.defaults.ChatMessage {


    public ChatMessage(String worldName, boolean open, String message, boolean inWorld) {
        super(worldName, open, message, inWorld);
    }
}
