package com.nao20010128nao.NoChatSpam;

import com.nao20010128nao.NoChatSpam.controllers.JavascriptController;
import com.nao20010128nao.NoChatSpam.controllers.ScriptController;
import com.nao20010128nao.NoChatSpam.localevents.CommandEvent;
import com.nao20010128nao.NoChatSpam.localevents.MeEvent;
import com.nao20010128nao.NoChatSpam.localevents.SayEvent;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
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
		getServer().getCommandMap().register("nocs", new CommandDispatcher("nocs"));
	}

	@Override
	public void onDisable() {
		controller.onFinish();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ("nocs".equalsIgnoreCase(label))
			return false;
		if (!sender.isOp()) {
			sender.sendMessage("You have no permission to use this command.");
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage("Usage: /nocs reload");
			return true;
		}
		if ("reload".equalsIgnoreCase(args[0])) {
			sender.sendMessage("Reloading script...");
			getLogger().info("Reloading script...");
			reloadConfig();
			Config config = getConfig();
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
			sender.sendMessage("Done.");
			getLogger().info("Done.");
		}
		return true;
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

	class CommandDispatcher extends Command {

		public CommandDispatcher(String name) {
			super(name);
		}

		@Override
		public boolean execute(CommandSender sender, String commandLabel, String[] args) {
			return onCommand(sender, this, commandLabel, args);
		}
	}
}
