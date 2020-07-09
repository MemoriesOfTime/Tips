package tip.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import tip.commands.base.BaseSubCommand;

import java.util.Arrays;

/**
 * @author SmallasWater
 */
public class SendSubCommand extends BaseSubCommand {
    public SendSubCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    private final static String[] TYPES = new String[]{"tip","popup","action","title","msg"};
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length > 2){
            String playerName = args[1];
            Player player = Server.getInstance().getPlayer(playerName);
            if(player != null){
                String type = args[2].toLowerCase();
                if(Arrays.asList(TYPES).contains(type)){
                    String msg = args[3];
                    switch (type){
                        case "tip":
                            player.sendTip(msg);
                            break;
                        case "action":
                            player.sendActionBar(msg);
                            break;
                        case "popup":
                            player.sendPopup(msg);
                            break;
                        case "title":
                            String[] strings = msg.split("&");
                            String title = strings[0];
                            StringBuilder s = new StringBuilder();
                            if(strings.length > 1){
                                for(int i = 1;i < strings.length;i++){
                                    s.append(strings[i]).append("\n");
                                }
                            }
                            player.sendTitle(title, s.toString());
                            break;
                        case "msg":
                            player.sendMessage(msg);
                            break;
                            default:break;
                    }
                    sender.sendMessage("发送成功");
                    return true;
                }else{
                    sender.sendMessage("请使用支持的类型: "+Arrays.asList(TYPES));
                }

            }else{
                sender.sendMessage("玩家 "+playerName+"不在线");
                return true;
            }
        }
        return false;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET,true),
                new CommandParameter("type", TYPES),
                new CommandParameter("message",CommandParamType.TEXT,true)
        };
    }
}
