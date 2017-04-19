package com.stateshifterlabs.achievementbooks.items;


import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.client.gui.GUI;
import com.stateshifterlabs.achievementbooks.common.NBTUtils;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.facade.MCPlayer;
import com.stateshifterlabs.achievementbooks.facade.Player;
import com.stateshifterlabs.achievementbooks.facade.Sound;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class AchievementBookItem extends Item {

	private Book book;
	private AchievementStorage achievementStorage;
	private NetworkAgent networkAgent;
	private Sound sound;
	private String playerName;

	public AchievementBookItem(Book book, AchievementStorage achievementStorage, NetworkAgent networkAgent, Sound sound) {
		super();
		this.book = book;
		this.achievementStorage = achievementStorage;
		this.networkAgent = networkAgent;
		this.sound = sound;
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName("achievementbooks.achievementBook." + book.itemName());
		setTextureName(MODID + ":book" + "-" +book.colour());
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player) {
		if (world.isRemote) {
			sound.openBook();
			Player thePlayer = new MCPlayer(player);
			String playerName = getPlayer(par1ItemStack, thePlayer.getDisplayName());
			final GUI screen = new GUI(player, book, achievementStorage.forPlayer(playerName), networkAgent,
									   sound);
			Minecraft.getMinecraft().displayGuiScreen(screen);
		}
		return par1ItemStack;
	}

	private String getPlayer(ItemStack par1ItemStack, String thePlayer) {
		NBTTagCompound tag = NBTUtils.getTag(par1ItemStack);
		if(tag.hasKey("player")) {
			return tag.getString("player");
		}

		tag.setString("player", thePlayer);
		par1ItemStack.setTagCompound(tag);
		return thePlayer;
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack)
	{
		if(playerName == null) {
			playerName = AchievementBooksMod.proxy.getPlayerName();
		}
		final String boundTo = getPlayer(par1ItemStack, playerName);
		if(this.playerName.equalsIgnoreCase(boundTo)) {
			return book.name();
		}

		return String.format("%s (%s)", book.name(), boundTo);
	}

	public void updateBook(Book book) {
		this.book = book;
	}

}
