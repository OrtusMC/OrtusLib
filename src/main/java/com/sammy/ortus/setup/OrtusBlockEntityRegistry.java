package com.sammy.ortus.setup;

import com.sammy.ortus.systems.block.sign.OrtusStandingSignBlock;
import com.sammy.ortus.systems.block.sign.OrtusWallSignBlock;
import com.sammy.ortus.systems.blockentity.OrtusSignBlockEntity;
import com.sammy.ortus.systems.multiblock.IOrtusMultiblockComponent;
import com.sammy.ortus.systems.multiblock.MultiBlockComponentEntity;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sammy.ortus.OrtusLib.ORTUS;


public class OrtusBlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ORTUS);

    public static final RegistryObject<BlockEntityType<MultiBlockComponentEntity>> MULTIBLOCK_COMPONENT = BLOCK_ENTITY_TYPES.register("multiblock_component", () -> BlockEntityType.Builder.of(MultiBlockComponentEntity::new, getBlocks(IOrtusMultiblockComponent.class)).build(null));
    public static final RegistryObject<BlockEntityType<OrtusSignBlockEntity>> SIGN = BLOCK_ENTITY_TYPES.register("sign", () -> BlockEntityType.Builder.of(OrtusSignBlockEntity::new, getBlocks(OrtusStandingSignBlock.class, OrtusWallSignBlock.class)).build(null));

    public static Block[] getBlocks(Class<?>... blockClasses) {
        IForgeRegistry<Block> blocks = ForgeRegistries.BLOCKS;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (Arrays.stream(blockClasses).anyMatch(b -> b.isInstance(block))) {
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }

    public static Block[] getBlocksExact(Class<?> clazz) {
        IForgeRegistry<Block> blocks = ForgeRegistries.BLOCKS;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (clazz.equals(block.getClass())) {
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }

    @Mod.EventBusSubscriber(modid = ORTUS, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientOnly {
        @SubscribeEvent
        public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(SIGN.get(), SignRenderer::new);
        }
    }
}