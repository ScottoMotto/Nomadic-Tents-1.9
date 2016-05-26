package com.yurtmod.init;

import com.yurtmod.block.BlockBarrier;
import com.yurtmod.block.BlockBedouinRoof;
import com.yurtmod.block.BlockBedouinWall;
import com.yurtmod.block.BlockTentDoor;
import com.yurtmod.block.BlockTentFrame;
import com.yurtmod.block.BlockTentFrame.BlockToBecome;
import com.yurtmod.block.BlockTepeeWall;
import com.yurtmod.block.BlockUnbreakable;
import com.yurtmod.block.BlockYurtRoof;
import com.yurtmod.block.BlockYurtWall;
import com.yurtmod.block.TileEntityTentDoor;
import com.yurtmod.item.ItemMallet;
import com.yurtmod.item.ItemSuperMallet;
import com.yurtmod.item.ItemTent;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Content 
{
	// begin blocks
	public static Block barrier;
	public static Block indestructibleDirt;

	public static Block yurtRoof;		
	public static Block yurtOuterWall;		
	public static Block yurtInnerWall;	
	public static Block tepeeWall;
	public static Block bedWall;
	public static Block bedRoof;

	public static Block yurtDoorSmall;	
	public static Block yurtDoorMed;		
	public static Block yurtDoorLarge;		
	public static Block tepeeDoorSmall; 	
	public static Block tepeeDoorMed;		
	public static Block tepeeDoorLarge;	
	public static Block bedDoorSmall;
	public static Block bedDoorMed;
	public static Block bedDoorLarge;

	public static Block yurtWallFrame;		
	public static Block yurtRoofFrame;		
	public static Block tepeeWallFrame;
	public static Block bedWallFrame;
	public static Block bedRoofFrame;
	
	public static ItemBlock ibBarrier;
	public static ItemBlock ibSuperDirt;
	public static ItemBlock ibYurtRoof;
	public static ItemBlock ibYurtOuterWall;
	public static ItemBlock ibYurtInnerWall;
	public static ItemBlock ibTepeeWall;
	public static ItemBlock ibBedWall;
	public static ItemBlock ibBedRoof;
	public static ItemBlock ibYurtDoorSmall;
	public static ItemBlock ibYurtDoorMed;
	public static ItemBlock ibYurtDoorLarge;
	public static ItemBlock ibTepeeDoorSmall;
	public static ItemBlock ibTepeeDoorMed;
	public static ItemBlock ibTepeeDoorLarge;
	public static ItemBlock ibBedDoorSmall;
	public static ItemBlock ibBedDoorMed;
	public static ItemBlock ibBedDoorLarge;
	public static ItemBlock ibYurtWallFrame;
	public static ItemBlock ibYurtRoofFrame;
	public static ItemBlock ibTepeeWallFrame;
	public static ItemBlock ibBedWallFrame;
	public static ItemBlock ibBedRoofFrame;

	public static Item itemTent;
	public static Item itemMallet;
	public static Item itemSuperMallet;	
	public static Item itemTentCanvas;
	public static Item itemYurtWall;
	public static Item itemTepeeWall;
	public static Item itemBedWall;

	public static void mainRegistry()
	{
		initBlocks();
		initItemBlocks();
		initItems();
		
		registerBlocks();
		registerItems();
		registerTileEntity(TileEntityTentDoor.class, "TileEntityTentDoor");
	}
	
	private static void initBlocks()
	{
		// blocks
		barrier = new BlockBarrier();
		indestructibleDirt = new BlockUnbreakable(Material.ground);
		yurtOuterWall = new BlockYurtWall();
		yurtInnerWall = new BlockYurtWall();
		yurtRoof = new BlockYurtRoof();
		tepeeWall = new BlockTepeeWall();
		bedWall = new BlockBedouinWall();
		bedRoof = new BlockBedouinRoof();
		// doors
		yurtDoorSmall = new BlockTentDoor();
		yurtDoorMed = new BlockTentDoor();
		yurtDoorLarge = new BlockTentDoor();
		tepeeDoorSmall = new BlockTentDoor();
		tepeeDoorMed = new BlockTentDoor();
		tepeeDoorLarge = new BlockTentDoor();
		bedDoorSmall = new BlockTentDoor();
		bedDoorMed = new BlockTentDoor();
		bedDoorLarge = new BlockTentDoor();
		// frame blocks
		yurtWallFrame = new BlockTentFrame(BlockToBecome.YURT_WALL_OUTER);
		yurtRoofFrame = new BlockTentFrame(BlockToBecome.YURT_ROOF);
		tepeeWallFrame = new BlockTentFrame(BlockToBecome.TEPEE_WALL);
		bedWallFrame = new BlockTentFrame(BlockToBecome.BEDOUIN_WALL);
		bedRoofFrame = new BlockTentFrame(BlockToBecome.BEDOUIN_ROOF);
	}
	
	private static void initItemBlocks()
	{
		ibBarrier = new ItemBlock(barrier);
		ibSuperDirt = new ItemBlock(indestructibleDirt);
		ibYurtOuterWall = new ItemBlock(yurtOuterWall);
		ibYurtInnerWall = new ItemBlock(yurtInnerWall);
		ibYurtRoof = new ItemBlock(yurtRoof);
		ibTepeeWall = new ItemBlock(tepeeWall);
		ibBedWall = new ItemBlock(bedWall);
		ibBedRoof = new ItemBlock(bedRoof);
		// doors
		ibYurtDoorSmall = new ItemBlock(yurtDoorSmall);
		ibYurtDoorMed = new ItemBlock(yurtDoorMed);
		ibYurtDoorLarge = new ItemBlock(yurtDoorLarge);
		ibTepeeDoorSmall = new ItemBlock(tepeeDoorSmall);
		ibTepeeDoorMed = new ItemBlock(tepeeDoorMed);
		ibTepeeDoorLarge = new ItemBlock(tepeeDoorLarge);
		ibBedDoorSmall = new ItemBlock(bedDoorSmall);
		ibBedDoorMed = new ItemBlock(bedDoorMed);
		ibBedDoorLarge = new ItemBlock(bedDoorLarge);
		// frame blocks
		ibYurtWallFrame = new ItemBlock(yurtWallFrame);
		ibYurtRoofFrame = new ItemBlock(yurtRoofFrame);
		ibTepeeWallFrame = new ItemBlock(tepeeWallFrame);
		ibBedWallFrame = new ItemBlock(bedWallFrame);
		ibBedRoofFrame = new ItemBlock(bedRoofFrame);
	}
	
	private static void initItems()
	{
		itemTent = new ItemTent();
		// tools
		itemMallet = new ItemMallet(ToolMaterial.IRON);
		itemSuperMallet = new ItemSuperMallet(ToolMaterial.DIAMOND);
		// crafting only
		itemTentCanvas = new Item().setCreativeTab(NomadicTents.tab);
		itemYurtWall = new Item().setCreativeTab(NomadicTents.tab);
		itemTepeeWall = new Item().setCreativeTab(NomadicTents.tab);
		itemBedWall = new Item().setCreativeTab(NomadicTents.tab);
	}

	private static void registerBlocks() 
	{
		register(barrier, ibBarrier, "tentmod_barrier");
		register(indestructibleDirt, ibSuperDirt, "indestructible_dirt");
		register(yurtOuterWall, ibYurtOuterWall, "yurt_wall_outer");
		register(yurtInnerWall, ibYurtInnerWall, "yurt_wall_inner");
		register(yurtRoof, ibYurtRoof, "yurt_roof");
		register(tepeeWall, ibTepeeWall, "tepee_wall");
		register(bedWall, ibBedWall, "bed_wall");
		register(bedRoof, ibBedRoof, "bed_roof");
		// doors
		register(yurtDoorSmall, ibYurtDoorSmall, "yurt_door_0");
		register(yurtDoorMed, ibYurtDoorMed, "yurt_door_1");
		register(yurtDoorLarge, ibYurtDoorLarge, "yurt_door_2");
		register(tepeeDoorSmall, ibTepeeDoorSmall, "tepee_door_0");
		register(tepeeDoorMed, ibTepeeDoorMed, "tepee_door_1");
		register(tepeeDoorLarge, ibTepeeDoorLarge, "tepee_door_2");
		register(bedDoorSmall, ibBedDoorSmall, "bed_door_0");
		register(bedDoorMed, ibBedDoorMed, "bed_door_1");
		register(bedDoorLarge, ibBedDoorLarge, "bed_door_2");
		// frame blocks
		register(yurtWallFrame, ibYurtWallFrame, "frame_yurt_wall");
		register(yurtRoofFrame, ibYurtRoofFrame, "frame_yurt_roof");
		register(tepeeWallFrame, ibTepeeWallFrame, "frame_tepee_wall");
		register(bedWallFrame, ibBedWallFrame, "frame_bed_wall");
		register(bedRoofFrame, ibBedRoofFrame, "frame_bed_roof");
	}

	private static void registerItems() 
	{
		register(itemTent, "tent");
		register(itemMallet, "mallet");
		register(itemSuperMallet, "super_mallet");
		register(itemTentCanvas, "tent_canvas");
		register(itemYurtWall, "yurt_wall_piece");
		register(itemTepeeWall, "tepee_wall_piece");
		register(itemBedWall, "bed_wall_piece");
	}

	private static void registerTileEntity(Class <? extends TileEntity> te, String name) 
	{
		GameRegistry.registerTileEntity(te, NomadicTents.MODID + "." + name);	
	}
	
	private static void register(Item item, String name)
	{
		register(item, name, name);
	}
	
	private static void register(Block block, ItemBlock ib, String name)
	{
		register(block, ib, name, name);
	}

	private static void register(Item item, String name, String unlocal)
	{
		item.setUnlocalizedName(unlocal).setRegistryName(NomadicTents.MODID, name);
		GameRegistry.register(item);
	}
	
	private static void register(Block block, ItemBlock ib, String name, String unlocal)
	{
		block.setUnlocalizedName(unlocal).setRegistryName(NomadicTents.MODID, name);
		ib.setUnlocalizedName(unlocal).setRegistryName(NomadicTents.MODID, name);
		GameRegistry.register(block);
		GameRegistry.register(ib);
	}
}
