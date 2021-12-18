package com.sekai.gradualgrass.config;

import com.sekai.gradualgrass.GradualGrass;
import com.sekai.gradualgrass.util.GradualGrowthHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GradualGrass.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GradualGrassConfig {
    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    //Common
    public static int amountOfStages;
    public static double growthChance;
    public static int growthRoll;
    public static int growthRangeMinX;
    public static int growthRangeMinY;
    public static int growthRangeMinZ;
    public static int growthRangeMaxX;
    public static int growthRangeMaxY;
    public static int growthRangeMaxZ;



    public static void bakeConfig() {
        //Common
        amountOfStages = COMMON.amountOfStages.get();
        GradualGrowthHelper.stageList = fetchStageList(amountOfStages);

        growthChance = COMMON.growthChance.get();
        growthRoll = COMMON.growthRoll.get();
        growthRangeMaxX = Math.max(COMMON.growthRangeMaxX.get(), COMMON.growthRangeMinX.get());
        growthRangeMinX = Math.min(COMMON.growthRangeMaxX.get(), COMMON.growthRangeMinX.get());
        growthRangeMaxY = Math.max(COMMON.growthRangeMaxY.get(), COMMON.growthRangeMinY.get());
        growthRangeMinY = Math.min(COMMON.growthRangeMaxY.get(), COMMON.growthRangeMinY.get());
        growthRangeMaxZ = Math.max(COMMON.growthRangeMaxZ.get(), COMMON.growthRangeMinZ.get());
        growthRangeMinZ = Math.min(COMMON.growthRangeMaxZ.get(), COMMON.growthRangeMinZ.get());
    }

    private static List<Integer> fetchStageList(int amountOfStages) {
        ArrayList<Integer> list = new ArrayList<>();
        switch (amountOfStages) {
            case 1:
                list.add(2);
                break;
            case 2:
                list.add(1);
                list.add(3);
                break;
            case 3:
                list.add(0);
                list.add(2);
                list.add(4);
                break;
            case 4:
                list.add(0);
                list.add(1);
                list.add(3);
                list.add(4);
                break;
            case 5:
                list.add(0);
                list.add(1);
                list.add(2);
                list.add(3);
                list.add(4);
                break;
        }
        return list;
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.IntValue amountOfStages;
        public final ForgeConfigSpec.DoubleValue growthChance;
        public final ForgeConfigSpec.IntValue growthRoll;
        public final ForgeConfigSpec.IntValue growthRangeMinX;
        public final ForgeConfigSpec.IntValue growthRangeMinY;
        public final ForgeConfigSpec.IntValue growthRangeMinZ;
        public final ForgeConfigSpec.IntValue growthRangeMaxX;
        public final ForgeConfigSpec.IntValue growthRangeMaxY;
        public final ForgeConfigSpec.IntValue growthRangeMaxZ;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            amountOfStages = builder
                    .comment("Amount of stages.\nThe smoothest results would be with 5, 2 and 1 even if others will work.")
                    .translation(GradualGrass.MODID + ".config." + "amountOfStages")
                    .defineInRange("amountOfStages", 5, 0, 5);
            growthChance = builder
                    .comment("Chance for a nearby dirt or gradual grass block to grow by a stage.\nVanilla grass is 1.")
                    .translation(GradualGrass.MODID + ".config." + "growthChance")
                    .defineInRange("growthChance", 1D, 0D, 1D);
            growthRoll = builder
                    .comment("How many rolls should grass source do per random tick?\nVanilla grass is 4.")
                    .translation(GradualGrass.MODID + ".config." + "growthRoll")
                    .defineInRange("growthRoll", 4, 0, Integer.MAX_VALUE);

            builder.push("Growth Range");
            builder.comment("Range for grass growth where range is how many blocks away can it grow.\nEx: 1 will only target blocks next to it.");
            growthRangeMinX = builder
                    .comment("Vanilla grass is -1.")
                    .translation(GradualGrass.MODID + ".config." + "growthRangeMinX")
                    .defineInRange("growthRangeMinX", -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            growthRangeMaxX = builder
                    .comment("Vanilla grass is 1.")
                    .translation(GradualGrass.MODID + ".config." + "growthRangeMaxX")
                    .defineInRange("growthRangeMaxX", 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            growthRangeMinY = builder
                    .comment("Vanilla grass is -3.")
                    .translation(GradualGrass.MODID + ".config." + "growthRangeMinY")
                    .defineInRange("growthRangeMinY", -3, Integer.MIN_VALUE, Integer.MAX_VALUE);
            growthRangeMaxY = builder
                    .comment("Vanilla grass is 1.")
                    .translation(GradualGrass.MODID + ".config." + "growthRangeMaxY")
                    .defineInRange("growthRangeMaxY", 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            growthRangeMinZ = builder
                    .comment("Vanilla grass is -1.")
                    .translation(GradualGrass.MODID + ".config." + "growthRangeMinZ")
                    .defineInRange("growthRangeMinZ", -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            growthRangeMaxZ = builder
                    .comment("Vanilla grass is 1.")
                    .translation(GradualGrass.MODID + ".config." + "growthRangeMaxZ")
                    .defineInRange("growthRangeMaxZ", 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == GradualGrassConfig.COMMON_SPEC) {
            bakeConfig();
        }
    }
}
