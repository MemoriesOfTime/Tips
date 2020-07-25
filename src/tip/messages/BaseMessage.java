package tip.messages;

import tip.Main;


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

    private String worldName;


    private boolean open;

    BaseMessage(String worldName, boolean open){
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


    public static BaseMessage getMessageByTypeAndWorld(String worldName,int type){
        BaseMessage baseMessage = null;
        for(BaseMessage message: Main.getInstance().getShowMessages()){
            if("default".equals(message.worldName) && message.getType() == type){
                baseMessage = message;
            }
            if(worldName.equalsIgnoreCase(message.worldName) && type == message.getType()){
                baseMessage =  message;
            }

        }
        return baseMessage;
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
        BOSS_BAR(0,"BossHealth"),
        CHAT_MESSAGE(1,"Chat"),
        NAME_TAG(2,"NameTag"),
        SCORE_BOARD(3,"Scoreboard"),
        TIP(4,"Tips");
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
