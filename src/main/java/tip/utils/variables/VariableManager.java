package tip.utils.variables;

import cn.nukkit.Player;
import tip.Main;

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
        if (msg == null) {
            return "";
        }
        for (BaseVariable variable : variablesClass) {
            try {
                variable.player = player;
                variable.string = msg;
                variable.strReplace();
                variables.putAll(variable.getVar());
            } catch (Throwable e) {
                Main.getInstance().getLogger().error("VariablesClass: " + variable.getClass().getName() + " Error executing strReplace() method!", e);
            }
        }
        variables.putAll(otherVariables);

        try {
            return ASMTemplateCompiler.strReplace(msg, variables);
        } catch (Throwable e) {
            Main.getInstance().getLogger().error("Error executing ASMTemplateCompiler.strReplace() method!", e);
            String message = msg;
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                message = message.replace(entry.getKey(), entry.getValue());
            }
            return message;
        }
    }

}
