package com.ricardothecoder.yac.structures.spawnables;

import com.ricardothecoder.yac.structures.SpawnableStructureBase;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EndPortalSctruture extends SpawnableStructureBase
{
	public EndPortalSctruture(World worldIn, BlockPos originPosition, Block toSpawn)
	{
		super(worldIn, originPosition);
		
		for (int x = -1; x <= 1; x++)
		{
			for (int z = -1; z <= 1; z++)
			{
				layout.put(new BlockPos(x, 0, z), toSpawn);
			}
		}
	}

}
