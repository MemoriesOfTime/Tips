package tip.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import tip.commands.base.BaseSubCommand;
import tip.windows.CreateWindow;


/**
 * @author SmallasWater
 */
public class ThemeSubCommand extends BaseSubCommand {
    public ThemeSubCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender.isPlayer()){
            CreateWindow.sendChoseTheme((Player) sender);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("tips.theme");
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
