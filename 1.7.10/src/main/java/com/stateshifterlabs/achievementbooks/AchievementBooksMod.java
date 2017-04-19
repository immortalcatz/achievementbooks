package com.stateshifterlabs.achievementbooks;

import com.stateshifterlabs.achievementbooks.commands.BindCommand;
import com.stateshifterlabs.achievementbooks.commands.CreateDemoCommand;
import com.stateshifterlabs.achievementbooks.commands.GiveCommand;
import com.stateshifterlabs.achievementbooks.commands.ImportCommand;
import com.stateshifterlabs.achievementbooks.commands.ListCommand;
import com.stateshifterlabs.achievementbooks.commands.MainCommand;
import com.stateshifterlabs.achievementbooks.commands.ReloadCommand;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import com.stateshifterlabs.achievementbooks.data.GameSave;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.facade.MCSound;
import com.stateshifterlabs.achievementbooks.networking.MigrationNetworkAgent;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;

import java.io.File;

@Mod(modid = AchievementBooksMod.MODID, version = AchievementBooksMod.VERSION, name = AchievementBooksMod.MODID)
public class AchievementBooksMod {
	public static final String MODID = "achievementbooks";
	public static final String VERSION = "@VERSION@";

	@SidedProxy(serverSide = "com.stateshifterlabs.achievementbooks.CommonProxy", clientSide = "com.stateshifterlabs.achievementbooks.ClientProxy")
	public static CommonProxy proxy;

	private final AchievementStorage storage = new AchievementStorage();
	private Books books = new Books();
	private NetworkAgent networkAgent;
	private Loader loader;
	private File configDir;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		configDir = new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID);
		networkAgent = new NetworkAgent(storage);
		loader = new Loader(configDir, books, storage, networkAgent, new MCSound());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		loader.init();
		new GameSave(storage, books, networkAgent);

		final Book migrationTargetBook = books.migration();
		if (migrationTargetBook != null) {
			MigrationNetworkAgent migrationNetworkAgent = new MigrationNetworkAgent(migrationTargetBook, networkAgent, configDir, storage);
			FMLCommonHandler.instance().bus().register(migrationNetworkAgent);
		}

	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {

		MainCommand mainCommand = new MainCommand();
		mainCommand.add(new ReloadCommand(loader));
		mainCommand.add(new BindCommand());
		mainCommand.add(new ImportCommand(loader, networkAgent, proxy.getDataDir()));
		mainCommand.add(new CreateDemoCommand(loader));
		mainCommand.add(new GiveCommand(books));
		mainCommand.add(new ListCommand(books));

		ICommandManager server = MinecraftServer.getServer().getCommandManager();
		((ServerCommandManager) server).registerCommand(mainCommand);
	}
}
