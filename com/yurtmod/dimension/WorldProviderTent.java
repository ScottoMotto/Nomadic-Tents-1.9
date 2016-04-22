package com.yurtmod.dimension;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;

public class WorldProviderTent extends WorldProvider
{	
	@Override
	public void registerWorldChunkManager()
	{
		this.worldChunkMgr = new BiomeProviderSingle(TentDimension.BIOME_SKY);
		this.setDimension(TentDimension.DIMENSION_ID);
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
		return TentDimension.BIOME_SKY;
	}

	@Override
	public boolean canRespawnHere()
	{
		return true;
	}

	@Override
	public int getRespawnDimension(EntityPlayerMP player)
	{
		return TentDimension.DIMENSION_ID;
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
		return TentDimension.TENT_DIMENSION;
	}
}
