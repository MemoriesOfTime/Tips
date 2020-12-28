package tip.utils.variables;


import cn.nukkit.Player;
import tip.utils.variables.defaults.Variable;

import java.util.ArrayList;


/**
 * @author SmallasWater
 */
public abstract class BaseVariable {

    protected Player player;
    protected String string;

    private final ArrayList<Variable> var = new ArrayList<>();

    public BaseVariable(Player player){
        this.player = player;
    }

    public boolean isResetMessage(){
        return false;
    }



    public String getString() {
        return string;
    }

    /**
     * 增加变量
     *
     * */
    protected final void addStrReplaceString(String name,String msg){
        var.add(new Variable(name){
            @Override
            public String value(Object args) {
                return msg;
            }
        });

    }


    protected final void addStrReplaceString(Variable variable){
        var.add(variable);

    }

    public void setString(String string) {
        this.string = string;
    }

    /**
     * 执行 变量转换..(在这个方法里执行添加变量..)
     * */
    public abstract void strReplace();

    public ArrayList<Variable> getVar() {
        return var;
    }


}
