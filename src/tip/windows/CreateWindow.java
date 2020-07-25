package tip.windows;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import tip.Main;
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
        FormWindowSimple simple = new FormWindowSimple(Main.getLanguage("window-setting-title"),Main.getLanguage("window-setting-content"));
        for(Player player1: Server.getInstance().getOnlinePlayers().values()){
            simple.addButton(new ElementButton(player1.getName(), new ElementButtonImageData("path", "textures/ui/Friend2")));
        }
        player.showFormWindow(simple,MENU);
    }

    static void sendSettingType(Player player){
        FormWindowSimple simple = new FormWindowSimple(Main.getLanguage("window-settingType-title"),Main.getLanguage("window-settingType-content"));
        for(BaseMessage.BaseTypes types: BaseMessage.BaseTypes.values()){
            simple.addButton(new ElementButton(types.getConfigName(), new ElementButtonImageData("path", "textures/ui/message")));
        }
        simple.addButton(getBackButton());
        player.showFormWindow(simple,SETTING);
    }

    private static ElementButton getBackButton(){
        return new ElementButton("Back",new ElementButtonImageData("path","textures/ui/refresh_light"));
    }

    static void sendSettingShow(Player player, BaseMessage.BaseTypes type){
        FormWindowCustom custom = new FormWindowCustom(Main.getLanguage("window-settingShow-title"));
        custom.addElement(new ElementDropdown(Main.getLanguage("window-settingShow-drop"), Api.getSettingLevels()));
        custom.addElement(new ElementToggle(Main.getLanguage("window-settingShow-toggle"),true));
        switch (type){
            case TIP:
                custom.setTitle(custom.getTitle()+"-- TipsShow");
                custom.addElement(new ElementDropdown(Main.getLanguage("window-settingType-content"),new LinkedList<String>(){{
                    add("tip");
                    add("popup");
                    add("action");
                }}));
                custom.addElement(new ElementInput(Main.getLanguage("window-settingShow-input")));
                break;
            case BOSS_BAR:
                custom.setTitle(custom.getTitle()+"-- BossHealth");
                custom.addElement(new ElementInput("Place edit time","time","5"));
                custom.addElement(new ElementToggle("Place select Change by Player Health",true));
                custom.addElement(new ElementInput("Place edit it more message use `&`"));
                break;
            case NAME_TAG:
                custom.setTitle(custom.getTitle()+"-- NameTag");
                custom.addElement(new ElementInput("place edit it"));
                break;
            case SCORE_BOARD:
                custom.setTitle(custom.getTitle()+"-- Scoreboard");
                custom.addElement(new ElementInput("Place edit scoreboard title"));
                custom.addElement(new ElementInput("Place edit it more message use `&`"));
                break;
            case CHAT_MESSAGE:
                custom.setTitle(custom.getTitle()+"-- Chat");
                custom.addElement(new ElementToggle("Place select only in world",false));
                custom.addElement(new ElementInput("place edit it"));
                break;
            default:break;
        }
        player.showFormWindow(custom,CHOSE);

    }


}
