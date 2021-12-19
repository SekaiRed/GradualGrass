package com.sekai.gradualgrass.mixin;

import com.sekai.gradualgrass.util.GradualGrowthHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpreadableSnowyDirtBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SpreadableSnowyDirtBlock.class)
public abstract class MixinSpreadable {
    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    private void randomTick(BlockState blockState, ServerWorld serverWorld, BlockPos pos, Random random, CallbackInfo info) {
        info.cancel();
        GradualGrowthHelper.randomTick(blockState, serverWorld, pos, random);
    }
}
