package com.sekai.gradualgrass.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

public class EventHandler {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(RegistryHandler.GRADUAL_GRASS_BLOCK.get(), RenderType.cutout());

        ReflectionHelper.fixGrass();
    }

    @SubscribeEvent
    public static void handleColorRegistration(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((p_228064_0_, p_228064_1_, p_228064_2_, p_228064_3_) -> {
            return p_228064_1_ != null && p_228064_2_ != null ? BiomeColors.getAverageGrassColor(p_228064_1_, p_228064_2_) : GrassColors.get(0.5D, 1.0D);
        }, RegistryHandler.GRADUAL_GRASS_BLOCK.get());
    }
}
