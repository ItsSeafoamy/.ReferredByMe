package uk.co.abyxstudioz.referredbyme;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ReferredByMe extends JavaPlugin implements Listener{

	@Override
	public void onEnable(){
		this.saveDefaultConfig();
		getLogger().info("ReferredByMe has been enabled.");
		getLogger().info("Author: LaXynd");
		getLogger().info("Version: V0.5");
		getServer().getPluginManager().registerEvents(this, this);
		if (ReferredByMe.this.getConfig().getDouble("Version") != 0.5){
			ReferredByMe.this.getConfig().set("Version", 0.5);
			this.saveConfig();
		}
	}

	@Override
	public void onDisable() {
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent evt) {
		Player player = evt.getPlayer();
		if (!ReferredByMe.this.getConfig().getBoolean("Players." + player.getPlayerListName().toLowerCase() + ".Referred")){
			if(!ReferredByMe.this.getConfig().getBoolean("Players." + player.getPlayerListName().toLowerCase() + ".exists")){
				if (!ReferredByMe.this.getConfig().getString("WelcomeMessage").equals("")){
					String WelcomeMessage = ReferredByMe.this.getConfig().getString("WelcomeMessage").replace("{player}", player.getPlayerListName().toLowerCase());
					player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + WelcomeMessage);
				}
				ReferredByMe.this.getConfig().set("Players." + player.getPlayerListName().toLowerCase() + ".exists", true);
				this.saveConfig();
			}
			if (!ReferredByMe.this.getConfig().getString("WhoReferred").equals("")){
				String WhoReferred = ReferredByMe.this.getConfig().getString("WhoReferred").replace("{player}", player.getPlayerListName().toLowerCase());
				player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + WhoReferred);
			}
		}
		if (ReferredByMe.this.getConfig().getInt("Players." + player.getPlayerListName().toLowerCase() + ".Rank") == 0){
			ReferredByMe.this.getConfig().set("Rank.Ranks", ReferredByMe.this.getConfig().getInt("Rank.Ranks") + 1);
			ReferredByMe.this.getConfig().set("Players." + player.getPlayerListName().toLowerCase() + ".Rank", ReferredByMe.this.getConfig().getInt("Rank.Ranks"));
			ReferredByMe.this.getConfig().set("Rank." + ReferredByMe.this.getConfig().getInt("Rank.Ranks") + ".Name", player.getPlayerListName().toLowerCase());
			ReferredByMe.this.getConfig().set("Rank." + ReferredByMe.this.getConfig().getInt("Rank.Ranks") + ".Referrals", ReferredByMe.this.getConfig().getInt("Players." + player.getPlayerListName().toLowerCase() + ".Referrals"));
			this.saveConfig();
		}
		int Rank = ReferredByMe.this.getConfig().getInt("Players." + player.getPlayerListName().toLowerCase() + ".Rank");
		int Rankers = Rank - 1;
		if (ReferredByMe.this.getConfig().getInt("Players." + player.getPlayerListName().toLowerCase() + ".Referrals") >= ReferredByMe.this.getConfig().getInt("Rank." + Rankers + ".Referrals") && Rankers != 0){
			ReferredByMe.this.getConfig().set("Rank." + Rank + ".Name", ReferredByMe.this.getConfig().getString("Rank." + Rankers + ".Name"));
			ReferredByMe.this.getConfig().set("Rank." + Rank + ".Referrals", ReferredByMe.this.getConfig().getInt("Rank." + Rankers + ".Referrals"));
			ReferredByMe.this.getConfig().set("Players." + ReferredByMe.this.getConfig().getString("Rank." + Rankers + ".Name") + ".Rank", Rank);
			ReferredByMe.this.getConfig().set("Rank." + Rankers + ".Name", player.getPlayerListName().toLowerCase());
			ReferredByMe.this.getConfig().set("Rank." + Rankers + ".Referrals", ReferredByMe.this.getConfig().getInt("Players." + player.getPlayerListName().toLowerCase() + ".Referrals"));
			ReferredByMe.this.getConfig().set("Players." + player.getPlayerListName().toLowerCase() + ".Rank", Rankers);
			this.saveConfig();
		}
		if (!ReferredByMe.this.getConfig().getString("ReferElse").equals("")){
			String ReferElse = ReferredByMe.this.getConfig().getString("ReferElse").replace("{player}", player.getPlayerListName().toLowerCase());
			player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + ReferElse);
		}
	}

	@EventHandler
	public void playerLogin(PlayerLoginEvent evt) {
		Player player = evt.getPlayer();
		String IP = evt.getAddress().getHostAddress();
		ReferredByMe.this.getConfig().set("Players." + player.getPlayerListName().toLowerCase() + ".IP", "'" + IP + "'");
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if(event.getLine(0).contains("[referrank]")){
			event.setLine(0, "§1[referrank]");
			event.setLine(2, "§4" + ReferredByMe.this.getConfig().getString("Rank." + event.getLine(1) + ".Name"));
			event.setLine(3, "§4" + ReferredByMe.this.getConfig().getString("Rank." + event.getLine(1) + ".Referrals"));
			event.setLine(1, "§2" + event.getLine(1));
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			try {
				Player player = event.getPlayer();
				Block block = event.getClickedBlock();
				BlockState state = block.getState();
				Sign sign = (Sign)state;
				String signline0 = sign.getLine(0);
				if(signline0.contains("[referrank]")){
					sign.setLine(0, "§1[referrank]");
					sign.setLine(2, "§4" + ReferredByMe.this.getConfig().getString("Rank." + sign.getLine(1).replace("§2","") + ".Name"));
					sign.setLine(3, "§4" + ReferredByMe.this.getConfig().getString("Rank." + sign.getLine(1).replace("§2","") + ".Referrals"));
					sign.setLine(1, "§2" + sign.getLine(1).replace("§2", ""));
					player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Updated");
					sign.update(true);
				}
			}catch (Exception e){
				
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("refer")){
			Player player = (Player) sender;
			if (args.length != 1) {
				String arguments =  ReferredByMe.this.getConfig().getString("arguments").replace("{player}", player.getPlayerListName().toLowerCase());
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + arguments);
				return false;
			} 
			else if (!(sender instanceof Player)) {
				sender.sendMessage("Cannot be executed from the console.");
				return false;
			} else {
				if (!ReferredByMe.this.getConfig().getBoolean("Players." + player.getPlayerListName().toLowerCase() + ".Referred")){
					if (ReferredByMe.this.getConfig().getInt("Players." + player.getPlayerListName().toLowerCase() + ".Referrals") == 0){
						Player target = Bukkit.getServer().getPlayer(args[0]);
						if (!player.getPlayerListName().toLowerCase().equalsIgnoreCase(args[0].toLowerCase())){
							if (ReferredByMe.this.getConfig().getBoolean("Players." + args[0].toLowerCase() + ".exists")){
								boolean IpCheck;
								try {
									if (!ReferredByMe.this.getConfig().getString("Players." + player.getPlayerListName().toLowerCase() + ".IP").equalsIgnoreCase(ReferredByMe.this.getConfig().getString("Players." + args[0].toLowerCase() + ".IP"))){
										IpCheck = true;
									}else {
										IpCheck = false;
									}
								}catch (Exception e){
									getLogger().warning("Failed to veify IPs.");
									getLogger().warning("One or both of the IPs returned the value null.");
									getLogger().warning("Check the config to make sure each player's IP isn't blank nor null.");
									IpCheck = true;
								}
								if (!ReferredByMe.this.getConfig().getBoolean("IpCheck")){
									IpCheck = true;
								}
								if (IpCheck){
									ReferredByMe.this.getConfig().set("Players." + player.getPlayerListName().toLowerCase() + ".Referred", true);
									ReferredByMe.this.getConfig().set("Players." + player.getPlayerListName().toLowerCase() + ".ReferredBy", args[0].toLowerCase());
									int count = ReferredByMe.this.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals");
									ReferredByMe.this.getConfig().set("Players." + args[0].toLowerCase() + ".Referrals", count + 1);
									if (ReferredByMe.this.getConfig().getInt("Players." + player.getPlayerListName().toLowerCase() + ".Referrals") == 0){
										String Referred = ReferredByMe.this.getConfig().getString("Referred").replace("{player}", player.getPlayerListName().toLowerCase());
										sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + Referred.replace("{target}", args[0].toLowerCase()));
										int Rank = ReferredByMe.this.getConfig().getInt("Players." + args[0].toLowerCase() + ".Rank");
										int Rankers = Rank - 1;
										if (ReferredByMe.this.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals") >= ReferredByMe.this.getConfig().getInt("Rank." + Rankers + ".Referrals") && Rankers != 0){
											ReferredByMe.this.getConfig().set("Rank." + Rank + ".Name", ReferredByMe.this.getConfig().getString("Rank." + Rankers + ".Name"));
											ReferredByMe.this.getConfig().set("Rank." + Rank + ".Referrals", ReferredByMe.this.getConfig().getInt("Rank." + Rankers + ".Referrals"));
											ReferredByMe.this.getConfig().set("Players." + ReferredByMe.this.getConfig().getString("Rank." + Rankers + ".Name") + ".Rank", Rank);
											ReferredByMe.this.getConfig().set("Rank." + Rankers + ".Name", args[0].toLowerCase());
											ReferredByMe.this.getConfig().set("Rank." + Rankers + ".Referrals", ReferredByMe.this.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals"));
											ReferredByMe.this.getConfig().set("Players." + args[0].toLowerCase() + ".Rank", Rankers);
										}
										this.saveConfig();
										if (target != null) {
											String YouReferred = ReferredByMe.this.getConfig().getString("YouReferred").replace("{player}", player.getPlayerListName().toLowerCase());
											target.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + YouReferred.replace("{target}", args[0].toLowerCase()));
										} if (ReferredByMe.this.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals") == ReferredByMe.this.getConfig().getInt("Rewards.Tier1")){
											String Command1 = ReferredByMe.this.getConfig().getString("Rewards.Command1").replace("{player}", player.getPlayerListName().toLowerCase());
											Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Command1.replace("{target}", args[0].toLowerCase()));
										} if (ReferredByMe.this.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals") == ReferredByMe.this.getConfig().getInt("Rewards.Tier2")){
											String Command2 = ReferredByMe.this.getConfig().getString("Rewards.Command2").replace("{player}", player.getPlayerListName().toLowerCase());
											Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Command2.replace("{target}", args[0].toLowerCase()));
										} if (ReferredByMe.this.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals") == ReferredByMe.this.getConfig().getInt("Rewards.Tier3")){
											String Command3 = ReferredByMe.this.getConfig().getString("Rewards.Command3").replace("{player}", player.getPlayerListName().toLowerCase());
											Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Command3.replace("{target}", args[0].toLowerCase()));
										} if (ReferredByMe.this.getConfig().getInt("Rewards.Recurring") != 0){
											if ((ReferredByMe.this.getConfig().getInt("Players." + args[0].toLowerCase() + ".Referrals") / ReferredByMe.this.getConfig().getInt("Rewards.Recurring")) * ReferredByMe.this.getConfig().getInt("Rewards.Recurring") == ReferredByMe.this.getConfig().getInt("Players." + args[0] + ".Referrals")){
												String Commandr = ReferredByMe.this.getConfig().getString("Rewards.Commandr").replace("{player}", player.getPlayerListName().toLowerCase());
												Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Commandr.replace("{target}", args[0].toLowerCase()));
											}	
										}
									} else {
										String ReferSelf = ReferredByMe.this.getConfig().getString("ReferSelf").replace("{player}", player.getPlayerListName().toLowerCase());
										player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ReferSelf.replace("{target}", args[0].toLowerCase()));
										ReferredByMe.this.getConfig().set("Players." + player.getPlayerListName().toLowerCase() + ".Referred", false);
										ReferredByMe.this.getConfig().set("Players." + player.getPlayerListName().toLowerCase() + ".ReferredBy", null);
										ReferredByMe.this.getConfig().set("Players." + player.getPlayerListName().toLowerCase() + ".Referrals", 0);
										return false;
									}
								} else {
									String ReferSelf = ReferredByMe.this.getConfig().getString("ReferSelf").replace("{player}", player.getPlayerListName().toLowerCase());
									player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ReferSelf.replace("{target}", args[0].toLowerCase()));
								}
							} else {
								String NotFound = ReferredByMe.this.getConfig().getString("NotFound").replace("{player}", player.getPlayerListName().toLowerCase());
								player.sendMessage(ChatColor.RED + "[ReferredByMe] " + NotFound.replace("{target}", args[0].toLowerCase()));
								return false;
							}
						}else {
							String ReferSelf = ReferredByMe.this.getConfig().getString("ReferSelf").replace("{player}", player.getPlayerListName().toLowerCase());
							player.sendMessage(ChatColor.RED + "[ReferredByMe] " + ReferSelf.replace("{target}", args[0].toLowerCase()));
							return false;
						}
					}else {
						String ReferLoop = ReferredByMe.this.getConfig().getString("ReferLoop").replace("{player}", player.getPlayerListName().toLowerCase());
						sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ReferLoop.replace("{target}", args[0].toLowerCase()));
						return false;
					}
				}else {
					String AlreadyReferred =  ReferredByMe.this.getConfig().getString("AlreadyReferred").replace("{player}", player.getPlayerListName().toLowerCase());
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + AlreadyReferred.replace("{target}", args[0].toLowerCase()));
					return false;
				}
			}
			return true;
		}else if (cmd.getName().equalsIgnoreCase("referreload")){
			if (args.length == 0){
				this.reloadConfig();
				String reload = ReferredByMe.this.getConfig().getString("reload");
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + reload);
			}else {
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ReferredByMe.this.getConfig().getString("arguments"));
				return false;
			}
			return true;
		}else if (cmd.getName().equalsIgnoreCase("referinfo")){
			if (args.length == 1){
				if (ReferredByMe.this.getConfig().getBoolean("Players." + args[0].toLowerCase() + ".exists")){
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Name: " + ChatColor.GRAY + args[0].toLowerCase());
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Referred By: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Players." + args[0].toLowerCase() + ".ReferredBy"));
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Referrals: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Players." + args[0].toLowerCase() + ".Referrals"));
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Rank: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getInt("Players." + args[0].toLowerCase() + ".Rank"));
				} else {
					Player player = (Player) sender;
					String NotFound = ReferredByMe.this.getConfig().getString("NotFound").replace("{player}", player.getPlayerListName().toLowerCase());
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + NotFound.replace("{target}", args[0].toLowerCase()));
				}
			} else if (args.length == 0){
				if (!(sender instanceof Player)) {
					sender.sendMessage("Cannot be executed from the console.");
					return false;
				} else {
					Player player = (Player) sender;
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Name: " + ChatColor.GRAY + player.getPlayerListName().toLowerCase());
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Referred By: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Players." + player.getPlayerListName().toLowerCase() + ".ReferredBy"));
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Referrals: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Players." + player.getPlayerListName().toLowerCase() + ".Referrals"));
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Rank: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getInt("Players." + player.getPlayerListName().toLowerCase() + ".Rank"));
				}
			} else if (args.length > 1){
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ReferredByMe.this.getConfig().getString("arguments"));
				return false;
			}
			return true;
		}else if (cmd.getName().equalsIgnoreCase("referversion")){
			if (args.length == 0){
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Author: " + ChatColor.GRAY + "LaXynd");
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Version: " + ChatColor.GRAY + "V0.5");
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Build Type: " + ChatColor.YELLOW + "Development");
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "For Minecraft Version: " + ChatColor.GRAY + "1.5.2");
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "Released: " + ChatColor.GRAY + "06/05/2013");
			} else {
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ReferredByMe.this.getConfig().getString("arguments"));
			}
			return true;
		}else if (cmd.getName().equalsIgnoreCase("referrank") || cmd.getName().equalsIgnoreCase("referleader")){
			if (args.length == 0){
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GOLD + "Rank: 1");
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GOLD + "Name: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Rank.1.Name"));
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GOLD + "Referrals: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Rank.1.Referrals"));
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GRAY + "Rank: 2");
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GRAY + "Name: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Rank.2.Name"));
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GRAY + "Referrals: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Rank.2.Referrals"));
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.DARK_RED + "Rank: 3");
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.DARK_RED + "Name: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Rank.3.Name"));
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.DARK_RED + "Referrals: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Rank.3.Referrals"));
			} else if (args.length == 1){
				ChatColor rank = ChatColor.GREEN;
				if (args[0].equals(1)){
					rank = ChatColor.GOLD;
				}else if (args[0].equals(2)){
					rank = ChatColor.GRAY;
				}else if (args[0].equals(3)){
					rank = ChatColor.DARK_RED;
				}else {
					rank = ChatColor.GREEN;
				}
				sender.sendMessage(ChatColor.RED + "[RederredByMe] " + rank + "Rank: " + args[0]);
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + rank + "Name: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Rank." + args[0] + ".Name"));
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + rank + "Referrals: " + ChatColor.GRAY + ReferredByMe.this.getConfig().getString("Rank." + args[0] + ".Referrals"));
			} else {
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ReferredByMe.this.getConfig().getString("arguments"));
				return false;
			}
			return true;
		}else if (cmd.getName().equalsIgnoreCase("referconfig")){
			if (args.length == 0){
				sender.sendMessage(ChatColor.RED + "[ReferredByMe]" + ReferredByMe.this.getConfig().getString("arguments"));
				return false;
			}if (args.length == 1 && args[0].equalsIgnoreCase("help")){
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "/referconfig Help Menu");
				sender.sendMessage(ChatColor.GREEN + "This command allows you to edit the config in-game.");
				sender.sendMessage(ChatColor.GREEN + "Usage: /referconfig <path> [value] [type]");
				sender.sendMessage(ChatColor.GREEN + "The path is the variable you are getting/setting.");
				sender.sendMessage(ChatColor.GREEN + "Indentation in the config file, is replace with dots (.)");
				sender.sendMessage(ChatColor.GREEN + "For example, If you were checking the player, LaXynd's Referral Count, the path would be " + ChatColor.RED + "Players.laxynd.Referrals");
				sender.sendMessage(ChatColor.GREEN + "Path names are case-sensitive. Player names are always set in lower case.");
				sender.sendMessage(ChatColor.GREEN + "The Types are String, Int and Boolean");
				sender.sendMessage(ChatColor.GREEN + "Strings are text. When setting messages, you would use String");
				sender.sendMessage(ChatColor.GREEN + "Ints (Integers) are numbers (no decimals). When setting how many referrals you need to obtain a reeward, use Int");
				sender.sendMessage(ChatColor.GREEN + "Booleans are true or false values. When setting wether you want to check IPs, use Boolean"); 
				sender.sendMessage(ChatColor.GREEN + "If you have any questions, go to " + ChatColor.WHITE + "http://dev.bukkit.org/server-mods/referredbyme/");
			}if (args.length == 1 && !args[0].equalsIgnoreCase("help")){
				sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + args[0] + ": " + ReferredByMe.this.getConfig().getString(args[0]));
			}if (args.length == 3){
				if (args[2].equalsIgnoreCase("String")){
					ReferredByMe.this.getConfig().set(args[0],args[1]);
					sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "The variable " + args[0] + " has now been set with the value " + args[1]);
				} if (args[2].equalsIgnoreCase("Int")){
					try {
						int Setter = Integer.parseInt(args[1]);
						ReferredByMe.this.getConfig().set(args[0], Setter);
						sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "The variable " + args[0] + " has now been set with the value " + Setter);
					} catch (Exception e){
						sender.sendMessage(ChatColor.RED + "[ReferredByMe] Error: Expected Int");
					}
				} if (args[2].equalsIgnoreCase("Boolean")){
					boolean Setter;
					if (args[1].equalsIgnoreCase("true")){
						Setter = true;
						ReferredByMe.this.getConfig().set(args[0], Setter);
						sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "The variable " + args[0] + " has now been set with the value " + Setter);
					} else if (args[1].equalsIgnoreCase("false")){
						Setter = false;
						ReferredByMe.this.getConfig().set(args[0], Setter);
						sender.sendMessage(ChatColor.RED + "[ReferredByMe] " + ChatColor.GREEN + "The variable " + args[0] + " has now been set with the value " + Setter);
					} else {
						sender.sendMessage(ChatColor.RED + "[ReferredByMe] Error: Expected Boolean");
					}
				}
				this.saveConfig();
			}if (args.length > 3 || args.length == 2){
				sender.sendMessage(ChatColor.RED + "[ReferredByMe]" + ReferredByMe.this.getConfig().getString("arguments"));
				return false;
			} return true;
		}
		return false; 
	}
}