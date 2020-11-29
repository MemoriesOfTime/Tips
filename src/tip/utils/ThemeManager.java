package tip.utils;

import cn.nukkit.utils.Config;
import tip.messages.defaults.MessageManager;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author ZXR
 */
public class ThemeManager extends LinkedHashMap<String, MessageManager> {


    private LinkedHashMap<String, Config> configs = new LinkedHashMap<>();

    public MessageManager put(String key, MessageManager value,Config config) {
        configs.put(key,config);
        return super.put(key, value);
    }

    public LinkedList<String> getNames() {
        return new LinkedList<>(configs.keySet());
    }


    public LinkedList<Config> getConfigs() {
        return new LinkedList<>(configs.values());
    }

    public Config getConfig(String theme) {
        return configs.getOrDefault(theme,null);
    }

    public MessageManager getDefaultManager(){
        return getOrDefault("default",null);
    }
}
