package com.yurtmod.dimension;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

public class TentChunkProvider implements IChunkProvider
{
	private World worldObj;

	public TentChunkProvider(World worldIn, long dimID, boolean mapFeaturesEnabled)
	{
		this.worldObj = worldIn;
	}

	@Override
	public boolean chunkExists(int x, int z) 
	{
		return true;
	}
	
	@Override
	public Chunk provideChunk(int x, int z) 
	{
		Chunk chunk = new Chunk(worldObj, new ChunkPrimer(), x, z);
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public Chunk provideChunk(BlockPos pos) 
	{
		return provideChunk(pos.getX(), pos.getZ());
	}

	@Override
	public void populate(IChunkProvider icp, int x, int z) {}
	@Override
	public void recreateStructures(Chunk ch, int x, int z) {}
	@Override
	public void saveExtraData() {}


	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) 
	{
		return true;
	}

	@Override
	public boolean unloadQueuedChunks() 
	{
		return false;
	}

	@Override
	public boolean canSave() 
	{
		return true;
	}

	@Override
	public String makeString() 
	{
		return "RandomLevelSource";
	}

	@Override
	public int getLoadedChunkCount() 
	{
		return 0;
	}

	@Override
	public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) 
	{
		return true;
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
}
