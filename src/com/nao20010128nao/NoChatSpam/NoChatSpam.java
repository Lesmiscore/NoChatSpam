package com.nao20010128nao.NoChatSpam;

import com.nao20010128nao.NoChatSpam.controllers.JavascriptController;
import com.nao20010128nao.NoChatSpam.controllers.ScriptController;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class NoChatSpam extends PluginBase implements Listener {
	ScriptController controller;

	@Override
	public void onEnable() {
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

	}
}
