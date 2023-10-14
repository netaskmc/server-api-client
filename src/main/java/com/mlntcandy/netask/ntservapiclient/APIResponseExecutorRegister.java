package com.mlntcandy.netask.ntservapiclient;

import net.minecraft.server.MinecraftServer;

public class APIResponseExecutorRegister {
    static APIResponseExecutor executor = null;
    public static MinecraftServer server = null;

    public static void register(APIResponseExecutor executor) {
        APIResponseExecutorRegister.executor = executor;
    }

    public static void execute(APIResponseSingular response) {
        if (executor == null) throw new RuntimeException("Tried to execute NeTask response, no executor is attached");
        if (server == null) throw new RuntimeException("Tried to execute NeTask response, no server instance is attached");
        executor.execute(response, server);
    }

    public static void setServer(MinecraftServer server) {
        APIResponseExecutorRegister.server = server;
    }
}
