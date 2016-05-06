package com.yurtmod.dimension;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;

public class TentChunkGenerator implements IChunkGenerator
{
	private World worldObj;

	public TentChunkGenerator(World worldIn, long dimID, boolean mapFeaturesEnabled)
	{
		worldIn.setSeaLevel(64);
		this.worldObj = worldIn;
	}

	@Override
	public Chunk provideChunk(int x, int z) 
	{
		ChunkPrimer chunkprimer = new ChunkPrimer();
		/*
		IBlockState airstate = Blocks.air.getDefaultState();
		IBlockState layer0state = Blocks.bedrock.getDefaultState();
		IBlockState layer1state = Blocks.water.getDefaultState();
		for (int i = 0; i < 256; ++i)
		{
			for (int j = 0; j < 16; ++j)
			{
				for (int k = 0; k < 16; ++k)
				{
					IBlockState toSet = i == 0 ? layer0state : (i == 1 ? layer1state : airstate);
					chunkprimer.setBlockState(j, i, k, toSet);
				}
			}
		}
		 */
		Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
		BiomeGenBase[] abiomegenbase = this.worldObj.getBiomeProvider().loadBlockGeneratorData((BiomeGenBase[])null, x * 16, z * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (int l = 0; l < abyte.length; ++l)
        {
            abyte[l] = (byte)BiomeGenBase.getIdForBiome(abiomegenbase[l]);
        }

        chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(int x, int z) {}
	@Override
	public void recreateStructures(Chunk ch, int x, int z) {}

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
