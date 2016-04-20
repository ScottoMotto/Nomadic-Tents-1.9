package com.yurtmod.content;

import java.util.List;

import com.yurtmod.main.YurtMain;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMallet extends Item
{	
	public ItemMallet(ToolMaterial material)
	{
		this.setMaxDamage(material.getMaxUses());
		// TODO this.setTextureName(YurtMain.MODID + ":mallet");
		this.setCreativeTab(YurtMain.tab);
		this.setFull3D();
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World worldIn,  BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return true;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack)
	{
		return false;
	}

	@Override
	public boolean canItemEditBlocks()
    {
        return true;
    }
}
