package com.mlntcandy.netask.ntservapiclient;

import net.minecraft.server.MinecraftServer;

public interface IResponseExecutor {
    void execute(APIResponseSingular response, MinecraftServer server);
}
