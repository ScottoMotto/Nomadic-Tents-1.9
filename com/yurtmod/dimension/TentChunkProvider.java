package com.yurtmod.dimension;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;

public class TentChunkProvider implements IChunkProvider, IChunkGenerator
{
	private World worldObj;

	public TentChunkProvider(World worldIn, long dimID, boolean mapFeaturesEnabled)
	{
		this.worldObj = worldIn;
	}
	
	@Override
	public Chunk provideChunk(int x, int z) 
	{
		Chunk chunk = new Chunk(worldObj, new ChunkPrimer(), x, z);
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(int x, int z) {}
	@Override
	public void recreateStructures(Chunk ch, int x, int z) {}

	@Override
	public boolean unloadQueuedChunks() 
	{
		return false;
	}

	@Override
	public String makeString() 
	{
		return "RandomLevelSource";
	}

	@Override
	public Chunk getLoadedChunk(int x, int z) 
	{
		return this.provideChunk(x, z);
	}

	@Override
	public BlockPos getStrongholdGen(World worldIn, String p_180513_2_, BlockPos p_180513_3_) 
	{
		return null;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) 
	{
		return new ArrayList();
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) 
	{
		return false;
	}
}
