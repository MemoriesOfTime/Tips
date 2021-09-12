package tip.tasks;


import tip.Main;


/**
 * @author SmallasWater
 */
public class AddPlayerTask {

    public static void add(BaseTipsRunnable task){
        Main.executor.execute(task);
    }
}
