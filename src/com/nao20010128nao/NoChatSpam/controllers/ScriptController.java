package com.nao20010128nao.NoChatSpam.controllers;

import com.nao20010128nao.NoChatSpam.NoChatSpam;
import com.nao20010128nao.NoChatSpam.localevents.ChatEvent;
import com.nao20010128nao.NoChatSpam.localevents.CommandEvent;
import com.nao20010128nao.NoChatSpam.localevents.MeEvent;
import com.nao20010128nao.NoChatSpam.localevents.SayEvent;

import cn.nukkit.Server;

public interface ScriptController {
	public void load(Server server, NoChatSpam plugin);

	public void onStart();

	public void onChat(ChatEvent event);

	public void onMe(MeEvent event);

	public void onSay(SayEvent event);

	public void onCommand(CommandEvent event);

	public void onFinish();
}
