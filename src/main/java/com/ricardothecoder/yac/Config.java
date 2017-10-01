package com.ricardothecoder.yac;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config
{
	
	// Instance
	public static Configuration config;
	
	public static void load(File configFile)
	{
		if (config == null)
			config = new Configuration(configFile);
		
		//config.getStringList("GameRules", "GameRules", new String[] {}, "Custom GameRules added by mods using this core");
		
		if (config.hasChanged())
			config.save();
	}
}
