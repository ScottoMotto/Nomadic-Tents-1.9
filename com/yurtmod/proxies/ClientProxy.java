package com.yurtmod.proxies;

import com.yurtmod.content.BlockTepeeWall;
import com.yurtmod.content.Content;
import com.yurtmod.dimension.StructureType;
import com.yurtmod.main.YurtMain;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

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
			String modelName = YurtMain.MODID + ":" + StructureType.getName(i);
			namesTentItem[i] = new ResourceLocation(modelName);
			ModelLoader.setCustomModelResourceLocation(Content.itemTent, i, new ModelResourceLocation(modelName, "inventory"));
			// debug:
			//System.out.println("registering variant with name '" + modelName + "'");
		}
		for(int j = 0; j < BlockTepeeWall.NUM_TEXTURES; j++)
		{
			String modelName = Content.ibTepeeWall.getRegistryName() + "_" + j;
			namesTepeeWall[j] = new ResourceLocation(modelName);
			ModelLoader.setCustomModelResourceLocation(Content.ibTepeeWall, j, new ModelResourceLocation(modelName));
			// debug:
			//System.out.println("registering wall variant with name '" + modelName + "'");
		}
		ModelBakery.registerItemVariants(Content.itemTent, namesTentItem);
		ModelBakery.registerItemVariants(Content.ibTepeeWall, namesTepeeWall);
	}

	@Override
	public void registerRenders()
	{
		// register items		
		register(Content.itemTentCanvas);
		register(Content.itemYurtWall);
		register(Content.itemTepeeWall);
		register(Content.itemMallet);
		register(Content.itemSuperMallet);
		//// register tent item
		for(int i = 0, len = StructureType.values().length; i < len; i++)
		{
			// getName returns something like "yurt_small"
			String modelName = YurtMain.MODID + ":" + StructureType.getName(i);
			register(Content.itemTent, i, modelName);
			// debug:
			//System.out.println("registering tent item with name '" + modelName + "'");
		}
		// register blocks
		register(Content.ibBarrier);
		register(Content.ibSuperDirt);
		//// wall blocks
		register(Content.ibYurtOuterWall);
		register(Content.ibYurtInnerWall);
		register(Content.ibYurtRoof);
		//// tepee wall block
		for(int j = 0; j < BlockTepeeWall.NUM_TEXTURES; j++)
		{
			String name = Content.ibTepeeWall.getRegistryName().toString() + "_" + j;
			register(Content.ibTepeeWall, j, name);
			// debug:
			//System.out.println("registering tepee wall with name '" + name + "'");
		}
		//// door blocks		
		register(Content.ibYurtDoorSmall);
		register(Content.ibYurtDoorMed);
		register(Content.ibYurtDoorLarge);	
		register(Content.ibTepeeDoorSmall);
		register(Content.ibTepeeDoorMed);
		register(Content.ibTepeeDoorLarge);
		//// frame blocks
		register(Content.ibYurtWallFrame);
		register(Content.ibYurtRoofFrame);
		register(Content.ibTepeeWallFrame);
		
		// debug:
		//System.out.println("Finished registering inventory renders with the ItemModelMesher");
	}
	
	private void register(Item i)
	{
		register(i, 0);
	}
	
	private void register(Item i, int meta)
	{
		register(i, meta, i.getRegistryName().toString());
	}
	
	private void register(Item i, int meta, String name)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(i, meta, new ModelResourceLocation(name, "inventory"));
	}
}
