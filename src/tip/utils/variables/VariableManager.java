package tip.utils.variables;

import cn.nukkit.Player;
import tip.utils.variables.defaults.Variable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 可以直接通过Main调用
 * @author SmallasWater
 */
public class VariableManager {

    public void addVariableClass(BaseVariable variable){
        variablesClass.add(variable);
    }

    /**
     * 增加变量
     * */
    public void addVariable(Variable variable){
        otherVariables.add(variable);
    }
    private ArrayList<Variable> otherVariables = new ArrayList<>();

    private ArrayList<Variable> variables = new ArrayList<>();

    private LinkedList<BaseVariable> variablesClass = new LinkedList<>();

    public String format(String in){
        return format(null,in);
    }

    public String format(Player player,String msg){
        String message = msg;
        if(message == null){
            return "";
        }
        for(BaseVariable variable: variablesClass){
            variable.player = player;
            variable.string = msg;
            variable.strReplace();
            if(variable.isResetMessage()){
                message = variable.getString();
            }
            variables.addAll(variable.getVar());
            variables.addAll(otherVariables);
        }
        for (Variable var : variables) {
            message = message.replace(var.getName(), var.value(player));
        }
        return message;
    }

}
