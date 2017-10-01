package com.ricardothecoder.yac.structures;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpawnableStructureBase
{
	protected HashMap<BlockPos, Block> layout = new HashMap<BlockPos, Block>();
	
	public World world;
	public BlockPos origin;
	
	private String unlocalizedName;
	
	public SpawnableStructureBase(World worldIn, BlockPos originPosition) // USE THE CONTRUCTOR TO BUILD THE LAYOUT 
	{
		world = worldIn;
		origin = originPosition;
	}
	
	public void spawnStructure()
	{
		for (BlockPos pos : layout.keySet())
		{
			BlockPos truePos = new BlockPos(origin.getX() + pos.getX(), origin.getY() + pos.getY(), origin.getZ() + pos.getZ());
			Block toSet = layout.get(pos);
			
			world.setBlockState(truePos, toSet.getDefaultState());
		}
	}
	
	private void setUnlocalizedName(String name)
	{
		unlocalizedName = name;
	}
	
	private String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public String getLocalizedLevel(int level)
	{
		return I18n.format("structure." + unlocalizedName + "." + level);
	}
}
