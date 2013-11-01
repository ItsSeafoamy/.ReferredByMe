package uk.co.abyxstudioz.referredbyme.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.abyxstudioz.referredbyme.ReferredByMe;

public class ReferConfig implements CommandExecutor {
    private ReferredByMe referredByMe;

    public ReferConfig(ReferredByMe mainPlugin) {
        referredByMe = mainPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe]" + referredByMe.getConfig().getString("Messages.arguments"));
            return false;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "/referconfig Help Menu");
            sender.sendMessage(ChatColor.GREEN + "This command allows you to edit the config in-game.");
            sender.sendMessage(ChatColor.GREEN + "Usage: /referconfig <path> [value] [type]");
            sender.sendMessage(ChatColor.GREEN + "The path is the variable you are getting/setting.");
            sender.sendMessage(ChatColor.GREEN + "Indentation in the config file, is replace with dots (.)");
            sender.sendMessage(ChatColor.GREEN
                    + "For example, If you were checking the player, LaXynd's Referral Count, the path would be " + ChatColor.RED
                    + "Players.laxynd.Referrals");
            sender.sendMessage(ChatColor.GREEN + "Path names are case-sensitive. Player names are always set in lower case.");
            sender.sendMessage(ChatColor.GREEN + "The Types are String, Int and Boolean");
            sender.sendMessage(ChatColor.GREEN + "Strings are text. When setting messages, you would use String");
            sender.sendMessage(ChatColor.GREEN + "Use underscores (_) where you want spaces with Strings.");
            sender.sendMessage(ChatColor.GREEN
                    + "Ints (Integers) are numbers (no decimals). When setting how many referrals you need to obtain a reward, use Int");
            sender.sendMessage(ChatColor.GREEN
                    + "Booleans are true or false values. When setting wether you want to check IPs, use Boolean");
            sender.sendMessage(ChatColor.GREEN + "If you have any questions, go to " + ChatColor.WHITE
                    + "http://dev.bukkit.org/server-mods/referredbyme/");
        }
        if (args.length == 1 && !args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + args[0] + ": "
                    + referredByMe.getConfig().getString(args[0]));
        }
        if (args.length == 3) {
            if (args[2].equalsIgnoreCase("String")) {
                referredByMe.getConfig().set(args[0], args[1].replace("_", " "));
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "The variable " + ChatColor.RED
                        + args[0] + ChatColor.GREEN + " has now been set with the value " + ChatColor.RED
                        + args[1].replace("_", " "));
            }
            if (args[2].equalsIgnoreCase("Int")) {
                try {
                    int Setter = Integer.parseInt(args[1]);
                    referredByMe.getConfig().set(args[0], Setter);
                    sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "The variable " + ChatColor.RED
                            + args[0] + ChatColor.GREEN + " has now been set with the value " + ChatColor.RED + Setter);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "[ReferredByMe] Error: Expected Int");
                }
            }
            if (args[2].equalsIgnoreCase("Boolean")) {
                boolean Setter;
                if (args[1].equalsIgnoreCase("true")) {
                    Setter = true;
                    referredByMe.getConfig().set(args[0], Setter);
                    sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "The variable " + args[0]
                            + ChatColor.GREEN + " has now been set with the value " + ChatColor.RED + Setter);
                } else if (args[1].equalsIgnoreCase("false")) {
                    Setter = false;
                    referredByMe.getConfig().set(args[0], Setter);
                    sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "The variable " + args[0]
                            + ChatColor.GREEN + " has now been set with the value " + ChatColor.RED + Setter);
                } else {
                    sender.sendMessage(ChatColor.RED + "[ReferredByMe] Error: Expected Boolean");
                }
            }
            referredByMe.saveConfig();
        }
        if (args.length > 3 || args.length == 2) {
            sender.sendMessage(ChatColor.RED + "[ReferredByMe]" + referredByMe.getConfig().getString("Messages.arguments"));
            return false;
        }
        return true;
    }

}
