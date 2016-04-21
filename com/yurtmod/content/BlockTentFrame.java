package com.yurtmod.content;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTentFrame extends BlockUnbreakable
{
	public static final int NUM_TEXTURES = 4;
	private final BlockToBecome TO_BECOME;
	public static final PropertyInteger PROGRESS = PropertyInteger.create("progress", 0, NUM_TEXTURES - 1);
	
	public static final AxisAlignedBB AABB_PROGRESS_0 = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
	public static final AxisAlignedBB AABB_PROGRESS_1 = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	public static final AxisAlignedBB AABB_PROGRESS_2 = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

	protected BlockTentFrame(BlockToBecome type)
	{
		super(Material.wood);
		this.TO_BECOME = type;
		this.setDefaultState(this.blockState.getBaseState().withProperty(PROGRESS, 0));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			int meta = this.getMetaFromState(state);

			if(player.getHeldItem(hand) != null && player.getHeldItem(hand).getItem() instanceof ItemMallet)
			{
				if(player.getHeldItem(hand).getItem() == Content.itemSuperMallet)
				{
					return onSuperMalletUsed(worldIn, pos, player, player.getHeldItem(hand));
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
					player.getHeldItem(hand).damageItem(1, player);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	 public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		int meta = this.getMetaFromState(state);
		if(meta == 0)
		{
			return AABB_PROGRESS_0;
		}
		else if(meta <= NUM_TEXTURES / 2)
		{
			return AABB_PROGRESS_1;
		}
		else return AABB_PROGRESS_2;
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
	
	@Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        return;
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn)
    {
        return;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, new IProperty[] {PROGRESS});
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
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean becomeReal(World worldIn, BlockPos pos)
	{
		return !worldIn.isRemote && worldIn.setBlockState(pos, this.TO_BECOME.getBlock());
	}

	public boolean onSuperMalletUsed(World worldIn, BlockPos pos, EntityPlayer player, ItemStack stack)
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
						((BlockTentFrame) current).onSuperMalletUsed(worldIn,curPos,player,stack);
						stack.damageItem(1, player);
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
