package tip.utils;


import java.util.LinkedList;

/**
 * @author 若水
 */
public class BossMessageBuilder {

    private int time;

    private LinkedList<String> strings;

    private boolean health;

    public BossMessageBuilder(LinkedList<String> strings,int time,boolean health){
        this.time = time;
        this.strings = strings;
        this.health = health;
    }

    public int getTime() {
        return time;
    }

    public LinkedList<String> getStrings() {
        return strings;
    }

    public boolean isHealth() {
        return health;
    }
}
