package com.djfr34k.matilda;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent;


public class PlayerLoginListener implements Listener {
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogin(PlayerLoginEvent event) {
		final String pname = event.getPlayer().getName();
		Matilda.conlog("Matilda writing sql for " + pname + "'s connect");
		SQLHandler.CreateSession(pname);
	}
	
	@EventHandler
	public void onPlayerExit(PlayerQuitEvent event) {
		final String pname = event.getPlayer().getName();
		Matilda.conlog("Matilda writing sql for " + pname + "'s disconnect");
		SQLHandler.CloseSession(pname);
	}
	
	
}
