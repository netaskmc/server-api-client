package com.mlntcandy.netask.ntservapi;

import com.mlntcandy.netask.ntservapiclient.APIResponseExecutor;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public class APIExecutor implements APIResponseExecutor {
    @Override
    public void tellraw(MinecraftServer server, String target, String payload) {
        if (server.getPlayerList().getPlayer(UUID.fromString(target)) == null) return;
        final String command = "tellraw " + target + " " + payload;
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }
}
