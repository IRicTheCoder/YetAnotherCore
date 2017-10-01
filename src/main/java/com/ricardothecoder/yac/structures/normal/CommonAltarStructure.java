package com.ricardothecoder.yac.structures.normal;

import java.util.Arrays;
import java.util.HashMap;

import com.ricardothecoder.yac.structures.StructureBase;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommonAltarStructure extends StructureBase
{
	public CommonAltarStructure(World worldIn, BlockPos basePosition, int levels, Block runeBlock, Block... fillerBlocks)
	{
		super(worldIn, basePosition);
		this.fillerBlocks.addAll(Arrays.asList(fillerBlocks));
		
		for (int l = 1; l <= levels; l++)
		{
			if (!layout.containsKey(l))
				layout.put(l, new HashMap<BlockPos, Block>());
			
			for (int x = -l; x <= l; x++)
			{
				for (int z = -l; z <= l; z++)
				{
					if ((x == -l && z == -l) || (x == -l && z == l) || (x == l && z == -l) || (x == l && z == l))
						layout.get(l).put(new BlockPos(x, -l, z), runeBlock);
					else
						layout.get(l).put(new BlockPos(x, -l, z), null);
				}
			}
		}
	}
}
