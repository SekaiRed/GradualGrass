package com.sekai.gradualgrass.util;

import com.sekai.gradualgrass.GradualGrass;
import com.sekai.gradualgrass.blocks.GradualGrassBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GradualGrass.MODID);
    public static final DeferredRegister<Block> MOD_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GradualGrass.MODID);

    public static void init()
    {
        MOD_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MOD_BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> GRADUAL_GRASS_BLOCK = MOD_BLOCKS.register("gradual_grass_block", () -> new GradualGrassBlock(AbstractBlock.Properties.of(Material.GRASS).randomTicks().strength(0.55F).sound(SoundType.GRAVEL)));
}
