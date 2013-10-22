package uk.co.abyxstudioz.referredbyme.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.abyxstudioz.referredbyme.ReferredByMe;

public class ReferClaim implements CommandExecutor {
    private ReferredByMe referredByMe;

    public ReferClaim(ReferredByMe mainPlugin) {
        referredByMe = mainPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        Player player = (Player) sender;
        boolean claimed = false;
        for (int i = 0; i < referredByMe.getConfig().getInt("Rewards.Tiers"); i++) {
            int j = i + 1;
            if (referredByMe.getConfig().getBoolean("Players." + player.getName().toLowerCase() + ".Claimable." + j)) {
                List<String> CommandL = referredByMe.getConfig().getStringList("Rewards." + j + ".Command");
                for (String command : CommandL) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                            command.replace("{target}", player.getName().toLowerCase()));
                }
                referredByMe.getConfig().set("Players." + player.getName().toLowerCase() + ".Claimable." + j, false);
                claimed = true;
                referredByMe.saveConfig();
            }
        }
        for (int i = 0; i < referredByMe.getConfig().getInt("Players." + player.getName().toLowerCase() + ".Claimable.RCount"); i++) {
            List<String> CommandL = referredByMe.getConfig().getStringList("Rewards.Recurring.Command");
            for (String command : CommandL) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                        command.replace("{target}", player.getName().toLowerCase()));
            }
            referredByMe.getConfig().set("Players." + player.getName().toLowerCase() + ".Claimable.RCount", 0);
            claimed = true;
            referredByMe.saveConfig();
        }
        if (claimed) {
            player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN
                    + referredByMe.getConfig().getString("Messages.Claimed").replace("{player}", player.getName().toLowerCase()));
        } else {
            player.sendMessage(ChatColor.RED
                    + "[ReferredByMe] "
                    + ChatColor.GREEN
                    + referredByMe.getConfig().getString("Messages.NotClaimed")
                            .replace("{player}", player.getName().toLowerCase()));
        }
        return true;
    }

}
