package com.ricardothecoder.yac.structures;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureBase
{
	protected HashMap<Integer, HashMap<BlockPos, Block>> layout = new HashMap<Integer, HashMap<BlockPos, Block>>();
	
	protected ArrayList<Block> fillerBlocks = new ArrayList<Block>();
	
	public World world;
	public BlockPos startPos;
	
	private String unlocalizedName;
	
	public StructureBase(World worldIn, BlockPos basePosition) // USE THE CONTRUCTOR TO BUILD THE LAYOUT 
	{
		world = worldIn;
		startPos = basePosition;
	}
	
	protected boolean isLevelComplete(int level)
	{
		boolean completed = true;
		
		for (BlockPos pos : layout.get(level).keySet())
		{
			BlockPos truePos = new BlockPos(startPos.getX() + pos.getX(), startPos.getY() + pos.getY(), startPos.getZ() + pos.getZ());
			Block toCheck = layout.get(level).get(pos);
			
			if ((toCheck != null && world.getBlockState(truePos).getBlock() != toCheck) || (toCheck == null && !fillerBlocks.contains(world.getBlockState(truePos).getBlock())))
			{
				completed = false;
				break;
			}
		}
		
		return completed;
	}
	
	public boolean isLevel(int level)
	{
		boolean isToLevel = false;
		
		for (int i = 1; i <= level; i++)
		{
			isToLevel = isLevelComplete(i);
			
			if (!isToLevel)
				break;
		}
		
		return isToLevel;
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
