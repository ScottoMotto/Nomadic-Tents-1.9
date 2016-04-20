package com.yurtmod.content;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTentFrame extends BlockUnbreakable
{
	public static final int NUM_TEXTURES = 4;
	private final BlockToBecome TO_BECOME;
	public static final PropertyInteger PROGRESS = PropertyInteger.create("progress", 0, NUM_TEXTURES - 1);

	protected BlockTentFrame(BlockToBecome type)
	{
		super(Material.wood);
		this.TO_BECOME = type;
		this.setDefaultState(this.blockState.getBaseState().withProperty(PROGRESS, 0));
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			int meta = this.getMetaFromState(state);

			if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemMallet)
			{
				if(player.getHeldItem().getItem() == Content.itemSuperMallet)
				{
					return onSuperMalletUsed(worldIn, pos);
				}
				// debug:
				//System.out.print("Activated by Tent Mallet\n");
				if(meta < NUM_TEXTURES - 1)
				{
					worldIn.setBlockState(pos, this.getStateFromMeta(meta + 1), 3);
					// debug:
					//System.out.print("Built up a frame block by 1 unit. My metadata is now " + this.getMetaFromState(worldIn.getBlockState(pos)) + "\n");
				}
				else
				{
					this.becomeReal(worldIn, pos);
					player.getHeldItem().damageItem(1, player);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		int meta = this.getMetaFromState(worldIn.getBlockState(pos));
		if(meta == 0)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
		}
		else if(meta <= NUM_TEXTURES / 2)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}
		else
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}
	
	@Override
    public void setBlockBoundsForItemRender() 
    {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
    }
	
	@SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }
	
	@Override
	public boolean isFullCube()
    {
        return false;
    }
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

	@Override
	protected BlockState createBlockState() 
	{
		return new BlockState(this, new IProperty[] {PROGRESS});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return getDefaultState().withProperty(this.PROGRESS, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return ((Integer)state.getValue(this.PROGRESS)).intValue();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean becomeReal(World worldIn, BlockPos pos)
	{
		return !worldIn.isRemote && worldIn.setBlockState(pos, this.TO_BECOME.getBlock());
	}

	public boolean onSuperMalletUsed(World worldIn, BlockPos pos)
	{
		this.becomeReal(worldIn, pos);
		for(int i = -1; i < 2; i++)
		{
			for(int j = -1; j < 2; j++)
			{
				for(int k = -1; k < 2; k++)
				{
					BlockPos curPos = new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k);
					Block current = worldIn.getBlockState(curPos).getBlock();
					if(current instanceof BlockTentFrame)
					{
						((BlockTentFrame) current).onSuperMalletUsed(worldIn,curPos);
					}
				}
			}
		}
		return true;
	}
	
	public static enum BlockToBecome
	{
		YURT_WALL_INNER,
		YURT_WALL_OUTER,
		YURT_ROOF,
		TEPEE_WALL;
		
		public IBlockState getBlock()
		{
			switch(this)
			{
			case YURT_WALL_INNER: 	return Content.yurtInnerWall.getDefaultState();
			case YURT_WALL_OUTER: 	return Content.yurtOuterWall.getDefaultState();
			case YURT_ROOF: 		return Content.yurtRoof.getDefaultState();
			case TEPEE_WALL: 		return Content.tepeeWall.getDefaultState();
			default: return null;
			}
		}
	}
}
