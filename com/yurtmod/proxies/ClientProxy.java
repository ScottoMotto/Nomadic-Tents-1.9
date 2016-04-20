package com.yurtmod.proxies;

import com.yurtmod.content.BlockTepeeWall;
import com.yurtmod.content.Content;
import com.yurtmod.dimension.StructureHelper.StructureType;
import com.yurtmod.main.YurtMain;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy 
{
	@Override
	public void preInitRenders() 
	{
		int len = StructureType.values().length;
		ResourceLocation[] namesTentItem = new ResourceLocation[len];
		ResourceLocation[] namesTepeeWall = new ResourceLocation[BlockTepeeWall.NUM_TEXTURES];
		for(int i = 0; i < len; i++)
		{
			String modelName = YurtMain.MODID + ":" + Content.itemTent.getUnlocalizedName(new ItemStack(Content.itemTent, 1, i)).substring(5);
			namesTentItem[i] = new ResourceLocation(modelName);
			// debug:
			//System.out.println("registering variant with name '" + modelName + "'");
		}
		for(int j = 0; j < BlockTepeeWall.NUM_TEXTURES; j++)
		{
			String modelName = YurtMain.MODID + ":tepee_wall_" + j;
			namesTepeeWall[j] = new ResourceLocation(modelName);
			// debug:
			//System.out.println("registering wall variant with name '" + modelName + "'");
		}
		ModelBakery.registerItemVariants(Content.itemTent, namesTentItem);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(Content.tepeeWall), namesTepeeWall);
	}

	@Override
	public void registerRenders()
	{
		// register items		
		register(Content.itemTentCanvas, 0, Content.N_CANVAS);
		register(Content.itemYurtWall, 0, Content.N_WALL);
		register(Content.itemTepeeWall, 0, Content.N_WALL2);
		register(Content.itemMallet, 0, Content.N_MALLET);
		register(Content.itemSuperMallet, 0, Content.N_SUPER_MALLET);
		//// register tent item
		for(int i = 0, len = StructureType.values().length; i < len; i++)
		{
			String modelName = Content.itemTent.getUnlocalizedName(new ItemStack(Content.itemTent, 1, i)).substring(5);
			register(Content.itemTent, i, modelName);
			// debug:
			//System.out.println("registering model with name '" + modelName + "'");
		}
		// register blocks
		register(Content.barrier, 0, Content.N_BARRIER);
		register(Content.indestructibleDirt, 0, Content.N_FLOOR);
		//// wall blocks
		register(Content.yurtOuterWall, 0, Content.N_WALL_OUTER);
		register(Content.yurtInnerWall, 0, Content.N_WALL_INNER);
		register(Content.yurtRoof, 0, Content.N_ROOF);
		//// tepee wall block
		for(int j = 0; j < BlockTepeeWall.NUM_TEXTURES; j++)
		{
			register(Content.tepeeWall, j, Content.N_TEPEE_WALL + "_" + j);
		}
		//// door blocks		
		register(Content.yurtDoorSmall, 0, Content.N_DOOR_SMALL);
		register(Content.yurtDoorMed, 0, Content.N_DOOR_MED);
		register(Content.yurtDoorLarge, 0, Content.N_DOOR_LARGE);	
		register(Content.tepeeDoorSmall, 0, Content.N_TEPEE_DOOR_SMALL);
		register(Content.tepeeDoorMed, 0, Content.N_TEPEE_DOOR_MED);
		register(Content.tepeeDoorLarge, 0, Content.N_TEPEE_DOOR_LARGE);
		//// frame blocks
		register(Content.yurtWallFrame, 0, Content.N_FRAME_WALL);
		register(Content.yurtRoofFrame, 0, Content.N_FRAME_ROOF);
		register(Content.tepeeWallFrame, 0, Content.N_FRAME_TEPEE);
		
		// debug:
		//System.out.println("Finished registering inventory renders with the ItemModelMesher");
	}
	
	private void register(Item i, int meta, String model)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(i, meta, getBasicModelLoc(model));
	}
	
	private void register(Block b, int meta, String blockstateName)
	{
		register(Item.getItemFromBlock(b), meta, blockstateName);
	}

	/** @return a default ModelResourceLocation for this mod */
	private ModelResourceLocation getBasicModelLoc(String modelName)
	{
		return new ModelResourceLocation(YurtMain.MODID + ":" + modelName, "inventory");
	}
}
