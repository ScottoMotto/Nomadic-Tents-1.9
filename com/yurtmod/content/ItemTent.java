package com.yurtmod.content;

import java.util.List;

import com.yurtmod.dimension.StructureHelper;
import com.yurtmod.dimension.StructureType;
import com.yurtmod.dimension.TentDimension;
import com.yurtmod.main.TentSaveData;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTent extends Item 
{
	public static final String OFFSET_X = "TentOffsetX";
	public static final String OFFSET_Z = "TentOffsetZ";

	public ItemTent()
	{
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) 
	{
		if(!world.isRemote)
		{
			if(itemStack.getTagCompound() == null) itemStack.setTagCompound(new NBTTagCompound());
			// determine new offset data
			adjustSaveData(itemStack, world, player);
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!TentDimension.isTentDimension(worldIn.provider.getDimension()) && !worldIn.isRemote)
		{
			EnumFacing d = player.getHorizontalFacing();
			boolean hitTop = facing == EnumFacing.UP;
			IBlockState clicked = worldIn.getBlockState(pos);
			int meta = stack.getItemDamage();
			StructureType type = StructureType.byMetadata(meta);
			if(clicked.getBlock() == Blocks.snow_layer || clicked.getMaterial() == Material.plants)
			{
				hitTop = true;
				//pos = pos.down(1);
				// debug:
				//System.out.println("You clicked on a replaceable material, the tent will replace it.");
			}
			else pos = pos.up(1);

			if(!player.canPlayerEdit(pos, hitTop ? EnumFacing.UP : facing, stack))
			{
				// debug:
				//System.out.println("Player cannot edit");
				return EnumActionResult.FAIL;
			}
			else if(hitTop)
			{
				// debug:
				System.out.println("Trying to generate a Tent...");
				if(StructureHelper.canSpawnStructureHere(worldIn, type, pos, d))
				{
					Block door = StructureType.byMetadata(meta).getDoorBlock();
					if(StructureHelper.generateSmallStructureOverworld(worldIn, type, pos, d))
					{
						// lower door:
						TileEntity te = worldIn.getTileEntity(pos);
						if(te != null && te instanceof TileEntityTentDoor)
						{
							StructureType.applyToTileEntity(player, stack, (TileEntityTentDoor)te);
						}
						else System.out.println("Error! Failed to retrieve TileEntityTentDoor at " + pos);
						// remove tent from inventory
						stack.stackSize--;
						return EnumActionResult.SUCCESS;
					}
				}
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	public boolean canItemEditBlocks()
	{
		return true;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		return "item." + StructureType.getName(stack);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) 
	{
		for(StructureType type : StructureType.values())
		{
			ItemStack tent = type.getDropStack(-1, -1 - type.ordinal());
			subItems.add(tent);
		}
	}
	
	/**
     * Retrieves the normal 'lifespan' of this item when it is dropped on the ground as a EntityItem.
     * This is in ticks, standard result is 6000, or 5 mins.
     *
     * @param itemStack The current ItemStack
     * @param world The world the entity is in
     * @return The normal lifespan in ticks.
     */
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return Integer.MAX_VALUE;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List par3List, boolean par4)
	{
		par3List.add(this.getTooltipColor(stack.getItemDamage()) + I18n.translateToLocal("tooltip.extra_dimensional_space"));
	}

	public void adjustSaveData(ItemStack stack, World world, EntityPlayer player)
	{
		TentSaveData data = TentSaveData.forWorld(world);
		StructureType struct = StructureType.byMetadata(stack.getItemDamage());
		stack.getTagCompound().setInteger(OFFSET_Z, struct.getTagOffsetZ());
		switch(struct)
		{
		case TEPEE_LARGE:	
			data.addCountTepeeLarge(1);
			data.addCountTepeeMed(-1);
			stack.getTagCompound().setInteger(OFFSET_X, data.getCountTepeeLarge());
			break;
		case TEPEE_MEDIUM:
			data.addCountTepeeMed(1);
			data.addCountTepeeSmall(-1);
			stack.getTagCompound().setInteger(OFFSET_X, data.getCountTepeeMed());
			break;
		case TEPEE_SMALL:
			data.addCountTepeeSmall(1);
			stack.getTagCompound().setInteger(OFFSET_X, data.getCountTepeeSmall());
			break;
		case YURT_LARGE:
			data.addCountYurtLarge(1);
			data.addCountYurtMed(-1);
			stack.getTagCompound().setInteger(OFFSET_X, data.getCountYurtLarge());
			break;
		case YURT_MEDIUM:
			data.addCountYurtMed(1);
			data.addCountYurtSmall(-1);
			stack.getTagCompound().setInteger(OFFSET_X, data.getCountYurtMed());
			break;
		case YURT_SMALL:
			data.addCountYurtSmall(1);
			stack.getTagCompound().setInteger(OFFSET_X, data.getCountYurtSmall());
			break;
		default:
			break;
		}
	}

	public TextFormatting getTooltipColor(int meta)
	{
		switch(meta)
		{
		case 0: case 3: return TextFormatting.RED;
		case 1: case 4: return TextFormatting.BLUE;
		case 2: case 5: return TextFormatting.GREEN;
		default: 		return TextFormatting.GRAY;
		}
	}
}