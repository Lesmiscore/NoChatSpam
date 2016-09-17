package com.nao20010128nao.NoChatSpam.controllers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.nao20010128nao.NoChatSpam.NoChatSpam;
import com.nao20010128nao.NoChatSpam.localevents.ChatEvent;
import com.nao20010128nao.NoChatSpam.localevents.CommandEvent;
import com.nao20010128nao.NoChatSpam.localevents.MeEvent;
import com.nao20010128nao.NoChatSpam.localevents.SayEvent;

import cn.nukkit.Server;
import cn.nukkit.utils.Config;

public class JavascriptController implements ScriptController {
	Server server;
	NoChatSpam plugin;

	Context context;
	Scriptable scope;
	/* Hooks */
	Function onStart, onChat, onMe, onSay, onCommand, onFinish;

	@Override
	public void load(Server server, NoChatSpam plugin) {
		this.server = server;
		this.plugin = plugin;

		Config cfg = plugin.getConfig();
		cfg.reload();
		File toLoad = new File(plugin.getDataFolder(), "contol.js");
		if (cfg.exists("file", true)) {
			File path = new File(cfg.getString("file"));
			if (path.isAbsolute())
				toLoad = path;
			else
				toLoad = new File(plugin.getDataFolder(), cfg.getString("file"));
		}
		if (!toLoad.exists()) {
			plugin.getLogger().info(toLoad + " doesn't exist!");
			plugin.setEnabled(false);
			return;
		}
		plugin.getLogger().info("Loading script from: " + toLoad);

		context.setOptimizationLevel(9);
		scope = context.initStandardObjects();
		Reader r = null;
		try {
			r = new FileReader(toLoad);
			context.evaluateReader(scope, r, toLoad.toString(), 0, null);
		} catch (Throwable e) {
			plugin.getLogger().alert("Unable to load the script", e);
			return;
		} finally {
			try {
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			onStart = (Function) scope.get("onStart", scope);
		} catch (Throwable e) {
			plugin.getLogger().alert("Error", e);
		}
		try {
			onChat = (Function) scope.get("onChat", scope);
		} catch (Throwable e) {
			plugin.getLogger().alert("Error", e);
		}
		try {
			onMe = (Function) scope.get("onMe", scope);
		} catch (Throwable e) {
			plugin.getLogger().alert("Error", e);
		}
		try {
			onSay = (Function) scope.get("onSay", scope);
		} catch (Throwable e) {
			plugin.getLogger().alert("Error", e);
		}
		try {
			onCommand = (Function) scope.get("onCommand", scope);
		} catch (Throwable e) {
			plugin.getLogger().alert("Error", e);
		}
		try {
			onFinish = (Function) scope.get("onFinish", scope);
		} catch (Throwable e) {
			plugin.getLogger().alert("Error", e);
		}
	}

	@Override
	public void onStart() {
		if (onStart != null)
			onStart.call(context, scope, scope, new Object[] { server, plugin });
	}

	@Override
	public void onChat(ChatEvent event) {
		if (onChat != null)
			onChat.call(context, scope, scope, new Object[] { event });
	}

	@Override
	public void onMe(MeEvent event) {
		if (onMe != null)
			onMe.call(context, scope, scope, new Object[] { event });
	}

	@Override
	public void onSay(SayEvent event) {
		if (onSay != null)
			onSay.call(context, scope, scope, new Object[] { event });
	}

	@Override
	public void onCommand(CommandEvent event) {
		if (onCommand != null)
			onCommand.call(context, scope, scope, new Object[] { event });
	}

	@Override
	public void onFinish() {
		if (onFinish != null)
			onFinish.call(context, scope, scope, new Object[] {});
	}
}
