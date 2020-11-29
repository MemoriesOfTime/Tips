package tip.messages;


/**
 * 适配旧版本的
 * @author SmallasWater
 */
@Deprecated
public class TipMessage extends tip.messages.defaults.TipMessage {

    public TipMessage(String worldName, boolean open, int type, String message) {
        super(worldName, open, type, message);
    }
}
