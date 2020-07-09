package tip.lib.viewcompass;

import cn.nukkit.Player;
import tip.utils.variables.BaseVariable;

/**
 * 本类引用 @PetteriM1 作者 的ViewCompass 方法
 * @author from PetteriM1
 * */
public class ViewCompassVariable extends BaseVariable {


    private static final String[] COMPASS = new String[36];

    public ViewCompassVariable(Player player) {
        super(player);
        init();
    }
    private void init(){
        COMPASS[0] = "\u00A77|  |  |  |  |  \u00A7l\u00A71南\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[1] = "\u00A77|  |  |  |  \u00A7l\u00A71南\u00A7r\u00A77  |  |  |  |  |  |";
        COMPASS[2] = "\u00A77|  |  |  \u00A7l\u00A71南\u00A7r\u00A77  |  |  |  |  \u00A7l\u00A7f西南\u00A7r\u00A77  |  |";
        COMPASS[3] = "\u00A77|  |  |  |  |  |  \u00A7l\u00A7f西南\u00A7r\u00A77  |  |  |  |";
        COMPASS[4] = "\u00A77|  |  |  |  |  \u00A7l\u00A7f西南\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[5] = "\u00A77|  |  |  |  \u00A7l\u00A7f西南\u00A7r\u00A77  |  |  |  |  |  |";
        COMPASS[6] = "\u00A77|  |  \u00A7l\u00A7f西南\u00A7r\u00A77  |  |  |  |  \u00A7l\u00A7a西\u00A7r\u00A77  |  |  |";
        COMPASS[7] = "\u00A77|  |  |  |  |  |  \u00A7l\u00A7a西\u00A7r\u00A77  |  |  |  |";
        COMPASS[8] = "\u00A77|  |  |  |  |  \u00A7l\u00A7a西\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[9] = "\u00A77|  |  |  |  |  \u00A7l\u00A7a西\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[10] = "\u00A77|  |  |  |  \u00A7l\u00A7a西\u00A7r\u00A77  |  |  |  |  |  |";
        COMPASS[11] = "\u00A77|  |  |  \u00A7l\u00A7a西\u00A7r\u00A77  |  |  |  |  \u00A7l\u00A7f西北\u00A7r\u00A77  |  |";
        COMPASS[12] = "\u00A77|  |  |  |  |  |  \u00A7l\u00A7f西北\u00A7r\u00A77  |  |  |  |";
        COMPASS[13] = "\u00A77|  |  |  |  |  \u00A7l\u00A7f西北\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[14] = "\u00A77|  |  |  |  \u00A7l\u00A7f西北\u00A7r\u00A77  |  |  |  |  |  |";
        COMPASS[15] = "\u00A77|  |  \u00A7l\u00A7f西北\u00A7r\u00A77  |  |  |  |  \u00A7l\u00A7c北\u00A7r\u00A77  |  |  |";
        COMPASS[16] = "\u00A77|  |  |  |  |  |  \u00A7l\u00A7c北\u00A7r\u00A77  |  |  |  |";
        COMPASS[17] = "\u00A77|  |  |  |  |  \u00A7l\u00A7c北\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[18] = "\u00A77|  |  |  |  |  \u00A7l\u00A7c北\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[19] = "\u00A77|  |  |  |  \u00A7l\u00A7c北\u00A7r\u00A77  |  |  |  |  |  |";
        COMPASS[20] = "\u00A77|  |  |  \u00A7l\u00A7c北\u00A7r\u00A77  |  |  |  |  \u00A7l\u00A7f东北\u00A7r\u00A77  |  |";
        COMPASS[21] = "\u00A77|  |  |  |  |  |  \u00A7l\u00A7f东北\u00A7r\u00A77  |  |  |  |";
        COMPASS[22] = "\u00A77|  |  |  |  |  \u00A7l\u00A7f东北\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[23] = "\u00A77|  |  |  |  \u00A7l\u00A7f东北\u00A7r\u00A77  |  |  |  |  |  |";
        COMPASS[24] = "\u00A77|  |  \u00A7l\u00A7f东北\u00A7r\u00A77  |  |  |  |  \u00A7l\u00A7e东\u00A7r\u00A77  |  |  |";
        COMPASS[25] = "\u00A77|  |  |  |  |  |  |  \u00A7l\u00A7e东\u00A7r\u00A77  |  |  |";
        COMPASS[26] = "\u00A77|  |  |  |  |  |  \u00A7l\u00A7e东\u00A7r\u00A77  |  |  |  |";
        COMPASS[27] = "\u00A77|  |  |  |  |  \u00A7l\u00A7e东\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[28] = "\u00A77|  |  |  |  \u00A7l\u00A7e东\u00A7r\u00A77  |  |  |  |  |  |";
        COMPASS[29] = "\u00A77|  |  |  \u00A7l\u00A7e东\u00A7r\u00A77  |  |  |  |  \u00A7l\u00A7f东南\u00A7r\u00A77  |  |";
        COMPASS[30] = "\u00A77|  |  |  |  |  |  \u00A7l\u00A7f东南\u00A7r\u00A77  |  |  |  |";
        COMPASS[31] = "\u00A77|  |  |  |  |  \u00A7l\u00A7f东南\u00A7r\u00A77  |  |  |  |  |";
        COMPASS[32] = "\u00A77|  |  |  |  \u00A7l\u00A7f东南\u00A7r\u00A77  |  |  |  |  |  |";
        COMPASS[33] = "\u00A77|  |  \u00A7l\u00A7f东南\u00A7r\u00A77  |  |  |  |  \u00A7l\u00A71南\u00A7r\u00A77  |  |  |";
        COMPASS[34] = "\u00A77|  |  |  |  |  |  |  \u00A7l\u00A71南\u00A7r\u00A77  |  |  |";
        COMPASS[35] = "\u00A77|  |  |  |  |  |  \u00A7l\u00A71南\u00A7r\u00A77  |  |  |  |";
    }

    private String getCompass(double direction) {
        direction = direction + Math.ceil(-direction / 360) * 360;
        direction = direction * 2 / 10 / 2;
        return COMPASS[Math.round((long) direction)];
    }

    @Override
    public void strReplace() {
        addStrReplaceString("{view}",getCompass(player.getYaw()));
    }
}
