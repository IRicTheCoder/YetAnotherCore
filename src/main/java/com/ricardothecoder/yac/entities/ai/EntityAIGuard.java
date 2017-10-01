package com.ricardothecoder.yac.entities.ai;

import com.ricardothecoder.yac.entities.EntityGuard;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIGuard extends EntityAIBase
{
    private final EntityGuard entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    private int executionChance;
    private boolean mustUpdate;
    
    private int updateRate;
    private int finishTimer;
    private int order;

    public EntityAIGuard(EntityGuard creatureIn, double speedIn)
    {
        this(creatureIn, speedIn, 120);
    }
    
    public EntityAIGuard(EntityGuard creatureIn, double speedIn, int updateRate)
    {
        this(creatureIn, speedIn, 120, updateRate);
    }

    public EntityAIGuard(EntityGuard creatureIn, double speedIn, int chance, int updateRate)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
        
        this.updateRate = updateRate;
        this.finishTimer = 0;
        this.order = 0;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.mustUpdate)
        {
            if (this.finishTimer > 0)
            {
            	this.finishTimer -= 1;            	
            	return false;
            }
        }

        if (this.entity.getGuardPath().length <= 0)
        	return false;
        
        BlockPos pos = this.entity.getGuardPath()[this.order];

        if (pos == null)
        {
            return false;
        }
        else
        {
            this.xPosition = pos.getX();
            this.yPosition = pos.getY();
            this.zPosition = pos.getZ();
            this.mustUpdate = false;
            
            this.order++;
            if (this.order >= this.entity.getGuardPath().length)
            	this.order = 0;
            
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	if (this.entity.getNavigator().noPath())
    	{
    		this.finishTimer = this.updateRate;
    		return false;
    	}
    	
        return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    /**
     * Makes task to bypass chance
     */
    public void makeUpdate()
    {
        this.mustUpdate = true;
    }

    /**
     * Changes task random possibility for execution
     */
    public void setExecutionChance(int newchance)
    {
        this.executionChance = newchance;
    }
}