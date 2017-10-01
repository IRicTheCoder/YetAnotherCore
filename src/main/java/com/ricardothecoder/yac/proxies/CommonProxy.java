package com.ricardothecoder.yac.proxies;

import com.ricardothecoder.yac.References;
import com.ricardothecoder.yac.util.GameruleManager;
import com.ricardothecoder.yac.world.WorldManager;
import com.ricardothecoder.yac.world.grief.GriefManager;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public static WorldManager worldManager;
	public static GriefManager griefManager;
	
	public void registerGamerules()
	{
		GameruleManager.registerGameRule("endermanGrief", "true", References.NAME);
    	GameruleManager.registerGameRule("creeperGrief", "true", References.NAME);
    	GameruleManager.registerGameRule("ghastGrief", "true", References.NAME);
    	GameruleManager.registerGameRule("witherGrief", "true", References.NAME);
	}
	
	public void registerEvents()
	{
		worldManager = new WorldManager();
    	griefManager = new GriefManager();
    	
    	MinecraftForge.EVENT_BUS.register(worldManager);
    	MinecraftForge.EVENT_BUS.register(griefManager);
	}
	
	public void unregisterEvents()
	{
		MinecraftForge.EVENT_BUS.unregister(worldManager);
    	MinecraftForge.EVENT_BUS.unregister(griefManager);
	}
	
	public void registerAddons() { }
	public void registerRenders() { }
}
