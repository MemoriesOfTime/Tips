package tip.utils;


import cn.nukkit.utils.BossBarColor;

import java.util.LinkedList;

/**
 * @author 若水
 */
public class BossMessageBuilder {

    private int time;

    private LinkedList<String> strings;

    private boolean health;

    private BossBarColor bossBarColor;

    public BossMessageBuilder(LinkedList<String> strings,int time,boolean health){
        this(strings, time, health, BossBarColor.GREEN);
    }

    public BossMessageBuilder(LinkedList<String> strings, int time, boolean health, BossBarColor bossBarColor){
        this.time = time;
        this.strings = strings;
        this.health = health;
        this.bossBarColor = bossBarColor;
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

    public BossBarColor getBossBarColor() {
        return bossBarColor;
    }
}
