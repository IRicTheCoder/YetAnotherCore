package com.ricardothecoder.yac.entities.stats;

import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

public class EntityStats
{
	protected NBTTagCompound stats;

	public EntityStats()
	{
		stats = new NBTTagCompound();
	}
	
	public Set<String> getStatsNames()
	{
	    return stats.getKeySet();
	}

	public boolean isStatsEmpty()
	{
	    return stats.hasNoTags();
	}

	public boolean containsStat(String name)
	{
	    return stats.hasKey(name);
	}

	public void saveStats(NBTTagCompound compound)
	{
	    compound.merge(stats);
	}

	public void readStats(NBTTagCompound compound)
	{
	    for (String stat : getStatsNames())
	    {
	        stats.setTag(stat, compound.getTag(stat));
	    }
	}	
}
