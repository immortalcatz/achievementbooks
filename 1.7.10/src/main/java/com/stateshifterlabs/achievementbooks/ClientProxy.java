package com.stateshifterlabs.achievementbooks;

import net.minecraft.client.Minecraft;

public class ClientProxy extends CommonProxy {

	@Override
	public String getDataDir() {
		return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
	}

	@Override
	public String getPlayerName() {
		return Minecraft.getMinecraft().thePlayer.getDisplayName();
	}

}
