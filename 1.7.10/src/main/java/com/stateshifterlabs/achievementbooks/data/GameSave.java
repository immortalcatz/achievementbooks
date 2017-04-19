package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import com.stateshifterlabs.achievementbooks.serializers.AchievementStorageSerializer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class GameSave {


	private final Gson gson;
	private File saveDir;
	private AchievementStorage storage;
	private Books books;
	private NetworkAgent networkAgent;

	public GameSave(AchievementStorage storage, Books books, NetworkAgent networkAgent) {
		this.storage = storage;
		this.books = books;
		this.books = books;
		this.networkAgent = networkAgent;
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(AchievementStorage.class, new AchievementStorageSerializer(storage));
		gson = builder.create();
	}

	public void load() {
		saveDir =
				new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "/" + MODID.toLowerCase());

		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}

		File saveFile = new File(saveDir.getAbsolutePath() + "/achievementbooks.save.json");

		if (!saveFile.exists()) {
			return;
		}

		try {
			storage.clear();
			storage = gson.fromJson(new FileReader(saveFile), AchievementStorage.class);
		} catch (FileNotFoundException e) {

		}
	}

	public void save() {
		saveDir =
				new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "/" + MODID.toLowerCase());
		saveDir.mkdirs();
		save(storage);

	}

	public void save(AchievementStorage storage) {

		File saveFile = new File(saveDir.getAbsolutePath() + "/achievementbooks.save.json");
		String saveData = gson.toJson(storage);

		try {
			FileWriter fw = new FileWriter(saveFile);

			fw.write(saveData);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load load) {
		if (!load.world.isRemote) {
			load();
		}

	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save save) {
		if (!save.world.isRemote) {
			save();
		}

	}

	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		save();
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (player != null && !player.worldObj.isRemote) {
			networkAgent.sendAchievementsTo((EntityPlayerMP) player);

			int size = player.inventory.getSizeInventory();
			for (int i = 0; i < size; i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if(stack == null) {
					continue;
				}
				if (stack.getUnlocalizedName().substring(5, 21).equalsIgnoreCase("achievementbooks")) {
					NBTTagCompound tag = stack.getTagCompound();
					if (tag == null) {
						continue;
					}
					if (tag.hasKey("player")) {
						networkAgent.sendAchievementsToFor((EntityPlayerMP) player, tag.getString("player"));
					}
				}
			}
		}
	}

}
