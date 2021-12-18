package com.sekai.gradualgrass;

import com.sekai.gradualgrass.config.GradualGrassConfig;
import com.sekai.gradualgrass.util.EventHandler;
import com.sekai.gradualgrass.util.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GradualGrass.MODID)
public class GradualGrass
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "gradualgrass";

    public GradualGrass() {
        RegistryHandler.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GradualGrassConfig.COMMON_SPEC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventHandler::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventHandler::handleColorRegistration);
    }
}
