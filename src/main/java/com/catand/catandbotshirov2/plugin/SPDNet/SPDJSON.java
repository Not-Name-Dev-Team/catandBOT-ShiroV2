package com.catand.catandbotshirov2.plugin.SPDNet;

import lombok.Data;

import java.util.List;

@Data
public class SPDJSON {
	public SPDJSON() {
	}


	public SPDJSON(int port, String roomprefix, long seed, int SPDMinVersion, int NETMinVersion, int assetVersion, boolean itemSharing, String motd, List<Account> accounts) {
		this.port = port;
		this.roomprefix = roomprefix;
		this.seed = seed;
		this.SPDMinVersion = SPDMinVersion;
		this.NETMinVersion = NETMinVersion;
		this.assetVersion = assetVersion;
		this.itemSharing = itemSharing;
		this.motd = motd;
		this.accounts = accounts;
	}

	private int port;
	private String roomprefix;
	private long seed;
	private int SPDMinVersion;
	private int NETMinVersion;
	private int assetVersion;
	private boolean itemSharing;
	private String motd;
	private List<Account> accounts;
}
