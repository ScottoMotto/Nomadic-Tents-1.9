package com.yurtmod.content;

import com.yurtmod.dimension.StructureHelper.ITepeeBlock;
import com.yurtmod.dimension.StructureHelper.IYurtBlock;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockBarrier extends BlockUnbreakable implements IYurtBlock, ITepeeBlock
{
	public BlockBarrier() 
	{
		super(Material.rock);
		this.setBlockBounds(0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
		return new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX() + 1, (double)pos.getY() + 1, (double)pos.getZ() + 1);
    }
	
	@Override
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public int getRenderType()
	{
		return -1;
	}
}
