package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Loader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class ReloadCommand extends CommandBase {


	private Loader loader;

	public ReloadCommand(Loader loader) {
		this.loader = loader;
	}

	@Override
	public String getCommandName() {
		return "reload";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "reload //Reloads all books in the conifg";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		loader.init();
	}

}
