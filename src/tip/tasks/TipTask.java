package tip.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import tip.Main;
import tip.utils.SendPlayerClass;

import java.util.LinkedHashMap;


/**
 * @author 若水
 */
public class TipTask extends BaseTipsRunnable {


    private static LinkedHashMap<Player,SendPlayerClass> sendPlayerClassLinkedHashMap = new LinkedHashMap<>();
    public TipTask(Main owner,int sleep) {
        super(owner);
        this.sleep = sleep;

    }
    private int sleep;



    @Override
    public void run() {
        while (true){
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                try {
                    if(!sendPlayerClassLinkedHashMap.containsKey(player)){
                        sendPlayerClassLinkedHashMap.put(player,new SendPlayerClass(player, getOwner()));
                    }
                    sendPlayerClassLinkedHashMap.get(player).init();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

    }





}
