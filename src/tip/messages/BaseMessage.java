package tip.messages;


import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public abstract class BaseMessage {

    public static final int BOSS_BAR_TYPE = 0;

    public static final int CHAT_MESSAGE_TYPE = 1;

    public static final int NAME_TAG_TYPE = 2;

    public static final int SCOREBOARD_TYPE = 3;

    public static final int TIP_MESSAGE_TYPE = 4;

    public static final int BROAD_CAST_TYPE = 5;

    private String worldName;


    private boolean open;

    public BaseMessage(String worldName, boolean open){
        this.worldName = worldName;
        this.open = open;
    }

    public int getType(){
        return -1;
    }



    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getWorldName() {
        return worldName;
    }

    public boolean isOpen() {
        return open;
    }


    public void setOpen(boolean open) {
        this.open = open;
    }



    /**
     * 保存在配置的..
     * @return 配置内容
     * */
    abstract public LinkedHashMap<String,Object> getConfig();

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BaseMessage){
            return ((BaseMessage) obj).getWorldName().equalsIgnoreCase(getWorldName())
                    && ((BaseMessage) obj).getType() == getType();
        }
        return false;
    }

    public static BaseTypes getBaseTypeByInteger(int type){
        for(BaseTypes types:BaseTypes.values()){
            if(types.getType() == type){
                return types;
            }
        }
        return null;
    }

    public static BaseTypes getTypeByName(String name){
        for(BaseTypes types:BaseTypes.values()){
            if(types.getConfigName().equalsIgnoreCase(name)){
                return types;
            }
        }
        return null;
    }



    public enum BaseTypes{
        /**显示类型*/
        BOSS_BAR(0,"Boss血条"),
        CHAT_MESSAGE(1,"聊天"),
        NAME_TAG(2,"头部"),
        SCORE_BOARD(3,"计分板"),
        TIP(4,"底部"),
        BROADCAST(5,"聊天栏公告");
        protected int type;
        protected String configName;
        BaseTypes(int type,String configName){
            this.type = type;
            this.configName = configName;
        }

        public String getConfigName() {
            return configName;
        }

        public int getType() {
            return type;
        }
    }
}
