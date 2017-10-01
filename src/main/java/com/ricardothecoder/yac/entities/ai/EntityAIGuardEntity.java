package com.ricardothecoder.yac.entities.ai;

import com.ricardothecoder.yac.entities.EntityGuard;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIGuardEntity extends EntityAIBase
{
	private final EntityGuard theGuard;
    private EntityLivingBase theOwner;
    World theWorld;
    private final double followSpeed;
    private final PathNavigate guardPathfinder;
    private int timeToRecalcPath;
    float maxDist;
    float minDist;
    private float oldWaterCost;

    public EntityAIGuardEntity(EntityGuard theGuardIn, double followSpeedIn, float minDistIn, float maxDistIn)
    {
        this.theGuard = theGuardIn;
        this.theWorld = theGuardIn.worldObj;
        this.followSpeed = followSpeedIn;
        this.guardPathfinder = theGuardIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);

        if (!(theGuardIn.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for GuardEntityGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.theGuard.getGuardEntity();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).isSpectator())
        {
            return false;
        }
        else if (this.theGuard.isIdling())
        {
            return false;
        }
        else if (this.theGuard.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist))
        {
            return false;
        }
        else
        {
            this.theOwner = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.guardPathfinder.noPath() && this.theGuard.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist) && !this.theGuard.isIdling();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.theGuard.getPathPriority(PathNodeType.WATER);
        this.theGuard.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theOwner = null;
        this.guardPathfinder.clearPathEntity();
        this.theGuard.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    private boolean isEmptyBlock(BlockPos pos)
    {
        IBlockState iblockstate = this.theWorld.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR ? true : !iblockstate.isFullCube();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.theGuard.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.theGuard.getVerticalFaceSpeed());

        if (!this.theGuard.isIdling())
        {
            if (--this.timeToRecalcPath <= 0)
            {
                this.timeToRecalcPath = 10;

                if (!this.guardPathfinder.tryMoveToEntityLiving(this.theOwner, this.followSpeed))
                {
                    if (!this.theGuard.getLeashed())
                    {
                        if (this.theGuard.getDistanceSqToEntity(this.theOwner) >= 144.0D)
                        {
                            int i = MathHelper.floor_double(this.theOwner.posX) - 2;
                            int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
                            int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l)
                            {
                                for (int i1 = 0; i1 <= 4; ++i1)
                                {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isFullyOpaque() && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1)))
                                    {
                                        this.theGuard.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theGuard.rotationYaw, this.theGuard.rotationPitch);
                                        this.guardPathfinder.clearPathEntity();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
