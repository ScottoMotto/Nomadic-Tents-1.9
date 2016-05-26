package com.yurtmod.block;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.yurtmod.block.Categories.IFrameBlock;
import com.yurtmod.block.Categories.ITepeeBlock;
import com.yurtmod.dimension.TentDimension;
import com.yurtmod.init.Config;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTepeeWall extends BlockUnbreakable implements ITepeeBlock
{
	public static final int NUM_TEXTURES = 15;		// how many textures can apply to this block
	private static final int NUM_PATTERNS = 5;		// the first X textures excluding texture 0 are patterns
	public static final PropertyInteger TEXTURE = PropertyInteger.create("texture", 0, NUM_TEXTURES - 1);

	public BlockTepeeWall() 
	{
		super(Material.cloth);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
	{
		super.onBlockAdded(worldIn, pos, state);
		if(this.getMetaFromState(state) == 0)
		{
			int metaToSet;
			BlockPos doorPos = this.findDoorNearby(worldIn, pos);
			// this determines what pattern overworld tepees should have for each layer
			if(!TentDimension.isTentDimension(worldIn) && doorPos != null && (Math.abs(pos.getY() - doorPos.getY()) % 2 == 0))
			{		
				TileEntityTentDoor te = (TileEntityTentDoor)worldIn.getTileEntity(doorPos);
				// psuedo-random seed guarantees all blocks that are same y-dis from door get the same seed
				int randSeed = pos.getY() + doorPos.getX() + doorPos.getZ() + te.getOffsetX() * 123 + te.getOffsetZ() * 321;
				metaToSet = getMetaForRandomPattern(new Random(randSeed));
				worldIn.setBlockState(pos, this.getStateFromMeta(metaToSet), 2);
			}
			else if(worldIn.rand.nextInt(100) < Config.TEPEE_DECORATED_CHANCE)
			{
				metaToSet = getMetaForRandomDesign(worldIn.rand);
				worldIn.setBlockState(pos, this.getStateFromMeta(metaToSet), 2);
			}			
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) 
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos, this.getStateFromMeta(stack.getMetadata()));
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 **/
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int i = 0; i < NUM_TEXTURES; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, new IProperty[] {TEXTURE});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return getDefaultState().withProperty(TEXTURE, meta % NUM_TEXTURES);
	}

	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return state.getValue(TEXTURE).intValue();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state);
	}

	public static int getMetaForBase()
	{
		return 0;
	}

	public static int getMetaForRandomPattern(Random rand)
	{
		return rand.nextInt(NUM_PATTERNS) + 1;
	}

	public static int getMetaForRandomDesign(Random rand)
	{
		return getMetaForRandomPattern(rand) + NUM_PATTERNS;
	}

	/**
	 * Traces all connected ITepeeBlock blocks (frames and tepee walls)
	 * until it finds the lower door of the tepee.
	 * @param world the world
	 * @param pos BlockPos to begin searching from
	 * @return BlockPos of lower tepee door if found, else null
	 **/
	private BlockPos findDoorNearby(World world, BlockPos pos)
	{
		List checked = new LinkedList();
		while(pos != null && !(world.getBlockState(pos).getBlock() instanceof BlockTentDoor))
		{
			pos = getNextTepeeBlock(world, checked, pos);
		}
		if(pos == null) return null;	
		boolean isLower = world.getBlockState(pos).getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER;
		// debug:
		//System.out.println("Found a door! " + pos);
		//System.out.println("isLower = " + isLower);
		return isLower ? pos : pos.down(1);
	}

	/**
	 * Searches a 3x3x3 box for an ITepeeBlock that
	 * has not been added to the list already.
	 * @param worldIn the world
	 * @param exclude list of BlockPos already checked
	 * @param pos center of the 3x3x3 box
	 **/
	private BlockPos getNextTepeeBlock(World worldIn, List exclude, BlockPos pos)
	{
		int radius = 1;
		// favor blocks below this one - useful because most tepee blocks will be above the door
		for(int y = -radius; y <= radius; y++)
		{
			for(int x = -radius; x <= radius; x++)
			{
				for(int z = -radius; z <= radius; z++)
				{
					BlockPos checkPos = pos.add(x, y, z);
					IBlockState stateAt = worldIn.getBlockState(checkPos);
					if(!exclude.contains(checkPos))
					{
						if(stateAt.getBlock() instanceof ITepeeBlock || stateAt.getBlock() instanceof IFrameBlock)
						{
							exclude.add(checkPos);
							return checkPos;
						}
					}
				}
			}
		}
		return null;
	}
}
