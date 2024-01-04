package com.mlntcandy.netask.ntservapiclient;

import com.mlntcandy.netask.ntservapi.DefaultExecutor;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class APIResponseExecutor {
    private static final Map<String, IResponseExecutor> executors = new HashMap<>();

    public static void attach(String action, IResponseExecutor executor) {
        executors.put(action, executor);
    }

    private boolean fallbackToExternal(@NotNull APIResponseSingular response, MinecraftServer server) {
        IResponseExecutor executor = executors.get(response.action);
        if (executor == null) return false;
        try {
            executor.execute(response, server);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public void execute(@NotNull APIResponseSingular response, MinecraftServer server) {
        switch (response.action) {
            case "tellraw" -> DefaultExecutor.tellraw(server, response.target, response.payload);
            default -> {
                boolean found = fallbackToExternal(response, server);
                if (!found) throw new RuntimeException("Unsupported action sent by NeTask Server");
            }
        }
    }
}
