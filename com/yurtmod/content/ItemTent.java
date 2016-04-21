package com.yurtmod.content;

import java.util.List;

import com.yurtmod.dimension.StructureHelper;
import com.yurtmod.dimension.StructureHelper.StructureType;
import com.yurtmod.main.Config;
import com.yurtmod.main.TentSaveData;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
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
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return EnumActionResult.SUCCESS;
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
		for(int i = 0, len = StructureType.values().length; i < len; i++)
		{
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player, EnumHand hand)
	{
		if(worldIn.provider.getDimension() != Config.DIMENSION_ID && !worldIn.isRemote)
		{
			RayTraceResult rtr = this.getMovingObjectPositionFromPlayer(worldIn, player, true);

			if (rtr == null)
			{
				return new ActionResult(EnumActionResult.FAIL, stack);
			}
			else if(rtr.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				int d = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360) + 0.50) & 3;
				BlockPos hitPos = rtr.getBlockPos().up(1);
				boolean hitTop = rtr.sideHit == EnumFacing.UP;
				Block clicked = worldIn.getBlockState(hitPos).getBlock();
				int meta = stack.getItemDamage();
				if(clicked == Blocks.snow_layer || worldIn.getBlockState(hitPos).getMaterial() == Material.plants)
				{
					hitTop = true;
					hitPos.down(1);
					// debug:
					//System.out.println("You clicked on a replaceable material, the yurt will replace it.");
				}

				if(!player.canPlayerEdit(hitPos, hitTop ? EnumFacing.UP : rtr.sideHit, stack))
				{
					return new ActionResult(EnumActionResult.FAIL, stack);
				}
				else if(hitTop)
				{
					// debug:
					//System.out.println("Trying to generate a Yurt...");
					if(StructureHelper.canSpawnStructureHere(worldIn, this.getStructureType(meta), hitPos, d))
					{
						Block door = this.getStructureType(meta).getDoorBlock();
						if(StructureHelper.generateSmallStructureOverworld(worldIn, this.getStructureType(meta), hitPos, d))
						{
							// lower door:
							TileEntity te = worldIn.getTileEntity(hitPos);
							if(te != null && te instanceof TileEntityTentDoor)
							{
								this.getStructureType(meta).applyToTileEntity(player, stack, (TileEntityTentDoor)te);
							}
							else System.out.println("Error! Failed to retrieve TileEntityTentDoor at " + hitPos);
							// remove tent from inventory
							stack.stackSize--;
							return new ActionResult(EnumActionResult.SUCCESS, stack);
						}
					}
				}
			}
		}
		return new ActionResult(EnumActionResult.PASS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List par3List, boolean par4)
	{
		par3List.add(this.getTooltipColor(stack.getItemDamage()) + I18n.translateToLocal("tooltip.extra_dimensional_space"));
	}

	public StructureType getStructureType(int meta)
	{
		return StructureHelper.StructureType.values()[meta % StructureType.values().length];
	}

	public void adjustSaveData(ItemStack stack, World world, EntityPlayer player)
	{
		TentSaveData data = TentSaveData.forWorld(world);
		StructureType struct = getStructureType(stack.getItemDamage());
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