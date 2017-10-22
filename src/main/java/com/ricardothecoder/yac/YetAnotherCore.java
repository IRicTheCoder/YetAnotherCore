package com.ricardothecoder.yac;

import com.ricardothecoder.yac.proxies.CommonProxy;
import com.ricardothecoder.yac.util.ModLogger;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = References.MODID, version = References.VERSION, name = References.NAME, acceptedMinecraftVersions="[1.10,)", dependencies="before:*;")
public class YetAnotherCore
{
	@Instance
	public static YetAnotherCore instance;
	
	@SidedProxy(serverSide="com.ricardothecoder.yac.proxies.CommonProxy", clientSide="com.ricardothecoder.yac.proxies.ClientProxy")
	public static CommonProxy proxy;

	static
	{
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModLogger.info(References.NAME, "PRE-INITIALIZATION");
		
		Config.load(event.getSuggestedConfigurationFile());
	}
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	ModLogger.info(References.NAME, "INITIALIZATION");
    	ModLogger.info(References.NAME, "Registering Game Rules");
    	
    	proxy.registerGamerules();
    	proxy.registerRenders();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	ModLogger.info(References.NAME, "POST-INITIALIZATION");
    }
    
    @EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event)
    {
    	ModLogger.info(References.NAME, "SERVER ABOUT TO START");
    	ModLogger.info(References.NAME, "Registering Events");
    	
    	proxy.registerEvents();
    }
    
    public void serverStopped(FMLServerStoppedEvent event)
    {
    	ModLogger.info(References.NAME, "SERVER STOPPED");
    	ModLogger.info(References.NAME, "Unregistering Events");
    	
    	proxy.unregisterEvents();
    }
}
