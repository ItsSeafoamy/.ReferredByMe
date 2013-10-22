package uk.co.abyxstudioz.referredbyme.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.abyxstudioz.referredbyme.ReferredByMe;

public class ReferLeader implements CommandExecutor {
    private ReferredByMe referredByMe;

    public ReferLeader(ReferredByMe mainPlugin) {
        referredByMe = mainPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GOLD + "Rank: 1");
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GOLD + "Name: " + ChatColor.GRAY
                    + referredByMe.getConfig().getString("Rank.1.Name"));
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GOLD + "Referrals: " + ChatColor.GRAY
                    + referredByMe.getConfig().getString("Rank.1.Referrals"));
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GRAY + "Rank: 2");
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GRAY + "Name: " + ChatColor.GRAY
                    + referredByMe.getConfig().getString("Rank.2.Name"));
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GRAY + "Referrals: " + ChatColor.GRAY
                    + referredByMe.getConfig().getString("Rank.2.Referrals"));
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.DARK_RED + "Rank: 3");
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.DARK_RED + "Name: " + ChatColor.GRAY
                    + referredByMe.getConfig().getString("Rank.3.Name"));
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.DARK_RED + "Referrals: " + ChatColor.GRAY
                    + referredByMe.getConfig().getString("Rank.3.Referrals"));
        } else if (args.length == 1) {
            ChatColor rank = ChatColor.GREEN;
            if (args[0].equals(1)) {
                rank = ChatColor.GOLD;
            } else if (args[0].equals(2)) {
                rank = ChatColor.GRAY;
            } else if (args[0].equals(3)) {
                rank = ChatColor.DARK_RED;
            } else {
                rank = ChatColor.GREEN;
            }
            sender.sendMessage(ChatColor.RED + "[RederredByMe] " + rank + "Rank: " + args[0]);
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + rank + "Name: " + ChatColor.GRAY
                    + referredByMe.getConfig().getString("Rank." + args[0] + ".Name"));
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + rank + "Referrals: " + ChatColor.GRAY
                    + referredByMe.getConfig().getString("Rank." + args[0] + ".Referrals"));
        } else {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + referredByMe.getConfig().getString("Messages.arguments"));
            return false;
        }
        return true;
    }

}
