package tip.commands.sub;

import cn.nukkit.Achievement;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.form.window.FormWindowSimple;
import com.smallaswater.achievement.Achievements;
import tip.commands.base.BaseSubCommand;

/**
 * @author SmallasWater
 */
public class AchAllSubCommand extends BaseSubCommand {

    public AchAllSubCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            FormWindowSimple simple = new FormWindowSimple("§e§l成就系统", "");
            StringBuilder builder = new StringBuilder();
            builder.append("§7成就进度: §2").append(((Player) sender).achievements.size()).append("§r/§7").append(Achievement.achievements.size()).append("§r\n");
            for (String s1 : Achievement.achievements.keySet()) {
                String msg = Achievement.achievements.get(s1).message;
                if (Server.getInstance().getPluginManager().getPlugin("Achievements") != null) {
                    String a1 = Achievements.getString(msg);
                    if (!"".equals(a1)) {
                        msg = a1;
                    } else {
                        a1 = Achievements.getString(s1);
                        if (!"".equals(a1)) {
                            msg = a1;
                        }
                    }
                }
                builder.append(msg).append("§e: —— ");
                if (!((Player) sender).hasAchievement(s1)) {
                    builder.append("§c§l✘§r\n\n");
                } else {
                    builder.append("§a§l√§r\n\n");
                }
            }

            simple.setContent(builder.toString());
            ((Player) sender).showFormWindow(simple);
        }
        return true;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("tips.achall");
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
