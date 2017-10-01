package com.ricardothecoder.yac.world;

import java.util.Map;

import com.ricardothecoder.yac.util.GameruleManager;

import net.minecraft.world.World;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class WorldManager
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load event)
	{
		World world = event.getWorld();
		Map<String, Property> rules = GameruleManager.getAllGameRules();
		
		for (String gameRule : rules.keySet()){
			GameruleManager.setGameRule(world, gameRule, rules.get(gameRule).getString());
		}
	}
}
