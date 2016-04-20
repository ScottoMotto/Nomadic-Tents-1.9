package com.yurtmod.content;

import com.yurtmod.main.YurtMain;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockUnbreakable extends Block
{
	public BlockUnbreakable(Material material)
	{
		super(material);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setCreativeTab(YurtMain.tab);
		this.setStepSound(soundTypeWood);
	}
}
