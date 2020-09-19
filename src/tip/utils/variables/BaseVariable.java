package tip.utils.variables;


import cn.nukkit.Player;

import java.util.LinkedHashMap;


/**
 * @author SmallasWater
 */
public abstract class BaseVariable {


    protected final Player player;


    protected String string;

    private final LinkedHashMap<String,String> var = new LinkedHashMap<>();

    public BaseVariable(Player player){
        this.player = player;
    }




    public String getString() {
        return string;
    }

    /**
     * 增加变量
     *
     * */
    protected final void addStrReplaceString(String key, String value){
        var.put(key, value);

    }

    public void setString(String string) {
        this.string = string;
    }

    /**
     * 执行 变量转换..(在这个方法里执行添加变量..)
     * */
    public abstract void strReplace();

    public LinkedHashMap<String, String> getVar() {
        return var;
    }
}
