package com.yurtmod.dimension;

import com.yurtmod.main.Config;
import com.yurtmod.main.YurtMain;

import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.DimensionManager;

public class TentDimension 
{
	public static final int DIMENSION_ID = DimensionManager.getNextFreeDimId();
	public static final String DIM_NAME = "Tent Dimension";
	public static final DimensionType TENT_DIMENSION = DimensionType.register("TENT", "_tent", DIMENSION_ID, WorldProviderTent.class, false);
	public static final BiomeGenBase BIOME_SKY = new BiomeGenSky(new BiomeGenBase.BiomeProperties("Tent").setTemperature(0.8F).setRainfall(0.4F)).setRegistryName(YurtMain.MODID, "Tent");
			
	
	public static void mainRegistry()
	{
		//DimensionManager.registerProviderType(Config.DIMENSION_ID, WorldProviderTent.class, false);
		BiomeManager.addBiome(BiomeType.WARM, new BiomeEntry(BIOME_SKY, Config.TENT_BIOME_ID));
		DimensionManager.registerDimension(DIMENSION_ID, TentDimension.TENT_DIMENSION);
	}
	
	public static boolean isTentDimension(int id)
	{
		return id == TentDimension.DIMENSION_ID;
	}
}
