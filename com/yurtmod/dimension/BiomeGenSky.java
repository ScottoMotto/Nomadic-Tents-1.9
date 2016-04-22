package com.yurtmod.dimension;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenSky extends BiomeGenBase
{
	public BiomeGenSky(BiomeGenBase.BiomeProperties properties)
    {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.topBlock = Blocks.air.getDefaultState();
        this.fillerBlock = Blocks.air.getDefaultState();
        this.theBiomeDecorator = new BiomeEndDecorator();
    }
}
