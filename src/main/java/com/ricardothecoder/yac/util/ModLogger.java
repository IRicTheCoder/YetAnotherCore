package com.ricardothecoder.yac.util;

import org.apache.logging.log4j.Level;

import com.ricardothecoder.yac.References;

import net.minecraftforge.fml.common.FMLLog;

public class ModLogger
{
	private static void log(String modname, Level level, String message)
	{
		FMLLog.log(modname, level, message);
	}
	
	public static void info(String modname, String message)
	{
		log(modname, Level.INFO, message);
	}
	
	public static void debug(String modname, String message)
	{
		log(modname, Level.DEBUG, message);
	}
	
	public static void error(String modname, String message)
	{
		log(modname, Level.ERROR, message);
	}
	
	public static void all(String modname, String message)
	{
		log(modname, Level.ALL, message);
	}
	
	public static void off(String modname, String message)
	{
		log(modname, Level.OFF, message);
	}
	
	public static void warn(String modname, String message)
	{
		log(modname, Level.WARN, message);
	}
	
	public static void trace(String modname, String message)
	{
		log(modname, Level.TRACE, message);
	}
	
	public static void fatal(String modname, String message)
	{
		log(modname, Level.FATAL, message);
	}
}
