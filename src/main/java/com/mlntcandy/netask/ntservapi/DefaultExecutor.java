package com.mlntcandy.netask.ntservapi;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class DefaultExecutor {
    public static void tellraw(MinecraftServer server, String target, String payload) {
        Player player = server.getPlayerList().getPlayer(UUID.fromString(target));
        if (player == null) return;
        final String command = "tellraw " + player.getName().getString() + " " + payload;
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }
}
