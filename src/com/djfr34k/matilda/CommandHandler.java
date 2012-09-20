package com.djfr34k.matilda;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;


public class CommandHandler implements CommandExecutor {
	

	private Matilda plugin;
	
	public CommandHandler(Matilda plugin) {
		this.plugin = plugin;
	}
	
	/*Define slash commands */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}
		
		if(cmd.getName().equalsIgnoreCase("linksteam")){
			if (player == null) {
				sender.sendMessage("this command can only be run by a player (not really FR34K is lazy if you need it to run in console ask nice)");
			} else {
				if (args.length == 0){
					sender.sendMessage(ChatColor.RED + "Not enough Arguments");
					return false;
				}
				else if (args.length == 1){
					
					if (args[0].equals("help")) {
						sender.sendMessage(ChatColor.AQUA + "/linksteam [STEAM_0:0:Account] to set your own link"); 
						sender.sendMessage(ChatColor.AQUA + "/linksteam [minecraftuser] [STEAM_0:0:Account] to set someone elses link.");
						return true;
					}
					else {
						String result;
						result = SQLHandler.RegisterPlayer(sender.getName(), args[0]);
						sender.sendMessage(ChatColor.YELLOW + result);
						return true;
					}
				}
				else if (args.length == 2){
					String result;
					result = SQLHandler.RegisterPlayer(args[0], args[1]);
					sender.sendMessage(ChatColor.YELLOW + result);
					return true;
				}
				else{
					sender.sendMessage(ChatColor.RED + "Too many arguments");
					return false;
				}
			}
		}
		return false;
	}
}
