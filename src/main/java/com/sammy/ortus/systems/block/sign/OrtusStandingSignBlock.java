package com.sammy.ortus.systems.block.sign;

import com.sammy.ortus.systems.blockentity.OrtusSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class OrtusStandingSignBlock extends StandingSignBlock implements EntityBlock
{
    public OrtusStandingSignBlock(Properties properties, WoodType type)
    {
        super(properties, type);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OrtusSignBlockEntity(pos,state);
    }
}
