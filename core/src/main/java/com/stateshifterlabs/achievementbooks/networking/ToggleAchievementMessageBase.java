package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.facade.ByteBufferUtilities;
import com.stateshifterlabs.achievementbooks.facade.NetworkMessage;
import io.netty.buffer.ByteBuf;

public class ToggleAchievementMessageBase implements NetworkMessage {
	private String bookName;
	private int achievementId;
	private String username;
	private ByteBufferUtilities bufferUtilities;

	public ToggleAchievementMessageBase(ByteBufferUtilities bufferUtilities) {
		this.bufferUtilities = bufferUtilities;
	}

	public ToggleAchievementMessageBase withData(String username, String bookName, int achievmenetId) {
		this.username = username;
		this.bookName = bookName;
		this.achievementId = achievmenetId;

		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		achievementId = buf.readInt();
		String data = bufferUtilities.readUTF8String(buf);
		String[] info = data.split(":");
		username = info[0];
		bookName = info[1];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(achievementId);
		bufferUtilities.writeUTF8String(buf, String.format("%s:%s", username, bookName));
	}

	public int achievementId() {
		return achievementId;
	}

	public String bookName() {
		return bookName;
	}

	public String playerName() {
		return username;
	}
}
