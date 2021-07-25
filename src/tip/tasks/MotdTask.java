package tip.tasks;


import tip.Main;
import tip.utils.Api;


/**
 * @author SmallasWater
 * Create on 2020/12/2 19:06
 * Package tip.tasks
 */
public class MotdTask extends BaseTipsRunnable {
    public MotdTask(Main owner) {
        super(owner);
    }

    @Override
    public void run() {
        Api api;
        while (true) {
            api = new Api(owner.getMotd(),new FakeIPlayer());
            owner.getServer().getNetwork().setName(api.strReplace());
            try {
                Thread.sleep(getOwner().getConfig().getInt("自定义刷新刻度.motd",20) * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
