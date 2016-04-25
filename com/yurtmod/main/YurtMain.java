package com.yurtmod.main;

import com.yurtmod.content.Content;
import com.yurtmod.dimension.TentDimension;
import com.yurtmod.proxies.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = YurtMain.MODID, name = YurtMain.NAME, version = YurtMain.VERSION, acceptedMinecraftVersions = YurtMain.MCVERSION)
public class YurtMain 
{
	public static final String MODID = "yurtmod";
	public static final String NAME = "Nomadic Tents";
	public static final String VERSION = "4.03";
	public static final String MCVERSION = "1.9";
	public static final String CLIENT = "com." + MODID + ".proxies.ClientProxy";
	public static final String SERVER = "com." + MODID + ".proxies.CommonProxy";
	
	@SidedProxy(clientSide = CLIENT, serverSide = SERVER)
	public static CommonProxy proxy;
	
	public static CreativeTabs tab;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		tab = new CreativeTabs("yurtMain")
		{
			@Override
			public Item getTabIconItem() 
			{
				return Content.itemTent;
			}
		};
		Config.mainRegistry(new Configuration(event.getSuggestedConfigurationFile()));
		Content.mainRegistry();
		TentDimension.mainRegistry();
		proxy.preInitRenders();
	}
	
	@EventHandler
    public void init(FMLInitializationEvent event)
    {    
		Crafting.mainRegistry();
		if(event.getSide() == Side.CLIENT)
		{
			proxy.registerRenders();
		}
    }
}
