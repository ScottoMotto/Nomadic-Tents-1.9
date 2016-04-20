package com.yurtmod.content;

import com.yurtmod.dimension.StructureHelper.IYurtBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockYurtRoof extends BlockUnbreakable implements IYurtBlock
{
	public BlockYurtRoof() 
	{
		super(Material.cloth);	
	}
}
