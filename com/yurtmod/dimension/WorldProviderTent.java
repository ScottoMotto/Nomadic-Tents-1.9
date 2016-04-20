package com.yurtmod.dimension;

import com.yurtmod.main.Config;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenOcean;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderTent extends WorldProvider
{
	@Override
	public void registerWorldChunkManager()
	{
		this.worldChunkMgr = new WorldChunkManager(worldObj);
		this.setDimension(Config.DIMENSION_ID);
		this.setAllowedSpawnTypes(false, false);
		this.hasNoSky = false;
	}
	
	@Override
	public String getDimensionName() 
	{
		return TentDimension.DIM_NAME;
	}
	
	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new TentChunkProvider(this.worldObj, this.dimensionId, false);
	}
	
	@Override
	public BiomeGenBase getBiomeGenForCoords(BlockPos pos)
    {
        return BiomeGenBase.ocean;
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
	public String getInternalNameSuffix() 
	{
		return "dim" + Config.DIMENSION_ID;
	}
}
