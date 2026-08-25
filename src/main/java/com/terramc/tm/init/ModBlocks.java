package com.terramc.tm.init;

import com.terramc.tm.TerraMC;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

// 方块注册中心。
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TerraMC.MODID);

    // 示例：注册一个方块
    // public static final DeferredBlock<Block> EXAMPLE = BLOCKS.registerSimpleBlock("example", BlockBehaviour.Properties.of());

    private ModBlocks() {
    }
}
