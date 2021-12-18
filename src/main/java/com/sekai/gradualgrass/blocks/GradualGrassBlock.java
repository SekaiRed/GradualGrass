package com.sekai.gradualgrass.blocks;

import com.sekai.gradualgrass.config.GradualGrassConfig;
import com.sekai.gradualgrass.util.GradualGrowthHelper;
import com.sekai.gradualgrass.util.RegistryHandler;
import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GradualGrassBlock extends GrassBlock {
    public static final int FINAL_STAGE = 4;
    //0 dirt / FINAL_STAGE grass
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, FINAL_STAGE);

    public GradualGrassBlock(Properties properties) {
        super(properties);
        //this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
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

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> blockBlockStateBuilder) {
        super.createBlockStateDefinition(blockBlockStateBuilder);
        blockBlockStateBuilder.add(STAGE);
    }
}
