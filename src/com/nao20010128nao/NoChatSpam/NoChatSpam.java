package com.nao20010128nao.NoChatSpam;

import com.nao20010128nao.NoChatSpam.controllers.JavascriptController;
import com.nao20010128nao.NoChatSpam.controllers.ScriptController;
import com.nao20010128nao.NoChatSpam.localevents.CommandEvent;
import com.nao20010128nao.NoChatSpam.localevents.MeEvent;
import com.nao20010128nao.NoChatSpam.localevents.SayEvent;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class NoChatSpam extends PluginBase implements Listener {
	ScriptController controller;

	@Override
	public void onEnable() {
		getDataFolder().mkdirs();
		getServer().getPluginManager().registerEvents(this, this);

		Config config = getConfig();

		if (!config.exists("type"))
			config.set("type", "javascript");
		config.save();

		String type = config.getString("type");
		switch (type) {
			case "javascript":
			case "js":
				controller = new JavascriptController();
				break;
			default:
				getLogger().error("No type found for " + type);
		}
		controller.load(getServer(), this);
		controller.onStart();
	}

	@Override
	public void onDisable() {
		controller.onFinish();
	}

	@EventHandler
	public void onCommandPreProp(PlayerCommandPreprocessEvent event) {
		final String message = event.getMessage();
		if (!message.startsWith("/"))
			return;
		String commandBody = message.substring(1).split(" ")[0];
		switch (commandBody) {
			case "me":
				MeEvent me = new MeEvent(event.getPlayer(), message.length() >= 4 ? message.substring(4) : "");
				controller.onMe(me);
				event.setCancelled(me.isCancelled());
				break;
			case "say":
				SayEvent se = new SayEvent(event.getPlayer(), message.length() >= 5 ? message.substring(5) : "");
				controller.onSay(se);
				event.setCancelled(se.isCancelled());
				break;
			default:
				CommandEvent ce = new CommandEvent(event.getPlayer(), message);
				controller.onCommand(ce);
				event.setCancelled(ce.isCancelled());
				break;
		}
	}
}
