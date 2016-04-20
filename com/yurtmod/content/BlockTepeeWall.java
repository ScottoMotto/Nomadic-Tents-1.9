package com.yurtmod.content;

import java.util.List;
import java.util.Random;

import com.yurtmod.dimension.StructureHelper.ITepeeBlock;
import com.yurtmod.main.Config;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
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
		if(this.getMetaFromState(state) == 0)
		{
			int metaToSet;
			BlockPos doorPos = this.findDoorNearby(worldIn, pos.getX(), pos.getY(), pos.getZ(), 4);
			// debug:
			//System.out.println("searched for door. y = " + pos.getY() + ", doorPos = " + (doorPos == null ? "null" : doorPos));
			//System.out.println("rand seed = " + (doorPos == null ? "null" : (pos.getY() + doorPos.getX() + doorPos.getZ())));
			if(worldIn.provider.getDimensionId() != Config.DIMENSION_ID && doorPos != null && (Math.abs(pos.getY() - doorPos.getY()) % 2 == 0))
			{
				// debug:
				//System.out.println("searched for door. y=" + pos.getY() + ", doorPos=" + doorPos);
				// psuedo-random: hopefully guarantees all blocks on same y are same meta
				int randSeed = pos.getY() + doorPos.getX() + doorPos.getZ();
				metaToSet = getMetaForRandomPattern(new Random(randSeed));
				worldIn.setBlockState(pos, this.getStateFromMeta(metaToSet), 2);
			}
			else if(worldIn.rand.nextInt(Config.TEPEE_DECORATED_RATIO) == 0)
			{
				metaToSet = getMetaForRandomDesign(worldIn.rand);
				worldIn.setBlockState(pos, this.getStateFromMeta(metaToSet), 2);
				// debug:
				//System.out.println("Placing as design: metaToSet = " + metaToSet);
			}			
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) 
	{
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
	protected BlockState createBlockState() 
	{
		return new BlockState(this, new IProperty[] {TEXTURE});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return getDefaultState().withProperty(TEXTURE, meta % NUM_TEXTURES);
	}

	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return ((Integer)state.getValue(TEXTURE)).intValue();
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
    
    /** @return BlockPos of lower door if found within {@link radius}, else null **/
    private BlockPos findDoorNearby(World world, int x, int y, int z, int radius)
    {
    	for(int i = -radius; i <= radius; i++)
    	{
    		for(int j = -radius; j <= radius; j++)
    		{
    			for(int k = -radius; k <= radius; k++)
        		{
    				BlockPos checkPos = new BlockPos(x + i, y + j, z + k);
    				IBlockState stateAt = world.getBlockState(checkPos);
        			if(stateAt.getBlock() instanceof BlockTentDoor && stateAt.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER 
        					&& (this.isTouchingBlock(world, checkPos, Content.tepeeWall) || this.isTouchingBlock(world, checkPos, Content.tepeeWallFrame)))
        			{
        				return checkPos;
        			}
        		}
    		}
    	}
    	return null;
    }
    
    /**
	 * Checks the six blocks the block at {@link pos} is touching, looking for Block keyBlock
	 * Args: this blocks world, position, and the block to check for.
	 * @return true if keyBlock is found, else false
	 */
	private boolean isTouchingBlock(World worldIn, BlockPos pos, Block keyBlock)
	{
		return 	(worldIn.getBlockState(pos.east(1)).getBlock() == keyBlock) || (worldIn.getBlockState(pos.west(1)).getBlock() == keyBlock) || 
				(worldIn.getBlockState(pos.north(1)).getBlock() == keyBlock) || (worldIn.getBlockState(pos.south(1)).getBlock() == keyBlock) || 
				(worldIn.getBlockState(pos.up(1)).getBlock() == keyBlock) || (worldIn.getBlockState(pos.down(1)).getBlock() == keyBlock);
	}
}
