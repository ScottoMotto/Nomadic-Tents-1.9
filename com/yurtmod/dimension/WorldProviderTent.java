package com.yurtmod.dimension;

import com.yurtmod.main.Config;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderTent extends WorldProvider
{
	@Override
	public void registerWorldChunkManager()
	{
		this.setDimension(Config.DIMENSION_ID);
		this.setAllowedSpawnTypes(false, false);
		this.hasNoSky = false;
	}

	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new TentChunkProvider(this.worldObj, this.getDimension(), false);
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(BlockPos pos)
	{
		return Biomes.ocean;
	}

	@Override
	public boolean canRespawnHere()
	{
		return true;
	}

	@Override
	public int getRespawnDimension(EntityPlayerMP player)
	{
		return Config.DIMENSION_ID;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return true;
	}

	@Override
	public String getWelcomeMessage()
	{
		return "Entering your Tent";
	}

	@Override
	public DimensionType getDimensionType() 
	{
		return DimensionType.OVERWORLD;
	}
}
