package com.ricardothecoder.yac.world.grief;

import com.ricardothecoder.yac.util.GameruleManager;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.sys.ShutdownHookThread;

public class GriefManager
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntitySpawn(EntityConstructing event)
	{
		if (event.getEntity() == null || event.getEntity().worldObj == null)
			return;
		
		if (event.getEntity().worldObj.isRemote)
			return;
		
		if (event.getEntity() instanceof EntityEnderman && GameruleManager.getGameRule(event.getEntity().worldObj, "endermanGrief").equals("false"))
		{
			EntityEnderman enderman = (EntityEnderman)event.getEntity();
			enderman.setHeldBlockState(Blocks.PISTON_HEAD.getDefaultState());
			return;
		}
		
		if (event.getEntity() instanceof EntityLargeFireball && GameruleManager.getGameRule(event.getEntity().worldObj, "ghastGrief").equals("false"))
		{
			EntityLargeFireball fireball = (EntityLargeFireball)event.getEntity();
			if (fireball.shootingEntity instanceof EntityGhast)
			{
				fireball.explosionPower = 0;
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onExplosionDetonate(ExplosionEvent.Detonate event)
	{
		if (event.getWorld() == null || event.getWorld().isRemote)
			return;
		
		if (event.getExplosion().getExplosivePlacedBy() instanceof EntityCreeper && GameruleManager.getGameRule(event.getWorld(), "creeperGrief").equals("false"))
		{
			event.getAffectedBlocks().clear();
			return;
		}
		
		if (event.getExplosion().getExplosivePlacedBy() instanceof EntityGhast && GameruleManager.getGameRule(event.getWorld(), "ghastGrief").equals("false"))
		{
			event.getAffectedBlocks().clear();
			return;
		}
		
		if (event.getExplosion().getExplosivePlacedBy() instanceof EntityWither && GameruleManager.getGameRule(event.getWorld(), "witherGrief").equals("false"))
		{
			event.getAffectedBlocks().clear();
			return;
		}
	}
}
