package uk.co.abyxstudioz.referredbyme.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.abyxstudioz.referredbyme.ReferredByMe;

public class Refer implements CommandExecutor {
    private ReferredByMe referredByMe;

    public Refer(ReferredByMe mainPlugin) {
        referredByMe = mainPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length != 1) {
            String arguments = referredByMe.getConfig().getString("Messages.arguments")
                    .replace("{player}", sender.getName().toLowerCase());
            sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + arguments);
            return false;
        } else if (!(sender instanceof Player)) {
            sender.sendMessage("Cannot be executed from the console.");
            return false;
        } else {
            if (!referredByMe.getConfig().getBoolean("Players." + sender.getName().toLowerCase() + ".Referred")) {
                if (referredByMe.getConfig().getInt("Players." + sender.getName().toLowerCase() + ".Referrals") == 0) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (!sender.getName().toLowerCase().equalsIgnoreCase(args[0].toLowerCase())) {
                        if (referredByMe.getConfig().getString("Players." + args[0].toLowerCase() + ".IP") != null) {
                            boolean checkIP;
                            try {
                                if (!referredByMe
                                        .getConfig()
                                        .getString("Players." + sender.getName().toLowerCase() + ".IP")
                                        .equalsIgnoreCase(
                                                referredByMe.getConfig().getString("Players." + args[0].toLowerCase() + ".IP"))) {
                                    checkIP = true;
                                } else {
                                    checkIP = false;
                                }
                            } catch (Exception e) {
                                referredByMe.getLogger().warning("Failed to veify IPs.");
                                referredByMe.getLogger().warning("One or both of the IPs returned the value null.");
                                referredByMe.getLogger().warning(
                                        "Check the config to make sure each player's IP isn't blank nor null.");
                                checkIP = true;
                            }
                            if (!referredByMe.getConfig().getBoolean("IpCheck")) {
                                checkIP = true;
                            }
                            if (checkIP) {
                                referredByMe.getConfig().set("Players." + sender.getName().toLowerCase() + ".Referred", true);
                                referredByMe.getConfig().set("Players." + sender.getName().toLowerCase() + ".ReferredBy",
                                        args[0].toLowerCase());
                                int count = referredByMe.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals");
                                referredByMe.getConfig().set("Players." + args[0].toLowerCase() + ".Referrals", count + 1);
                                if (referredByMe.getConfig().getInt("Players." + sender.getName().toLowerCase() + ".Referrals") == 0) {
                                    String Referred = referredByMe.getConfig().getString("Messages.Referred")
                                            .replace("{player}", sender.getName().toLowerCase());
                                    sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN
                                            + Referred.replace("{target}", args[0].toLowerCase()));
                                    int Rank = referredByMe.getConfig().getInt("Players." + args[0].toLowerCase() + ".Rank");
                                    int Rankers = Rank - 1;
                                    if (referredByMe.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals") >= referredByMe
                                            .getConfig().getInt("Rank." + Rankers + ".Referrals") && Rankers != 0) {
                                        referredByMe.getConfig().set("Rank." + Rank + ".Name",
                                                referredByMe.getConfig().getString("Rank." + Rankers + ".Name"));
                                        referredByMe.getConfig().set("Rank." + Rank + ".Referrals",
                                                referredByMe.getConfig().getInt("Rank." + Rankers + ".Referrals"));
                                        referredByMe.getConfig().set(
                                                "Players." + referredByMe.getConfig().getString("Rank." + Rankers + ".Name")
                                                        + ".Rank", Rank);
                                        referredByMe.getConfig().set("Rank." + Rankers + ".Name", args[0].toLowerCase());
                                        referredByMe.getConfig().set(
                                                "Rank." + Rankers + ".Referrals",
                                                referredByMe.getConfig()
                                                        .getInt("Players." + args[0].toLowerCase() + ".Referrals"));
                                        referredByMe.getConfig().set("Players." + args[0].toLowerCase() + ".Rank", Rankers);
                                    }
                                    referredByMe.saveConfig();
                                    if (target != null) {
                                        String YouReferred = referredByMe.getConfig().getString("Messages.YouReferred")
                                                .replace("{player}", sender.getName().toLowerCase());
                                        target.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN
                                                + YouReferred.replace("{target}", args[0].toLowerCase()));
                                    }
                                    for (int i = 0; i < referredByMe.getConfig().getInt("Rewards.Tiers"); i++) {
                                        int j = i + 1;
                                        referredByMe.getLogger().info("Tier: " + j);
                                        referredByMe.getLogger().info(
                                                "Referrals: "
                                                        + referredByMe.getConfig().getInt(
                                                                "Players." + args[0].toLowerCase() + ".Referrals"));
                                        referredByMe.getLogger().info(
                                                "Required Referrals: "
                                                        + referredByMe.getConfig().getInt("Rewards." + j + ".Referrals"));
                                        if (referredByMe.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals") == referredByMe
                                                .getConfig().getInt("Rewards." + j + ".Referrals")) {
                                            referredByMe.getConfig().set("Players." + args[0].toLowerCase() + ".Claimable." + j,
                                                    true);
                                            referredByMe.getLogger().info(
                                                    "Claimable: "
                                                            + referredByMe.getConfig().getBoolean(
                                                                    "Players." + args[0].toLowerCase() + ".Claimable." + j));
                                        }
                                    }
                                    if (referredByMe.getConfig().getInt("Rewards.Recurring.Referrals") != 0) {
                                        if ((referredByMe.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals") / referredByMe
                                                .getConfig().getInt("Rewards.Recurring.Referrals"))
                                                * referredByMe.getConfig().getInt("Rewards.Recurring.Referrals") == referredByMe
                                                .getConfig().getInt("Players." + args[0] + ".Referrals")) {
                                            referredByMe.getConfig().set(
                                                    "Players." + args[0].toLowerCase() + ".Claimable.RCount",
                                                    referredByMe.getConfig().getInt(
                                                            "Players." + args[0].toLowerCase() + ".Claimable.RCount") + 1);
                                        }
                                    }
                                    referredByMe.saveConfig();
                                } else {
                                    String ReferSelf = referredByMe.getConfig().getString("Messages.ReferSelf")
                                            .replace("{player}", sender.getName().toLowerCase());
                                    sender.sendMessage(ChatColor.RED + "[ReferredByMe] "
                                            + ReferSelf.replace("{target}", args[0].toLowerCase()));
                                    referredByMe.getConfig()
                                            .set("Players." + sender.getName().toLowerCase() + ".Referred", false);
                                    referredByMe.getConfig().set("Players." + sender.getName().toLowerCase() + ".ReferredBy",
                                            null);
                                    referredByMe.getConfig().set("Players." + sender.getName().toLowerCase() + ".Referrals", 0);
                                    referredByMe.saveConfig();
                                    return false;
                                }
                            } else {
                                String ReferSelf = referredByMe.getConfig().getString("Messages.ReferSelf")
                                        .replace("{player}", sender.getName().toLowerCase());
                                sender.sendMessage(ChatColor.RED + "[ReferredByMe] "
                                        + ReferSelf.replace("{target}", args[0].toLowerCase()));
                            }
                        } else {
                            String NotFound = referredByMe.getConfig().getString("Messages.NotFound")
                                    .replace("{player}", sender.getName().toLowerCase());
                            sender.sendMessage(ChatColor.RED + "[ReferredByMe] "
                                    + NotFound.replace("{target}", args[0].toLowerCase()));
                            return false;
                        }
                    } else {
                        String ReferSelf = referredByMe.getConfig().getString("Messages.ReferSelf")
                                .replace("{player}", sender.getName().toLowerCase());
                        sender.sendMessage(ChatColor.RED + "[ReferredByMe] "
                                + ReferSelf.replace("{target}", args[0].toLowerCase()));
                        return false;
                    }
                } else {
                    String ReferLoop = referredByMe.getConfig().getString("Messages.ReferLoop")
                            .replace("{player}", sender.getName().toLowerCase());
                    sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ReferLoop.replace("{target}", args[0].toLowerCase()));
                    return false;
                }
            } else {
                String AlreadyReferred = referredByMe.getConfig().getString("Messages.AlreadyReferred")
                        .replace("{player}", sender.getName().toLowerCase());
                sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + AlreadyReferred.replace("{target}", args[0].toLowerCase()));
                return false;
            }
        }
        return true;
    }

}
