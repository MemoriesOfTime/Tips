package tip.windows;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import tip.messages.BaseMessage;
import tip.utils.Api;

import java.util.LinkedList;


/**
 * @author SmallasWater
 */
public class CreateWindow {


    static int MENU = 0x123Ac01;
    static int SETTING = 0x123Ac02;
    static int CHOSE = 0x123Ac03;

    public static void sendSetting(Player player){
        FormWindowSimple simple = new FormWindowSimple("玩家列表","请选择你要修改的玩家");
        for(Player player1: Server.getInstance().getOnlinePlayers().values()){
            simple.addButton(new ElementButton(player1.getName(), new ElementButtonImageData("path", "textures/ui/Friend2")));
        }
        player.showFormWindow(simple,MENU);
    }

    static void sendSettingType(Player player){
        FormWindowSimple simple = new FormWindowSimple("显示类型","请选择你要修改显示类型");
        for(BaseMessage.BaseTypes types: BaseMessage.BaseTypes.values()){
            simple.addButton(new ElementButton(types.getConfigName(), new ElementButtonImageData("path", "textures/ui/message")));
        }
        simple.addButton(getBackButton());
        player.showFormWindow(simple,SETTING);
    }

    private static ElementButton getBackButton(){
        return new ElementButton("返回",new ElementButtonImageData("path","textures/ui/refresh_light"));
    }

    static void sendSettingShow(Player player, BaseMessage.BaseTypes type){
        FormWindowCustom custom = new FormWindowCustom("显示设置");
        custom.addElement(new ElementDropdown("请选择覆盖的地图 (default 为全地图覆盖)", Api.getSettingLevels()));
        custom.addElement(new ElementToggle("请选择是否开启显示",true));
        switch (type){
            case TIP:
                custom.setTitle(custom.getTitle()+"-- 底部显示");
                custom.addElement(new ElementDropdown("请选择显示类型",new LinkedList<String>(){{
                    add("tip");
                    add("popup");
                    add("action");
                }}));
                custom.addElement(new ElementInput("请编辑显示内容","可空 变量参考变量文件"));
                break;
            case BOSS_BAR:
                custom.setTitle(custom.getTitle()+"-- Boss血条");
                custom.addElement(new ElementInput("请编辑轮播时间(秒)","例如 5","5"));
                custom.addElement(new ElementToggle("请选择是否根据血量变化",true));
                custom.addElement(new ElementInput("请编辑显示内容 轮播内容请用 & 隔开","可空 变量参考变量文件"));
                break;
            case NAME_TAG:
                custom.setTitle(custom.getTitle()+"-- 头部显示");
                custom.addElement(new ElementInput("请编辑显示内容","可空 变量参考变量文件"));
                break;
            case SCORE_BOARD:
                custom.setTitle(custom.getTitle()+"-- 计分板");
                custom.addElement(new ElementInput("请编辑计分板标题","可空 变量参考变量文件"));
                custom.addElement(new ElementInput("请编辑显示内容 个计分板内容请用 & 隔开","可空 变量参考变量文件"));
                break;
            case CHAT_MESSAGE:
                custom.setTitle(custom.getTitle()+"-- 聊天显示");
                custom.addElement(new ElementToggle("请选择是否只在世界内聊天",false));
                custom.addElement(new ElementInput("请编辑聊天内容","可空 变量参考变量文件"));
                break;
            default:break;
        }
        player.showFormWindow(custom,CHOSE);

    }


}
