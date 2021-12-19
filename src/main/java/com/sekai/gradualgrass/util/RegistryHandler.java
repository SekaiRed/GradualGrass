package com.sekai.gradualgrass.util;

import com.sekai.gradualgrass.GradualGrass;
import com.sekai.gradualgrass.blocks.GradualGrassBlock;
import com.sekai.gradualgrass.blocks.ReplacedGrassBlock;
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
    public static final String MINECRAFT = "minecraft";

    //Replacements
    public static final DeferredRegister<Item> REP_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MINECRAFT);
    public static final DeferredRegister<Block> REP_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MINECRAFT);

    //Mod
    public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GradualGrass.MODID);
    public static final DeferredRegister<Block> MOD_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GradualGrass.MODID);

    public static void init()
    {
        REP_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        REP_BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MOD_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MOD_BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    /*public static final RegistryObject<Block> REP_GRASS_BLOCK = REP_BLOCKS.register("grass_block", () -> new ReplacedGrassBlock(AbstractBlock.Properties.of(Material.GRASS).randomTicks().strength(0.6F).sound(SoundType.GRASS)));
    public static final RegistryObject<Item> REP_GRASS_BLOCK_ITEM = REP_ITEMS.register("grass_block", () -> new BlockItem(REP_GRASS_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));*/

    public static final RegistryObject<Block> GRADUAL_GRASS_BLOCK = MOD_BLOCKS.register("gradual_grass_block", () -> new GradualGrassBlock(AbstractBlock.Properties.of(Material.GRASS).randomTicks().strength(0.55F).sound(SoundType.GRAVEL)));
    //public static final RegistryObject<Item> MOD_GRASS_BLOCK_ITEM = REP_ITEMS.register("grass_block", () -> new BlockItem(GRADUAL_GRASS_BLOCK.get(), new Item.Properties()));
}
