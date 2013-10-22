package uk.co.abyxstudioz.referredbyme.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.abyxstudioz.referredbyme.ReferredByMe;

public class ReferVersion implements CommandExecutor {
    private ReferredByMe referredByMe;

    public ReferVersion(ReferredByMe mainPlugin) {
        referredByMe = mainPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Author: " + ChatColor.GRAY + "LaXynd");
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Version: " + ChatColor.GREEN + "V0.7");
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Build Type: " + ChatColor.YELLOW
                    + "Development");
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "For Minecraft Version: " + ChatColor.GRAY
                    + "1.6.2");
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Released: " + ChatColor.GRAY + "28.07.13");
        } else {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + referredByMe.getConfig().getString("Messages.arguments"));
        }
        return true;
    }

}
