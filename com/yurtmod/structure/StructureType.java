package com.yurtmod.structure;

import com.yurtmod.block.TileEntityTentDoor;
import com.yurtmod.init.Content;
import com.yurtmod.item.ItemTent;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public enum StructureType
{
	YURT_SMALL(5, 2),
	YURT_MEDIUM(7, 3),
	YURT_LARGE(9, 4),
	TEPEE_SMALL(5, 2),
	TEPEE_MEDIUM(7, 3),
	TEPEE_LARGE(9, 4),
	BEDOUIN_SMALL(5, 2),
	BEDOUIN_MEDIUM(7, 3),
	BEDOUIN_LARGE(9, 4);
	
	private int squareWidth;
	private int doorOffsetZ;
	
	private StructureType(int sqWidth, int doorZ)
	{
		this.squareWidth = sqWidth;
		this.doorOffsetZ = doorZ;
	}
	
	/** @return square width of the structure **/
	public int getSqWidth()
	{
		return this.squareWidth;
	}
	
	/** @return The door is this number of blocks right from the front-left corner block **/
	public int getDoorPosition()
	{
		// on z-axis in Tent Dimension
		return doorOffsetZ;
	}
	
	public ItemStack getDropStack()
	{
		return new ItemStack(Content.itemTent, 1, this.ordinal());
	}
	
	public ItemStack getDropStack(int tagChunkX, int tagChunkZ)
	{
		ItemStack stack = this.getDropStack();
		if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger(ItemTent.OFFSET_X, tagChunkX);
		stack.getTagCompound().setInteger(ItemTent.OFFSET_Z, tagChunkZ);
		return stack;
	}
	
	public static void applyToTileEntity(EntityPlayer player, ItemStack stack, TileEntityTentDoor te)
	{
		if(stack.getTagCompound() == null || !stack.getTagCompound().hasKey(ItemTent.OFFSET_X)) 
		{
			System.out.println("ItemStack did not have any NBT information to pass to the TileEntity!");
			te.getWorld().removeTileEntity(te.getPos());
			return;
		}
		
		int offsetx = stack.getTagCompound().getInteger(ItemTent.OFFSET_X);
		int offsetz = stack.getTagCompound().getInteger(ItemTent.OFFSET_Z);
		te.setStructureType(StructureType.values()[stack.getItemDamage() % StructureType.values().length]);
		te.setOffsetX(offsetx);
		te.setOffsetZ(offsetz);
		te.setOverworldXYZ(player.posX, player.posY, player.posZ);
	}
	
	public Block getDoorBlock()
	{
		switch(this)
		{
		case YURT_SMALL: 	return Content.yurtDoorSmall;
		case YURT_MEDIUM: 	return Content.yurtDoorMed;
		case YURT_LARGE: 	return Content.yurtDoorLarge;
		case TEPEE_SMALL: 	return Content.tepeeDoorSmall;
		case TEPEE_MEDIUM: 	return Content.tepeeDoorMed;
		case TEPEE_LARGE: 	return Content.tepeeDoorLarge;
		case BEDOUIN_SMALL:	return Content.bedDoorSmall;
		case BEDOUIN_MEDIUM:return Content.bedDoorMed;
		case BEDOUIN_LARGE:	return Content.bedDoorLarge;
		}
		return null;
	}	
	
	/** @return the Z-offset of this structure type in the Tent Dimension **/
	public int getTagOffsetZ()
	{
		return this.ordinal();
	}
	
	public TextFormatting getTooltipColor()
	{
		switch(this)
		{
		case YURT_SMALL: case TEPEE_SMALL: case BEDOUIN_SMALL:		
			return TextFormatting.RED;
		case YURT_MEDIUM: case TEPEE_MEDIUM: case BEDOUIN_MEDIUM:	
			return TextFormatting.BLUE;
		case YURT_LARGE: case TEPEE_LARGE: case BEDOUIN_LARGE:
			return TextFormatting.GREEN;
		}
		return TextFormatting.GRAY;
	}
	
	public static String getName(ItemStack stack)
	{
		return getName(stack.getItemDamage());
	}
	
	public static String getName(int metadata)
	{
		return StructureType.values()[metadata].toString().toLowerCase();
	}
	
	public static StructureType get(int meta)
	{
		return StructureType.values()[meta % StructureType.values().length];
	}
}