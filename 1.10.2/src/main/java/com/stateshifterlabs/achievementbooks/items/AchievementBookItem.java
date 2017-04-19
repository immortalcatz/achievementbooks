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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class AchievementBookItem extends Item implements IForgeRegistryEntry<Item> {

	private Book book;
	private AchievementStorage achievementStorage;
	private NetworkAgent networkAgent;
	private Sound sound;
	private String playerName;

	public AchievementBookItem(
			Book book, AchievementStorage achievementStorage, NetworkAgent networkAgent, Sound sound
	) {
		super();
		this.book = book;
		this.achievementStorage = achievementStorage;
		this.networkAgent = networkAgent;
		this.sound = sound;
		setRegistryName(MODID + ":" + book.itemName());
		setCreativeTab(CreativeTabs.MISC);
		setUnlocalizedName("achievementbooks" + book.itemName());
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ActionResult<ItemStack> onItemRightClick(
			ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand
	) {

		if (worldIn.isRemote) {
			sound.openBook();
			Player thePlayer = new MCPlayer(playerIn);
			String playerName = getPlayer(itemStackIn, thePlayer.getDisplayName());
			final GUI screen = new GUI(playerIn, book, achievementStorage.forPlayer(playerName), networkAgent, sound);
			Minecraft.getMinecraft().displayGuiScreen(screen);
		}

		return new ActionResult(EnumActionResult.PASS, itemStackIn);
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
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
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

	public ModelResourceLocation getModelLocation() {
		return new ModelResourceLocation(MODID + ":book-" + book.colour(), "inventory");
	}
}
