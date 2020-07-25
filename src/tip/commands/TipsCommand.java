package tip.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import tip.Main;
import tip.commands.base.BaseCommand;
import tip.commands.sub.AchAllSubCommand;
import tip.commands.sub.ReloadSubCommand;
import tip.commands.sub.SendSubCommand;
import tip.windows.CreateWindow;

/**
 * @author SmallasWater
 */
public class TipsCommand extends BaseCommand {

    public TipsCommand(String name) {
        super(name,"");
        this.setPermission("tips.ts");
        this.setAliases(Main.getInstance().getConfig().getStringList("Command.aliases").toArray(new String[0]));
        this.description = Main.getInstance().getConfig().getString("Command.description","Custom player tips");
        this.addSubCommand(new AchAllSubCommand("achAll"));
        this.addSubCommand(new ReloadSubCommand("reload"));
        this.addSubCommand(new SendSubCommand("send"));
        this.loadCommandBase();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("tips.ts");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(hasPermission(sender)) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    CreateWindow.sendSetting((Player) sender);
                    return true;
                } else {
                    sender.sendMessage(Main.getLanguage("run-Console"));
                    return false;
                }
            }
            return super.execute(sender, s, args);
        }
        return true;
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§a====================");
        sender.sendMessage("§e/"+getName()+" §eOpen Setting Player Shows GUI");
        sender.sendMessage("§e/"+getName()+" §7reload §e Reload Config");
        sender.sendMessage("§e/"+getName()+" §7achAll §eOpen AchievementGUI");
        sender.sendMessage("§e/"+getName()+" §7send <type> <message>§esend message to player\n§type: tip,popup,action,title,msg");
        sender.sendMessage("§a====================");
    }
}
