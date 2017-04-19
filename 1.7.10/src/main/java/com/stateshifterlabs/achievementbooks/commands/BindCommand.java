package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.UTF8Utils;
import com.stateshifterlabs.achievementbooks.common.NBTUtils;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class BindCommand extends CommandBase {


	private final LanguageRegistry t;


	public BindCommand() {
		t = LanguageRegistry.instance();
	}

	@Override
	public int getRequiredPermissionLevel() {
		return Bind.PERMISSION;
	}

	@Override
	public String getCommandName() {
		return Bind.NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return Bind.USAGE;
	}

	@Override
	public java.util.List addTabCompletionOptions(ICommandSender sender, String[] p_71516_2_) {
		return p_71516_2_.length >= 1 ? getListOfStringsMatchingLastWord(p_71516_2_,
																		 MinecraftServer.getServer().getAllUsernames())
									  : null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender.getCommandSenderName().equalsIgnoreCase("server")) {
			sender.addChatMessage(new ChatComponentText(t.getStringLocalization("ab.command.bind.error.console")));
			return;
		}

		ItemStack book = sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName()).getHeldItem();

		if (!book.getUnlocalizedName().substring(5, 21).equalsIgnoreCase("achievementbooks")) {
			sender.addChatMessage(new ChatComponentText(t.getStringLocalization("ab.command.bind.error.nonbook")));
			return;
		}

		if (args.length < 2) {
			sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
			return;
		}

		NBTTagCompound tag = NBTUtils.getTag(book);
		tag.setString("player", args[1]);

		book.setTagCompound(tag);

		sender.addChatMessage(new ChatComponentText(UTF8Utils.utf8String(t.getStringLocalization("ab.command.bind.success"), " ", args[1])));

	}
}
