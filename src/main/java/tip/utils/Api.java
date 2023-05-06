package tip.utils;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;
import org.jetbrains.annotations.NotNull;
import tip.Main;
import tip.messages.BaseMessage;
import tip.messages.defaults.MessageManager;
import tip.utils.variables.BaseVariable;
import tip.utils.variables.VariableManager;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author 若水
 * Tips变量
 */
public final class Api {

    @Deprecated
    private final String string;
    @Deprecated
    private final IPlayer player;

    private static final LinkedHashMap<String, Class<? extends BaseVariable>> VARIABLE = new LinkedHashMap<>();

    @Deprecated
    public Api(String string, IPlayer player) {
        this.string = string;
        this.player = player;
    }

    @Deprecated
    public String strReplace() {
        String m = string;
        m = Main.getInstance().getVarManager().toMessage((Player) player, m);

        return TextFormat.colorize('&', m);
    }

    public static void registerVariables(String name, Class<? extends BaseVariable> variable) {
        if (VARIABLE.containsKey(name)) {
            return;
        }
        VARIABLE.put(name, variable);
        Main.getInstance().setVarManager(flush(variable));
    }

    private static VariableManager flush(Class<? extends BaseVariable> var) {
        VariableManager varManager = Main.getInstance().getVarManager();
        if (Main.getInstance().getVarManager() == null) {
            varManager = new VariableManager();
        }
        BaseVariable variable = Api.initVariable(var);
        if (variable != null) {
            varManager.addVariableClass(variable);
        }
        return varManager;
    }

    private static BaseVariable initVariable(Class<? extends BaseVariable> var) {
        BaseVariable variable = null;
        for (Constructor<?> constructor : var.getConstructors()) {
            try {
                if (constructor.getParameterCount() == 1) {
                    variable = (BaseVariable) constructor.newInstance((Object) null);
                } else {
                    variable = (BaseVariable) constructor.newInstance((Object) null, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return variable;
    }

    public static String strReplace(@NotNull String string, Player player) {
        return TextFormat.colorize('&', Main.getInstance().getVarManager().toMessage(player, string));
    }

    /**
     * 增加单个变量
     */
    public static void addVariable(@NotNull String var, @NotNull String message) {
        Main.getInstance().getVarManager().addVariable(var, message);
    }

    public static BaseMessage getSendPlayerMessage(String playerName, String levelName, BaseMessage.BaseTypes baseTypes) {
        PlayerConfig config = Main.getInstance().getPlayerConfig(playerName);
        BaseMessage message = null;
        if (config != null) {
            message = config.getMessage(levelName, baseTypes.getType());
        }
        if (message == null) {
            message = getLevelDefaultMessage(levelName, baseTypes);
        }

        return message;
    }

    public static BaseMessage getLevelDefaultMessage(String levelName, BaseMessage.BaseTypes baseTypes) {
        return Main.getInstance().getShowMessages().getMessageByTypeAndWorld(levelName, baseTypes.getType());
    }

    public static void setLevelMessage(BaseMessage message) {
        Main.getInstance().getShowMessages().setMessage(message);
    }

    public static LinkedList<String> getSettingLevels() {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("default");
        for (Level level : Server.getInstance().getLevels().values()) {
            if (!"default".equalsIgnoreCase(level.getFolderName())) {
                linkedList.add(level.getFolderName());
            }
        }
        return linkedList;
    }

    public static void setPlayerShowMessage(String playerName, BaseMessage message) {
        PlayerConfig config = Main.getInstance().getPlayerConfigInit(playerName);
        config.setMessage(message);

    }

    public static void removePlayerShowMessage(String playerName, BaseMessage message) {
        PlayerConfig config = Main.getInstance().getPlayerConfig(playerName);
        if (config == null) {
            config = new PlayerConfig(playerName, new MessageManager(), Main.getInstance().getTheme());
        }
        if (config.messages.contains(message)) {
            config.removeMessage(message);
        }
        if (config.messages.size() == 0) {
            Main.getInstance().getPlayerConfigs().remove(config);
        }
    }
}
