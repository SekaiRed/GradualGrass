package com.sekai.gradualgrass.blocks;

import com.sekai.gradualgrass.config.GradualGrassConfig;
import com.sekai.gradualgrass.util.GradualGrowthHelper;
import com.sekai.gradualgrass.util.RegistryHandler;
import net.minecraft.block.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

import static com.sekai.gradualgrass.blocks.GradualGrassBlock.FINAL_STAGE;
import static com.sekai.gradualgrass.blocks.GradualGrassBlock.STAGE;

public class ReplacedGrassBlock extends GrassBlock {
    public ReplacedGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld serverWorld, BlockPos pos, Random random) {
        if (!canBeGrass(state, serverWorld, pos)) {
            if (!serverWorld.isAreaLoaded(pos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            serverWorld.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
        } else {


            if (serverWorld.getMaxLocalRawBrightness(pos.above()) >= 9) {
                for(int i = 0; i < GradualGrassConfig.growthRoll; ++i) {
                    //BlockPos targetPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    BlockPos targetPos = applyRandomOffset(pos, random);
                    BlockState blockstate = GradualGrowthHelper.getGrownState(state, serverWorld.getBlockState(targetPos));
                    if (blockstate != null && GradualGrassConfig.growthChance > random.nextDouble() && isValidGrowthTarget(serverWorld.getBlockState(targetPos)) && canPropagate(blockstate, serverWorld, targetPos)) {
                        serverWorld.setBlockAndUpdate(targetPos, blockstate);
                    }
                }
            }
        }
    }

    private BlockPos applyRandomOffset(BlockPos pos, Random random) {
        return pos.offset(
                GradualGrassConfig.growthRangeMinX + random.nextInt(GradualGrassConfig.growthRangeMaxX - GradualGrassConfig.growthRangeMinX + 1),
                GradualGrassConfig.growthRangeMinY + random.nextInt(GradualGrassConfig.growthRangeMaxY - GradualGrassConfig.growthRangeMinY + 1),
                GradualGrassConfig.growthRangeMinZ + random.nextInt(GradualGrassConfig.growthRangeMaxZ - GradualGrassConfig.growthRangeMinZ + 1));
    }

    /*private static BlockState getGrownState(BlockState state, ServerWorld serverWorld, BlockPos pos) {
        BlockState newGrassState = RegistryHandler.GRADUAL_GRASS_BLOCK.get().defaultBlockState();
        BlockState targetState = serverWorld.getBlockState(pos);
        if(targetState.is(RegistryHandler.GRADUAL_GRASS_BLOCK.get())) {
            if (targetState.getValue(STAGE) < FINAL_STAGE) {
                newGrassState = newGrassState.setValue(STAGE, targetState.getValue(STAGE) + 1);
            } else {
                newGrassState = RegistryHandler.REP_GRASS_BLOCK.get().defaultBlockState();
            }
        }
        return newGrassState;
    }*/

    private static boolean canBeGrass(BlockState p_220257_0_, IWorldReader p_220257_1_, BlockPos p_220257_2_) {
        /*return p_220257_1_.getBlockState(p_220257_2_).is(Blocks.DIRT) ||
                p_220257_1_.getBlockState(p_220257_2_).is(RegistryHandler.REP_GRASS_BLOCK.get()) ||
                p_220257_1_.getBlockState(p_220257_2_).is(RegistryHandler.GRADUAL_GRASS_BLOCK.get());*/
        BlockPos blockpos = p_220257_2_.above();
        BlockState blockstate = p_220257_1_.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(p_220257_1_, p_220257_0_, p_220257_2_, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(p_220257_1_, blockpos));
            return i < p_220257_1_.getMaxLightLevel();
        }
    }

    private static boolean canPropagate(BlockState blockState, IWorldReader reader, BlockPos pos) {
        //return true;
        BlockPos blockpos = pos.above();
        return canBeGrass(blockState, reader, pos) && !reader.getFluidState(blockpos).is(FluidTags.WATER);
    }

    private static boolean isValidGrowthTarget(BlockState state) {
        return state.is(Blocks.DIRT) || state.is(RegistryHandler.GRADUAL_GRASS_BLOCK.get());
    }
}
