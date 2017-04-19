package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.facade.BufferUtilities;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class ToggleAchievementMessage implements IMessage {
	private final ToggleAchievementMessageBase base = new ToggleAchievementMessageBase(new BufferUtilities());

	public ToggleAchievementMessage withData(String username, String bookName, int achievmenetId) {
		base.withData(username, bookName, achievmenetId);
		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		base.fromBytes(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		base.toBytes(buf);
	}

	public int achievementId() {
		return base.achievementId();
	}

	public String bookName() {
		return base.bookName();
	}

	public String playerName() {
		return base.playerName();
	}
}
