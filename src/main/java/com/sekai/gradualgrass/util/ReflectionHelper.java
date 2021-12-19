package com.sekai.gradualgrass.util;

import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class ReflectionHelper {
    /*public static void fixGrass() {
        Field grassFieldFeature = ObfuscationReflectionHelper.findField(Features.States.class, "field_244081_t");//field_215411_h
        Field grassFieldSurface = ObfuscationReflectionHelper.findField(SurfaceBuilder.class, "field_215411_h");
        grassFieldFeature.setAccessible(true);
        grassFieldSurface.setAccessible(true);
        try {
            grassFieldFeature.set(Features.States.class, RegistryHandler.REP_GRASS_BLOCK.get());
            grassFieldSurface.set(SurfaceBuilder.class, RegistryHandler.REP_GRASS_BLOCK.get());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }*/
}
