package com.sammy.ortus.systems.block;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

/**
 * Various throwaway data stored in {@link #DATA_CACHE}, which is cleared after the game is finished loading.
 * All needsX parameters are intended to be used for datagen only.
 */
public class OrtusThrowawayBlockData {

    public static HashMap<OrtusBlockProperties, OrtusThrowawayBlockData> DATA_CACHE = new HashMap<>();

    public boolean needsPickaxe;
    public boolean needsAxe;
    public boolean needsShovel;
    public boolean needsHoe;

    public boolean needsStone;
    public boolean needsIron;
    public boolean needsDiamond;

    public boolean isCutoutLayer;
    public boolean hasCustomLoot;

    public OrtusThrowawayBlockData() {

    }

    public OrtusThrowawayBlockData needsPickaxe() {
        needsPickaxe = true;
        return this;
    }

    public OrtusThrowawayBlockData needsAxe() {
        needsAxe = true;
        return this;
    }

    public OrtusThrowawayBlockData needsShovel() {
        needsShovel = true;
        return this;
    }

    public OrtusThrowawayBlockData needsHoe() {
        needsHoe = true;
        return this;
    }

    public OrtusThrowawayBlockData needsStone() {
        needsStone = true;
        return this;
    }

    public OrtusThrowawayBlockData needsIron() {
        needsIron = true;
        return this;
    }

    public OrtusThrowawayBlockData needsDiamond() {
        needsDiamond = true;
        return this;
    }

    public OrtusThrowawayBlockData isCutoutLayer() {
        isCutoutLayer = true;
        return this;
    }

    public OrtusThrowawayBlockData hasCustomLoot() {
        hasCustomLoot = true;
        return this;
    }
}