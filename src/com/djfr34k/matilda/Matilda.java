package com.djfr34k.matilda;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager; //I am an Idiot who spent an hour realizing Plguin is not right

public class Matilda extends JavaPlugin {

	private static Logger log;
	
	/*Define Listener Classes */
	public final PlayerLoginListener PlayerListener = new PlayerLoginListener();
	
	private CommandHandler myExecutor;
	@Override
	public void onEnable(){
		log = this.getLogger();
		PluginManager pm = getServer().getPluginManager();
		
		/*Register Commands */
		myExecutor = new CommandHandler(this);
		getCommand("linksteam").setExecutor(myExecutor);
		
		/* Register Events */
		pm.registerEvents(this.PlayerListener, this);
		
		
		Matilda.conlog("Matilda has been loaded");
	}
	
	public void onDisable(){
		Matilda.conlog("Matilda has been unloaded");
	}
	
	
	public static void conlog(String message){
		if (message != null){
			log.info(message);
		}
	}
	
}
