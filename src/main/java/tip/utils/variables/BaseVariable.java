package tip.utils.variables;


import cn.nukkit.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;


/**
 * @author SmallasWater
 */
public abstract class BaseVariable {

    protected Player player;
    protected String string;

    private final LinkedHashMap<String, String> var = new LinkedHashMap<>();

    public BaseVariable(Player player) {
        this.player = player;
    }

    public boolean isResetMessage() {
        return false;
    }


    public String getString() {
        return string;
    }

    /**
     * 增加变量
     */
    protected final void addStrReplaceString(@NotNull String key, @NotNull String value) {
        var.put(key, value);

    }

    public void setString(@NotNull String string) {
        this.string = string;
    }

    /**
     * 执行 变量转换..(在这个方法里执行添加变量..)
     */
    public abstract void strReplace();

    public LinkedHashMap<String, String> getVar() {
        return var;
    }


}
