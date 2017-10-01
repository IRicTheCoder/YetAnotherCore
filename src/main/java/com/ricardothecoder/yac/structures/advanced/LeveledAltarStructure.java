package com.ricardothecoder.yac.structures.advanced;

import java.util.Arrays;
import java.util.HashMap;

import com.ricardothecoder.yac.structures.AdvancedStructureBase;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LeveledAltarStructure extends AdvancedStructureBase
{
	public LeveledAltarStructure(World worldIn, BlockPos basePosition, int circles, Block[] fillerBlocks, Block... levelBlocks)
	{
		super(worldIn, basePosition);
		this.fillerBlocks.addAll(Arrays.asList(fillerBlocks));
		
		int currLevel = 0;
		
		for (int l = 1; l <= circles; l++)
		{
			if (!layout.containsKey(l))
				layout.put(l, new HashMap<BlockPos, Block>());
			
			for (int x = -l; x <= l; x++)
			{
				for (int z = -l; z <= l; z++)
				{
					if ((x == -l && z == -l) || (x == -l && z == l) || (x == l && z == -l) || (x == l && z == l))
					{
						layout.get(l).put(new BlockPos(x, -l, z), levelBlocks[currLevel]);
						currLevel++;
					}
					else
						layout.get(l).put(new BlockPos(x, -l, z), null);
				}
			}
		}
	}
	
}
