package com.yurtmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLayered extends BlockUnbreakable
{
	public static final PropertyBool ABOVE_SIMILAR = PropertyBool.create("above_similar");
	
	public BlockLayered(Material m)
	{
		super(m);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ABOVE_SIMILAR, false));
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
	{
		updateMetadata(worldIn, pos);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos myPos, IBlockState state, Block neighbor) 
	{
		updateMetadata(worldIn, myPos);
	}
	
	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, new IProperty[] {ABOVE_SIMILAR});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return getDefaultState().withProperty(ABOVE_SIMILAR, meta > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return state.getValue(ABOVE_SIMILAR).booleanValue() ? 1 : 0;
	}
	
	private void updateMetadata(World worldIn, BlockPos myPos)
	{
		int metaToSet;
		if(worldIn.getBlockState(myPos.down(1)).getBlock() == this && worldIn.getBlockState(myPos.down(2)).getBlock() != this)
		{
			metaToSet = 1;
		}
		else
		{
			metaToSet = 0;
		}
		worldIn.setBlockState(myPos, this.getStateFromMeta(metaToSet), 3);
	}
}
