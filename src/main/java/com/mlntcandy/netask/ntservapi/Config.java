package com.mlntcandy.netask.ntservapi;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {
    public static ForgeConfigSpec.ConfigValue<String> API_ENDPOINT;
    public static ForgeConfigSpec.ConfigValue<String> SERVER_TOKEN;

    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        API_ENDPOINT = builder.define("api_endpoint", "https://netask.mlntcandy.com/");
        SERVER_TOKEN = builder.define("server_token", "none");

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }
}
