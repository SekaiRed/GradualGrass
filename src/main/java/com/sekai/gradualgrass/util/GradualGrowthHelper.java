package com.sekai.gradualgrass.util;

import com.sekai.gradualgrass.blocks.ReplacedGrassBlock;
import com.sekai.gradualgrass.config.GradualGrassConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import java.util.List;

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
                if(source.getBlock().is(RegistryHandler.REP_GRASS_BLOCK.get()))
                    newGrassState = RegistryHandler.REP_GRASS_BLOCK.get().defaultBlockState();
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
}
