package uk.co.abyxstudioz.referredbyme;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.abyxstudioz.referredbyme.commands.*;

public class ReferredByMe extends JavaPlugin implements Listener {

    private boolean update;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getLogger().info("ReferredByMe has been enabled.");
        getLogger().info("Author: LaXynd");
        getLogger().info("Version: V0.7");
        getServer().getPluginManager().registerEvents(this, this);
        if (getConfig().getDouble("Version") < 0.6) {
            update = true;
            this.saveConfig();
        } else {
            update = false;
            getConfig().set("Version", 0.7);
            this.saveConfig();
        }
        getCommand("refer").setExecutor(new Refer(this));
        getCommand("referclaim").setExecutor(new ReferClaim(this));
        getCommand("referconfig").setExecutor(new ReferConfig(this));
        getCommand("referinfo").setExecutor(new ReferInfo(this));
        getCommand("referleader").setExecutor(new ReferLeader(this));
        getCommand("referreload").setExecutor(new ReferReload(this));
        getCommand("referversion").setExecutor(new ReferVersion(this));
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();
        if (!getConfig().getBoolean("Players." + player.getName().toLowerCase() + ".Referred")) {
            if (getConfig().getString("Players." + player.getName().toLowerCase() + ".IP") == null) {
                if (!getConfig().getString("Messages.WelcomeMessage").equals("")) {
                    String WelcomeMessage = getConfig().getString("Messages.WelcomeMessage").replace("{player}",
                            player.getName().toLowerCase());
                    player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + WelcomeMessage);
                    getConfig().set("Players." + player.getName().toLowerCase() + ".IP",
                            player.getAddress().getAddress().getHostAddress());
                }
                this.saveConfig();
            }
            if (!getConfig().getString("Messages.WhoReferred").equals("")) {
                String WhoReferred = getConfig().getString("Messages.WhoReferred").replace("{player}",
                        player.getName().toLowerCase());
                player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + WhoReferred);
            }
        }
        if (getConfig().getInt("Players." + player.getName().toLowerCase() + ".Rank") == 0) {
            getConfig().set("Rank.Ranks", getConfig().getInt("Rank.Ranks") + 1);
            getConfig().set("Players." + player.getName().toLowerCase() + ".Rank", getConfig().getInt("Rank.Ranks"));
            getConfig().set("Rank." + getConfig().getInt("Rank.Ranks") + ".Name", player.getName().toLowerCase());
            getConfig().set("Rank." + getConfig().getInt("Rank.Ranks") + ".Referrals",
                    getConfig().getInt("Players." + player.getName().toLowerCase() + ".Referrals"));
            this.saveConfig();
        }
        int Rank = getConfig().getInt("Players." + player.getName().toLowerCase() + ".Rank");
        int Rankers = Rank - 1;
        if (getConfig().getInt("Players." + player.getName().toLowerCase() + ".Referrals") >= ReferredByMe.this.getConfig()
                .getInt("Rank." + Rankers + ".Referrals") && Rankers != 0) {
            getConfig().set("Rank." + Rank + ".Name", getConfig().getString("Rank." + Rankers + ".Name"));
            getConfig().set("Rank." + Rank + ".Referrals", getConfig().getInt("Rank." + Rankers + ".Referrals"));
            getConfig().set("Players." + getConfig().getString("Rank." + Rankers + ".Name") + ".Rank", Rank);
            getConfig().set("Rank." + Rankers + ".Name", player.getName().toLowerCase());
            getConfig().set("Rank." + Rankers + ".Referrals",
                    getConfig().getInt("Players." + player.getName().toLowerCase() + ".Referrals"));
            getConfig().set("Players." + player.getName().toLowerCase() + ".Rank", Rankers);
            this.saveConfig();
        }
        if (!getConfig().getString("Messages.ReferElse").equals("")) {
            String ReferElse = getConfig().getString("Messages.ReferElse").replace("{player}", player.getName().toLowerCase());
            player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + ReferElse);
        }
        if (update && player.isOp()) {
            player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.DARK_RED + "You are using Config Version "
                    + ChatColor.RED + getConfig().getDouble("Version") + ChatColor.DARK_RED + " with ReferredByMe Version "
                    + ChatColor.RED + "0.7" + ChatColor.DARK_RED + ". Please update your config to follow " + ChatColor.WHITE
                    + "http://dev.bukkit.org/bukkit-plugins/referredbyme/pages/example-config/");
        }
        for (int i = 0; i < getConfig().getInt("Rewards.Tiers"); i++) {
            int j = i + 1;
            if (getConfig().getBoolean("Players." + player.getName().toLowerCase() + ".Claimable." + j)) {
                player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN
                        + getConfig().getString("Messages.Claimable"));
                break;
            }
        }
        if (getConfig().getInt("Players." + player.getName().toLowerCase() + ".Claimable.RCount") != 0) {
            player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + getConfig().getString("Messages.Claimable"));
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[referrank]")) {
            event.setLine(0, "§1[referrank]");
            event.setLine(2, "§4" + getConfig().getString("Rank." + event.getLine(1) + ".Name"));
            event.setLine(3, "§4" + getConfig().getString("Rank." + event.getLine(1) + ".Referrals"));
            event.setLine(1, "§2" + event.getLine(1));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Player player = event.getPlayer();
                Sign sign = (Sign) event.getClickedBlock().getState();
                String signline0 = sign.getLine(0);
                String signLine1 = sign.getLine(1);
                if (signline0.equalsIgnoreCase("[referrank]")) {
                    sign.setLine(0, "§1[referrank]");
                    sign.setLine(1, "§2" + signLine1.replace("§2", ""));
                    sign.setLine(2, "§4" + getConfig().getString("Rank." + signLine1.replace("§2", "") + ".Name"));
                    sign.setLine(3, "§4" + getConfig().getString("Rank." + signLine1.replace("§2", "") + ".Referrals"));
                    player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Updated");
                    sign.update(true);
                }
            }
        }
    }
}
