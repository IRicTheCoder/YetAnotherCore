package com.ricardothecoder.yac.util;

import java.awt.Color;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityUtil
{
	private static int entityID = 0;
	
	public static void registerEntity(Class<? extends Entity> entityClass, String entityName, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, Color eggPrimary, Color eggSecondary)
	{
		entityID++;
		EntityRegistry.registerModEntity(entityClass, entityName, entityID, mod, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimary.getRGB(), eggSecondary.getRGB());
	}
	
	public static void registerEntity(Class<? extends Entity> entityClass, String entityName, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		entityID++;
		EntityRegistry.registerModEntity(entityClass, entityName, entityID, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
}
