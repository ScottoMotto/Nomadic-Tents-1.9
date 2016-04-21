package com.yurtmod.content;

import com.yurtmod.dimension.StructureHelper.IYurtBlock;

import net.minecraft.block.material.Material;

public class BlockYurtRoof extends BlockUnbreakable implements IYurtBlock
{
	public BlockYurtRoof() 
	{
		super(Material.cloth);	
	}
}
