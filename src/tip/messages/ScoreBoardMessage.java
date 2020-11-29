package tip.messages;


import java.util.LinkedList;

/**
 * @author SmallasWater
 */
@Deprecated
public class ScoreBoardMessage extends tip.messages.defaults.ScoreBoardMessage {

    public ScoreBoardMessage(String worldName, boolean open, String title, LinkedList<String> message) {
        super(worldName, open, title, message);
    }
}
