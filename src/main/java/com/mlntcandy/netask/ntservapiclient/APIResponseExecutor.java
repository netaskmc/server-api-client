package com.mlntcandy.netask.ntservapiclient;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public interface APIResponseExecutor {
    void tellraw(MinecraftServer server, String target, String payload);

    default void execute(@NotNull APIResponseSingular response, MinecraftServer server) {
        switch (response.type) {
            case "tellraw" -> tellraw(server, response.target, response.payload);
            default -> throw new RuntimeException("Unsupported action sent by NeTask Server");
        }
    }
}
