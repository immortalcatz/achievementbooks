package com.stateshifterlabs.achievementbooks.commands;


import com.stateshifterlabs.achievementbooks.common.NBTUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

public class BindCommand extends CommandBase {


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
	public java.util.List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		return args.length >= 1 ? getListOfStringsMatchingLastWord(args,server.getAllUsernames()): null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender.getName().equalsIgnoreCase("server")) {
			sender.addChatMessage(new TextComponentTranslation("ab.command.bind.error.console"));
			return;
		}

		ItemStack book = sender.getEntityWorld().getPlayerEntityByName(sender.getName()).getHeldItem(EnumHand.MAIN_HAND);

		if (!book.getUnlocalizedName().substring(5, 21).equalsIgnoreCase("achievementbooks")) {
			sender.addChatMessage(new TextComponentTranslation("ab.command.bind.error.nonbook"));
			return;
		}

		if (args.length < 2) {
			sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
			return;
		}

		NBTTagCompound tag = NBTUtils.getTag(book);
		tag.setString("player", args[1]);

		book.setTagCompound(tag);

		sender.addChatMessage(new TextComponentTranslation("ab.command.bind.success").appendText(" " + args[1]));

	}
}
