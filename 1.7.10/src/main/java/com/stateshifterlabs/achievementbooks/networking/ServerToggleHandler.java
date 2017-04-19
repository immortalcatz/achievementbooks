package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ServerToggleHandler implements IMessageHandler<ToggleAchievementMessage, IMessage> {

	private AchievementStorage storage;
	private NetworkAgent networkAgent;

	public ServerToggleHandler(AchievementStorage storage, NetworkAgent networkAgent) {
		this.storage = storage;
		this.networkAgent = networkAgent;
	}


	@Override
	public IMessage onMessage(final ToggleAchievementMessage message, final MessageContext ctx)
	{
		storage.forPlayer(message.playerName()).toggle(message.bookName(), message.achievementId());
		networkAgent.sendAchievementsToFor(ctx.getServerHandler().playerEntity, message.playerName());
		if(!ctx.getServerHandler().playerEntity.getDisplayName().equalsIgnoreCase(message.playerName())) {
			//TODO send to impersonated player if they're logged in
		}
		return null;
	}

}
