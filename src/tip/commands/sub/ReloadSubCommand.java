package tip.commands.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import tip.Main;
import tip.commands.base.BaseSubCommand;

/**
 * @author SmallasWater
 */
public class ReloadSubCommand extends BaseSubCommand {
    public ReloadSubCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Main.getInstance().init();
        sender.sendMessage(TextFormat.YELLOW+"Reload Config success");
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
