package com.yurtmod.content;

import com.yurtmod.main.YurtMain;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Content 
{
	// begin blocks
	public static Block barrier;			public static final String N_BARRIER = "yurtmod_barrier";
	public static Block indestructibleDirt;	public static final String N_FLOOR = "yurt_floor";

	public static Block yurtRoof;			public static final String N_ROOF = "yurt_roof";
	public static Block yurtOuterWall;		public static final String N_WALL_OUTER = "yurt_wall_outer";
	public static Block yurtInnerWall;		public static final String N_WALL_INNER = "yurt_wall_inner";
	public static Block tepeeWall;			public static final String N_TEPEE_WALL = "tepee_wall";

	public static Block yurtDoorSmall;		public static final String N_DOOR_SMALL = "yurt_door_0";
	public static Block yurtDoorMed;		public static final String N_DOOR_MED = "yurt_door_1";
	public static Block yurtDoorLarge;		public static final String N_DOOR_LARGE = "yurt_door_2";
	public static Block tepeeDoorSmall; 	public static final String N_TEPEE_DOOR_SMALL = "tepee_door_0";
	public static Block tepeeDoorMed;		public static final String N_TEPEE_DOOR_MED = "tepee_door_1";
	public static Block tepeeDoorLarge;		public static final String N_TEPEE_DOOR_LARGE = "tepee_door_2";

	public static Block yurtWallFrame;		public static final String N_FRAME_WALL = "yurt_frame_wall";
	public static Block yurtRoofFrame;		public static final String N_FRAME_ROOF = "yurt_frame_roof";
	public static Block tepeeWallFrame;		public static final String N_FRAME_TEPEE = "tepee_frame_wall";

	public static Item itemTent;			public static final String N_ITEM_TENT = "tent";

	public static Item itemMallet;			public static final String N_MALLET = "mallet";
	public static Item itemSuperMallet;		public static final String N_SUPER_MALLET = "super_mallet";
	public static Item itemTentCanvas;		public static final String N_CANVAS = "tent_canvas";
	public static Item itemYurtWall;		public static final String N_WALL = "yurt_wall_piece";
	public static Item itemTepeeWall;		public static final String N_WALL2 = "tepee_wall_piece";

	public static void mainRegistry()
	{
		registerBlocks();
		registerFrameBlocks();
		registerItems();
		registerTileEntity();
	}

	private static void registerBlocks() 
	{
		// misc. blocks
		barrier = new BlockBarrier().setUnlocalizedName(N_BARRIER);
		reg(barrier);
		indestructibleDirt = new BlockUnbreakable(Material.ground).setUnlocalizedName(N_FLOOR);
		reg(indestructibleDirt);
		// yurt walls
		yurtOuterWall = new BlockYurtWall().setUnlocalizedName(N_WALL_OUTER);
		reg(yurtOuterWall);
		yurtInnerWall = new BlockYurtWall().setUnlocalizedName(N_WALL_INNER);
		reg(yurtInnerWall);
		yurtRoof = new BlockYurtRoof().setUnlocalizedName(N_ROOF);
		reg(yurtRoof);
		// tepee walls
		tepeeWall = new BlockTepeeWall().setUnlocalizedName(N_TEPEE_WALL);
		reg(tepeeWall);
		// doors
		//// yurt:
		yurtDoorSmall = new BlockTentDoor().setUnlocalizedName(N_DOOR_SMALL);
		reg(yurtDoorSmall);
		yurtDoorMed = new BlockTentDoor().setUnlocalizedName(N_DOOR_MED);
		reg(yurtDoorMed);
		yurtDoorLarge = new BlockTentDoor().setUnlocalizedName(N_DOOR_LARGE);
		reg(yurtDoorLarge);
		//// tepee:
		tepeeDoorSmall = new BlockTentDoor().setUnlocalizedName(N_TEPEE_DOOR_SMALL);
		reg(tepeeDoorSmall);
		tepeeDoorMed = new BlockTentDoor().setUnlocalizedName(N_TEPEE_DOOR_MED);
		reg(tepeeDoorMed);
		tepeeDoorLarge = new BlockTentDoor().setUnlocalizedName(N_TEPEE_DOOR_LARGE);
		reg(tepeeDoorLarge);
	}

	private static void registerFrameBlocks() 
	{
		// frames
		yurtWallFrame = new BlockTentFrame(BlockTentFrame.BlockToBecome.YURT_WALL_OUTER).setUnlocalizedName(N_FRAME_WALL);
		reg(yurtWallFrame);
		yurtRoofFrame = new BlockTentFrame(BlockTentFrame.BlockToBecome.YURT_ROOF).setUnlocalizedName(N_FRAME_ROOF);
		reg(yurtRoofFrame);
		tepeeWallFrame = new BlockTentFrame(BlockTentFrame.BlockToBecome.TEPEE_WALL).setUnlocalizedName(N_FRAME_TEPEE);
		reg(tepeeWallFrame);
	}

	private static void registerItems() 
	{
		// tent item
		itemTent = new ItemTent().setUnlocalizedName(N_ITEM_TENT);
		reg(itemTent);
		// tools
		itemMallet = new ItemMallet(ToolMaterial.IRON).setUnlocalizedName(N_MALLET);
		reg(itemMallet);
		itemSuperMallet = new ItemSuperMallet(ToolMaterial.EMERALD).setUnlocalizedName(N_SUPER_MALLET);
		reg(itemSuperMallet);
		// init crafting-only items
		itemTentCanvas = new Item().setUnlocalizedName(N_CANVAS).setCreativeTab(YurtMain.tab);
		reg(itemTentCanvas);
		itemYurtWall = new Item().setUnlocalizedName(N_WALL).setCreativeTab(YurtMain.tab);
		reg(itemYurtWall);
		itemTepeeWall = new Item().setUnlocalizedName(N_WALL2).setCreativeTab(YurtMain.tab);
		reg(itemTepeeWall);
	}

	private static void registerTileEntity() 
	{
		GameRegistry.registerTileEntity(TileEntityTentDoor.class, YurtMain.MODID + "_TileEntityTentDoor");	
	}

	private static void reg(Item in)
	{
		GameRegistry.registerItem(in, in.getUnlocalizedName().substring(5));
	}

	private static void reg(Block in)
	{
		GameRegistry.registerBlock(in, in.getUnlocalizedName().substring(5));
	}
}
