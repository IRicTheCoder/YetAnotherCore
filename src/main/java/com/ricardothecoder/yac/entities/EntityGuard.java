package com.ricardothecoder.yac.entities;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityGuard extends EntityCreature
{
	protected BlockPos[] guardPath = new BlockPos[0];
	protected EntityLiving guardEntity = null;
	
	protected boolean isIdle = true;
	
	public EntityGuard(World worldIn)
	{
		super(worldIn);
	}
	
	public BlockPos[] getGuardPath()
	{
		return guardPath;
	}
	
	public EntityLiving getGuardEntity()
	{
		return guardEntity;
	}
	
	public boolean isIdling()
	{
		return isIdle;
	}
}
