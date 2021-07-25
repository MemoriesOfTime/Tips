package tip.tasks;

import cn.nukkit.Player;
import tip.messages.BaseMessage;
import tip.messages.defaults.BroadcastMessage;
import tip.utils.Api;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public class BroadCastPlayerTask {

    private Player player;
    private LinkedHashMap<BaseMessage,MessageType> type = new LinkedHashMap<>();


    public BroadCastPlayerTask(Player owner) {
        this.player = owner;
    }

    public void onRun() {
        if(player == null || !player.isOnline()){
            return;
        }
        BaseMessage message;
        message = Api.getSendPlayerMessage(player.getName(),player.level.getFolderName(),
                BaseMessage.BaseTypes.BROADCAST);
        if (message != null) {
            if (message.isOpen()) {
                if(!type.containsKey(message)){
                    type.put(message,new MessageType());
                }
                MessageType m = type.get(message);
                if (m.time == -1) {
                    m.time = ((BroadcastMessage)message).getTime();
                    m.key = true;
                } else if (m.time <= 0) {
                    m.i++;
                    m.time = ((BroadcastMessage)message).getTime();
                    m.key = true;
                }
                if (m.i >= ((BroadcastMessage)message).getMessages().size()) {
                    m.i = 0;
                }
                if (m.key) {
                    m.key = false;
                    String text = ((BroadcastMessage)message).getMessages().get(m.i);
                    player.sendMessage(Api.strReplace(text,player));
                }
                if (m.time > 0) {
                    m.time--;
                }
                type.put(message,m);
            }
        }
    }

    private class MessageType{
        public int i = 0;

        public int time = -1;

        boolean key = false;

    }
}
