package uk.co.abyxstudioz.referredbyme.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.abyxstudioz.referredbyme.ReferredByMe;

public class ReferReload implements CommandExecutor {
    private ReferredByMe referredByMe;

    public ReferReload(ReferredByMe mainPlugin) {
        referredByMe = mainPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 0) {
            referredByMe.reloadConfig();
            String reload = referredByMe.getConfig().getString("Messages.reload");
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + reload);
            if (referredByMe.getConfig().getDouble("Version") < 0.6) {
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.DARK_RED + "You are using Config Version "
                        + ChatColor.RED + referredByMe.getConfig().getDouble("Version") + ChatColor.DARK_RED
                        + " with ReferredByMe Version " + ChatColor.RED + "0.7" + ChatColor.DARK_RED
                        + ". Please update your config to follow " + ChatColor.WHITE
                        + "http://dev.bukkit.org/bukkit-plugins/referredbyme/pages/example-config/");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + referredByMe.getConfig().getString("Messages.arguments"));
            return false;
        }
        return true;
    }

}
