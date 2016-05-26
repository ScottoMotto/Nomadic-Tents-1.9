package com.yurtmod.proxies;

import com.yurtmod.block.BlockTepeeWall;
import com.yurtmod.init.Content;
import com.yurtmod.init.NomadicTents;
import com.yurtmod.structure.StructureType;

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
			String modelName = NomadicTents.MODID + ":" + StructureType.getName(i);
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
		register(Content.itemBedWall);
		register(Content.itemMallet);
		register(Content.itemSuperMallet);
		//// register tent item
		for(StructureType type : StructureType.values())
		{
			String modelName = NomadicTents.MODID + ":" + StructureType.getName(type.ordinal());
			register(Content.itemTent, type.ordinal(), modelName);
		}
		// register blocks
		register(Content.ibBarrier);
		register(Content.ibSuperDirt);
		//// yurt blocks
		register(Content.ibYurtOuterWall);
		register(Content.ibYurtInnerWall);
		register(Content.ibYurtRoof);
		//// tepee blocks
		for(int j = 0; j < BlockTepeeWall.NUM_TEXTURES; j++)
		{
			String name = Content.ibTepeeWall.getRegistryName().toString() + "_" + j;
			register(Content.ibTepeeWall, j, name);
		}
		//// bedouin blocks
		register(Content.ibBedWall);
		register(Content.ibBedRoof);
		//// door blocks		
		register(Content.ibYurtDoorSmall);
		register(Content.ibYurtDoorMed);
		register(Content.ibYurtDoorLarge);	
		register(Content.ibTepeeDoorSmall);
		register(Content.ibTepeeDoorMed);
		register(Content.ibTepeeDoorLarge);
		register(Content.ibBedDoorSmall);
		register(Content.ibBedDoorMed);
		register(Content.ibBedDoorLarge);
		//// frame blocks
		register(Content.ibYurtWallFrame);
		register(Content.ibYurtRoofFrame);
		register(Content.ibTepeeWallFrame);
		register(Content.ibBedWallFrame);
		register(Content.ibBedRoofFrame);
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
