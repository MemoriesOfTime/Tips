package tip.utils.variables;

import cn.nukkit.Player;
import tip.Main;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SmallasWater
 */
public final class VariableManager {

    private static final Pattern variablePattern = Pattern.compile("\\{([^}]+)}");

    private static final Map<String, Function<Map<String, String>, String>> templateCache = new ConcurrentHashMap<>();

    public static Function<Map<String, String>, String> compile(String template) {
        // 检查缓存中是否已存在
        return templateCache.computeIfAbsent(template, VariableManager::createTemplateFunction);
    }

    private static Function<Map<String, String>, String> createTemplateFunction(String template) {
        // 分析模板并构建函数
        StringBuilder functionCode = new StringBuilder();
        int lastIndex = 0;
        Matcher matcher = variablePattern.matcher(template);

        while (matcher.find()) {
            functionCode.append('"').append(template, lastIndex, matcher.start()).append("\" + ");
            functionCode.append("data.get(\"").append(matcher.group(1)).append("\") + ");
            lastIndex = matcher.end();
        }
        functionCode.append('"').append(template.substring(lastIndex)).append('"');

        // 生成 Lambda 表达式
        String finalExpression = functionCode.toString();
        return data -> {
            try {
                return (String) finalExpression;
            } catch (Exception e) {
                return "Error in template processing: " + e.getMessage();
            }
        };
    }

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

        return compile(message).apply(variables);
    }

}
