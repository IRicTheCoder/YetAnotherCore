package com.ricardothecoder.yac.world;

import java.util.Map;

import com.ricardothecoder.yac.Config;
import com.ricardothecoder.yac.References;
import com.ricardothecoder.yac.util.ModLogger;

import net.minecraft.world.World;
import net.minecraftforge.common.config.Property;

public class GameruleManager
{
	public static void registerGameRule(String name, String value, String modname)
	{
		if (!Config.config.hasCategory("GameRules") || (Config.config.hasCategory("GameRules") && !Config.config.hasKey("GameRules", name)))
		{
			ModLogger.info(References.NAME, "Registering a new Game Rule. Name: " + name + " Default: " + value + " By Mod: " + modname);
			Config.config.getString(name, "GameRules", value, "Default value for the game rule (Registered by " + modname + ")");
		}
		
		if (Config.config.hasChanged())
			Config.config.save();
	}
	
	public static void setGameRule(World world, String name, String value)
	{
		world.getGameRules().setOrCreateGameRule(name, value);
	}
	
	public static String getGameRule(World world, String name)
	{
		return world.getGameRules().getString(name);
	}
	
	public static Map<String,Property> getAllGameRules()
	{
		return Config.config.getCategory("GameRules").getValues();
	}
}
