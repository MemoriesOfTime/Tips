package tip.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import tip.commands.base.BaseSubCommand;
import tip.windows.CreateWindow;
import tip.windows.ListenerWindow;

/**
 * @author SmallasWater
 */
public class DefaultSubCommand extends BaseSubCommand {
    public DefaultSubCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            ListenerWindow.CHOSE_TYPE.put(sender.getName(),1);
            CreateWindow.sendSettingType((Player) sender);
            return true;
        } else {
            sender.sendMessage("请不要用控制台执行..");
            return false;
        }
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }


}
