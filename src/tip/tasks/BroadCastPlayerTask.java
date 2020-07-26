package tip.tasks;

import cn.nukkit.Player;
import tip.messages.BaseMessage;
import tip.messages.BroadcastMessage;
import tip.utils.Api;

class BroadCastPlayerTask {
    private int i = 0;

    private int time = -1;

    private Player player;

    private boolean key = false;

    BroadCastPlayerTask(Player owner) {
        this.player = owner;
    }

    void onRun() {
        if(player == null || !player.isOnline()){
            return;
        }
        BroadcastMessage message;
        message = (BroadcastMessage) Api.getSendPlayerMessage(player.getName(),player.level.getFolderName(),
                BaseMessage.BaseTypes.BROADCAST);
        if (message != null) {
            if (message.isOpen()) {
                if (time == -1) {
                    time = message.getTime();
                    key = true;
                } else if (time <= 0) {
                    this.i++;
                    time = message.getTime();
                    key = true;
                }
                if (this.i >= message.getMessages().size()) {
                    this.i = 0;
                }
                if (key) {
                    key = false;
                    String text = message.getMessages().get(this.i);
                    Api api = new Api(text, player);
                    text = api.strReplace();
                    player.sendMessage(text);

                }
                if (time > 0) {
                    time--;
                }
            }
        }
    }
}
