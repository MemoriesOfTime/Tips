package tip.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import tip.Main;
import tip.commands.base.BaseCommand;
import tip.commands.sub.*;
import tip.windows.CreateWindow;
import tip.windows.ListenerWindow;

/**
 * @author SmallasWater
 */
public class TipsCommand extends BaseCommand {

    public TipsCommand(String name) {
        super(name,"");
        this.setPermission("tips.ts");
        this.setAliases(Main.getInstance().getConfig().getStringList("自定义指令.aliases").toArray(new String[0]));
        this.description = Main.getInstance().getConfig().getString("自定义指令.description","自定义玩家提示");
        this.addSubCommand(new AchAllSubCommand("achAll"));
        this.addSubCommand(new ReloadSubCommand("reload"));
        this.addSubCommand(new SendSubCommand("send"));
        this.addSubCommand(new DefaultSubCommand("default"));
        this.addSubCommand(new ThemeSubCommand("theme"));
        this.loadCommandBase();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("tips.ts");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(hasPermission(sender)) {
            if(sender.isOp()){
                if (args.length == 0) {
                    if (sender instanceof Player) {
                        ListenerWindow.CHOSE_TYPE.put(sender.getName(),0);
                        CreateWindow.sendSetting((Player) sender);
                        return true;
                    } else {
                        sender.sendMessage("请不要用控制台执行..");
                        return false;
                    }
                }
            }else{
                if (args.length == 0) {
                    return false;
                }
            }
        }else{
            if (args.length == 0) {
                return false;
            }
        }
        return super.execute(sender, s, args);
    }

    @Override
    public void sendHelp(CommandSender sender) {
        if(sender.isOp()){
            sender.sendMessage("§a====================");
            sender.sendMessage("§e/"+getName()+" §e打开设置玩家显示GUI");
            sender.sendMessage("§e/"+getName()+" §7default §e打开设置默认显示GUI");
            sender.sendMessage("§e/"+getName()+" §7me §e打开设置默认显示GUI");
            sender.sendMessage("§e/"+getName()+" §7reload §e重新读取配置");
            sender.sendMessage("§e/"+getName()+" §7achAll §e打开成就GUI");
            sender.sendMessage("§e/"+getName()+" §7theme §e打开设置样式GUI");
            sender.sendMessage("§e/"+getName()+" §7send <玩家> <类型> <信息>§e给玩家发送消息\n§r类型: tip,popup,action,title,msg");
            sender.sendMessage("§a====================");
        }else{
            sender.sendMessage("§a====================");
            if(sender.hasPermission("tips.me")){
                sender.sendMessage("§e/"+getName()+" §7me §e打开设置默认显示GUI");
            }
            sender.sendMessage("§e/"+getName()+" §7achAll §e打开成就GUI");
            sender.sendMessage("§e/"+getName()+" §7theme §e打开设置样式GUI");
            sender.sendMessage("§a====================");
        }

    }
}
