package com.nao20010128nao.NoChatSpam.localevents;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;

public class CommandEvent extends BaseEvent implements Cancellable {
	boolean cancel = false;
	Player player;
	String text;

	public CommandEvent(Player p, String t) {
		// TODO 自動生成されたコンストラクター・スタブ
		player = p;
		text = t;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled() {
		cancel = true;
	}

	@Override
	public void setCancelled(boolean forceCancel) {
		cancel = forceCancel;
	}

	public Player getPlayer() {
		return player;
	}

	public String getCommand() {
		return text;
	}
}
