package com.catand.catandbotshirov2.plugin.SPDNet;

import lombok.Data;

@Data
public class Account {
	public Account() {
	}

	public Account(boolean bot, boolean admin, String key, String nick, boolean banned) {
		this.bot = bot;
		this.admin = admin;
		this.key = key;
		this.nick = nick;
		this.banned = banned;
	}

	private boolean bot;
	private boolean admin;
	private String key;
	private String nick;

	private boolean banned;
}
