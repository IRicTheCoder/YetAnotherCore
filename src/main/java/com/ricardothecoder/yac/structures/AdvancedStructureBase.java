package com.ricardothecoder.yac.structures;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdvancedStructureBase
{
	protected HashMap<Integer, HashMap<BlockPos, Block>> layout = new HashMap<Integer, HashMap<BlockPos, Block>>();
	
	protected ArrayList<Block> fillerBlocks = new ArrayList<Block>();
	
	public World world;
	public BlockPos startPos;
	
	private String unlocalizedName;
	
	public AdvancedStructureBase(World worldIn, BlockPos basePosition) // USE THE CONTRUCTOR TO BUILD THE LAYOUT 
	{
		world = worldIn;
		startPos = basePosition;
	}
	
	protected int completedLevels()
	{
		int completed = 0;
		boolean running = true;
		
		for (int circle = 0; circle < layout.size(); circle++){
			for (BlockPos pos : layout.get(circle).keySet())
			{
				BlockPos truePos = new BlockPos(startPos.getX() + pos.getX(), startPos.getY() + pos.getY(), startPos.getZ() + pos.getZ());
				Block toCheck = layout.get(circle).get(pos);
				
				if (toCheck != null)
				{
					if (world.getBlockState(truePos).getBlock() == toCheck)
						completed++;
					else if (fillerBlocks.contains(world.getBlockState(truePos).getBlock()))
						continue;
					else
					{
						running = false;
						break;
					}
				}
				
				if (toCheck == null && !fillerBlocks.contains(world.getBlockState(truePos).getBlock()))
				{
					running = false;
					break;
				}
			}
			
			if (!running)
				break;
		}
		
		return completed;
	}
	
	public boolean isLevel(int level)
	{
		return completedLevels() >= level;
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
