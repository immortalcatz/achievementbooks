package com.stateshifterlabs.achievementbooks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {

		final ModelResourceLocation resourceLocation =
				new ModelResourceLocation(String.format("%s:%s", MODID, id), "inventory");

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, resourceLocation);
		ModelLoader.setCustomModelResourceLocation(item, meta, resourceLocation);
	}

	@Override
	public String getDataDir() {
		return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
	}

	@Override
	public String getPlayerName() {
		return Minecraft.getMinecraft().thePlayer.getName();
	}

}
