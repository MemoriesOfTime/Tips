package tip.utils.variables;

import cn.nukkit.Player;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class VariableManager {

    public void addVariableClass(BaseVariable variable){
        variablesClass.add(variable);
    }

    /**
     * 增加变量
     * */
    public void addVariable(String var,String message){
        otherVariables.put(var,message);
    }
    private LinkedHashMap<String,String> otherVariables = new LinkedHashMap<>();

    private LinkedHashMap<String,String> variables = new LinkedHashMap<>();

    private LinkedList<BaseVariable> variablesClass = new LinkedList<>();

    public String toMessage(Player player,String msg){
        String message = msg;
        if(message == null){
            return "";
        }
        for(BaseVariable variable: variablesClass){
            variable.player = player;
            variable.string = msg;
            variable.strReplace();
//            if(variable.isResetMessage()){
//                message = variable.getString();
//            }
            variables.putAll(variable.getVar());
            variables.putAll(otherVariables);
        }

        for(String s: variables.keySet()){
            message = message.replace(s,variables.get(s));
        }
        return message;
    }

}
