package com.mlntcandy.netask.ntservapi;

import com.mlntcandy.netask.ntservapiclient.APIClient;
import com.mlntcandy.netask.ntservapiclient.APIResponseExecutorRegister;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Ntservapi.MODID)
public class Ntservapi {

    public static final String MODID = "ntservapi";
    private static final Logger LOGGER = LogUtils.getLogger();
    public Ntservapi() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        Config.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("NeTask serverApi client initializing...");
        APIResponseExecutorRegister.register(new APIExecutor());
        APIResponseExecutorRegister.setServer(event.getServer());
        LOGGER.info("NeTask serverApi client initialized successfully!");
    }

    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event) {
        registerCommand(event, "pay");
        registerCommand(event, "balance");
        registerCommand(event, "bal");
        registerCommand(event, "money");
        registerCommand(event, "balancetop");
        registerCommand(event, "baltop");
        registerCommand(event, "bankhelp");
        registerCommand(event, "history");
    }

    private void registerCommand(RegisterCommandsEvent event, String commandName) {
        event.getDispatcher().register(Commands.literal(commandName).then(Commands.argument("args", StringArgumentType.greedyString())).executes(cmd -> {
            String args = StringArgumentType.getString(cmd, "args");
            ArrayList<String> arrArgs = new ArrayList<>();

            arrArgs.add(commandName);
            for (String a : args.split(" ")) {
                if (!Objects.equals(a, "")) arrArgs.add(a);
            }

            ServerPlayer player = cmd.getSource().getPlayer();
            if (player == null) return 0;

            APIClient.sendMessage(arrArgs.toArray(new String[0]), player.getUUID());
            return 1;
        }));
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            // LOGGER.info("HELLO FROM CLIENT SETUP");
            // LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
