package uk.co.abyxstudioz.referredbyme.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.abyxstudioz.referredbyme.ReferredByMe;

public class ReferInfo implements CommandExecutor {
    private ReferredByMe referredByMe;

    public ReferInfo(ReferredByMe mainPlugin) {
        referredByMe = mainPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            if (referredByMe.getConfig().getString("Players." + args[0].toLowerCase() + ".IP") == null) {
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Name: " + ChatColor.GRAY
                        + args[0].toLowerCase());
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Referred By: " + ChatColor.GRAY
                        + referredByMe.getConfig().getString("Players." + args[0].toLowerCase() + ".ReferredBy"));
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Referrals: " + ChatColor.GRAY
                        + referredByMe.getConfig().getString("Players." + args[0].toLowerCase() + ".Referrals"));
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Rank: " + ChatColor.GRAY
                        + referredByMe.getConfig().getInt("Players." + args[0].toLowerCase() + ".Rank"));
            } else {
                Player player = (Player) sender;
                String NotFound = referredByMe.getConfig().getString("Messages.NotFound")
                        .replace("{player}", player.getName().toLowerCase());
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + NotFound.replace("{target}", args[0].toLowerCase()));
            }
        } else if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Cannot be executed from the console.");
                return false;
            } else {
                Player player = (Player) sender;
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Name: " + ChatColor.GRAY
                        + player.getName().toLowerCase());
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Referred By: " + ChatColor.GRAY
                        + referredByMe.getConfig().getString("Players." + player.getName().toLowerCase() + ".ReferredBy"));
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Referrals: " + ChatColor.GRAY
                        + referredByMe.getConfig().getString("Players." + player.getName().toLowerCase() + ".Referrals"));
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Rank: " + ChatColor.GRAY
                        + referredByMe.getConfig().getInt("Players." + player.getName().toLowerCase() + ".Rank"));
            }
        } else if (args.length > 1) {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + referredByMe.getConfig().getString("Messages.arguments"));
            return false;
        }
        return true;
    }

}
