package tip.utils.variables;

import cn.nukkit.Player;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author SmallasWater
 */
public final class VariableManager {

    public void addVariableClass(BaseVariable variable) {
        variablesClass.add(variable);
    }

    /**
     * 增加变量
     */
    public void addVariable(String var, String message) {
        otherVariables.put(var, message);
    }

    private final LinkedList<BaseVariable> variablesClass = new LinkedList<>();

    private final LinkedHashMap<String, String> otherVariables = new LinkedHashMap<>();

    private final LinkedHashMap<String, String> variables = new LinkedHashMap<>();

    public synchronized String toMessage(Player player, String msg) {
        String message = msg;
        if (message == null) {
            return "";
        }
        for (BaseVariable variable : variablesClass) {
            variable.player = player;
            variable.string = msg;
            variable.strReplace();
            variables.putAll(variable.getVar());
        }
        variables.putAll(otherVariables);

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

}
