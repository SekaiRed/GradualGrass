package com.sekai.gradualgrass.util;

import com.sekai.gradualgrass.GradualGrass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class EventHandler {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(RegistryHandler.GRADUAL_GRASS_BLOCK.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void handleColorRegistration(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((p_228064_0_, p_228064_1_, p_228064_2_, p_228064_3_) -> {
            return p_228064_1_ != null && p_228064_2_ != null ? BiomeColors.getAverageGrassColor(p_228064_1_, p_228064_2_) : GrassColors.get(0.5D, 1.0D);
        }, RegistryHandler.GRADUAL_GRASS_BLOCK.get());
    }

    @SubscribeEvent
    public static void missingRegistryEvent(RegistryEvent.MissingMappings<Block> event) {
        for(RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
            if (mapping.key.equals(new ResourceLocation(GradualGrass.MODID, "gradual_grass_block"))) {
                mapping.remap(Blocks.GRASS_BLOCK);
            }
        }
    }

    /*public static Block fromBlock = Blocks.GRASS_BLOCK; // change this to suit your need
    public static Block toBlock = RegistryHandler.REP_GRASS_BLOCK.get(); // change this to suit your need

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public static void onEvent(ChunkEvent.Load event)
    {
        if(!(event.getChunk() instanceof Chunk))
            return;

        Chunk theChunk = (Chunk) event.getChunk();

        // replace all blocks of a type with another block type

        BlockPos.Mutable pos = new BlockPos.Mutable(0, 0, 0);
        for (int x = 0; x < 16; ++x)
        {
            for (int z = 0; z < 16; ++z)
            {
                for (int y = 0; y < 256; ++y)
                {
                    pos.set(x, y, z);
                    if (theChunk.getBlockState(pos).getBlock() == fromBlock) {
                        theChunk.setBlockState(pos, toBlock.defaultBlockState(), false);
                    }
                }
            }
        }
        theChunk.markUnsaved();
    }*/
}
