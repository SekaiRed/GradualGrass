package com.sekai.gradualgrass.util;

import com.sekai.gradualgrass.blocks.ReplacedGrassBlock;
import com.sekai.gradualgrass.config.GradualGrassConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.Random;

import static com.sekai.gradualgrass.blocks.GradualGrassBlock.FINAL_STAGE;
import static com.sekai.gradualgrass.blocks.GradualGrassBlock.STAGE;

public class GradualGrowthHelper {
    public static List<Integer> stageList;

    //TODO Check if gradual grass can appear, it won't if the block above can't survive

    /**
     * @param source The BlockState causing the growth right now.
     * @param target The BlockState being targeted to be changed into it's next stage/grass.
     * @return The resulting BlockState that should replace the target at the relevant pos.
     */
    public static BlockState getGrownState(BlockState source, BlockState target) {
        BlockState newGrassState = RegistryHandler.GRADUAL_GRASS_BLOCK.get().defaultBlockState();
        if(target.is(RegistryHandler.GRADUAL_GRASS_BLOCK.get())) {
            int stage = target.getValue(STAGE);
            if (isLastState(stage)) {
                //Only grow up to grass if the source is a grass block
                /*if(source.getBlock().is(RegistryHandler.REP_GRASS_BLOCK.get()))
                    newGrassState = RegistryHandler.REP_GRASS_BLOCK.get().defaultBlockState();*/
                if(source.is(Blocks.GRASS_BLOCK))
                    newGrassState = Blocks.GRASS_BLOCK.defaultBlockState();
                else
                    newGrassState = null;
            } else {
                int nextStage = getNextState(stage);
                if(source.is(RegistryHandler.GRADUAL_GRASS_BLOCK.get())) {
                    if(nextStage < source.getValue(STAGE)) {
                        newGrassState = newGrassState.setValue(STAGE, nextStage);
                    } else {
                        newGrassState = null;
                    }
                } else {
                    newGrassState = newGrassState.setValue(STAGE, nextStage);
                }
            }
        } else if(target.is(Blocks.DIRT)) {
            if(source.is(RegistryHandler.GRADUAL_GRASS_BLOCK.get())) {
                if(source.getValue(STAGE) < 1) {
                    newGrassState = null;
                }
            }
        }
        return newGrassState;
    }

    /*public static BlockState getGrownState(BlockState source, BlockState target) {
        BlockState newGrassState = RegistryHandler.GRADUAL_GRASS_BLOCK.get().defaultBlockState();
        if(target.is(RegistryHandler.GRADUAL_GRASS_BLOCK.get())) {
            if (target.getValue(STAGE) < FINAL_STAGE) {
                newGrassState = newGrassState.setValue(STAGE, target.getValue(STAGE) + 1);
            } else {
                newGrassState = RegistryHandler.REP_GRASS_BLOCK.get().defaultBlockState();
            }
        }
        return newGrassState;
    }*/

    public static boolean isLastState(int state) {
        if(stageList.contains(state)) {
            return stageList.indexOf(state) == (stageList.size() - 1);
        } else {
            return false;
        }
    }

    public static int getNextState(int state) {
        if(stageList.contains(state)) {
            int index = stageList.indexOf(state);
            if(index < stageList.size() - 1) {
                return stageList.get(index + 1);
            }
        } else {
            int i = 0;
            while (i < stageList.size()) {
                if(state < stageList.get(i)) {
                    return stageList.get(i);
                }
                i++;
            }
        }

        return state;
    }

    public static void randomTick(BlockState state, ServerWorld serverWorld, BlockPos pos, Random random) {
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

    private static BlockPos applyRandomOffset(BlockPos pos, Random random) {
        return pos.offset(
                GradualGrassConfig.growthRangeMinX + random.nextInt(GradualGrassConfig.growthRangeMaxX - GradualGrassConfig.growthRangeMinX + 1),
                GradualGrassConfig.growthRangeMinY + random.nextInt(GradualGrassConfig.growthRangeMaxY - GradualGrassConfig.growthRangeMinY + 1),
                GradualGrassConfig.growthRangeMinZ + random.nextInt(GradualGrassConfig.growthRangeMaxZ - GradualGrassConfig.growthRangeMinZ + 1));
    }

    private static boolean canBeGrass(BlockState p_220257_0_, IWorldReader p_220257_1_, BlockPos p_220257_2_) {
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
