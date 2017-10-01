package com.ricardothecoder.yac.util;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class SpawnUtil
{
	public static void addSpawnByType(Class<? extends EntityLiving> entity, int weightedProb, int min, int max, EnumCreatureType type, BiomeDictionary.Type... bTypes)
	{
		ArrayList<Biome> biomes = new ArrayList<Biome>();
		
		for (BiomeDictionary.Type bType : bTypes)
		{
			biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(bType)));
		}
		
		EntityRegistry.addSpawn(entity, weightedProb, min, max, type, biomes.toArray(new Biome[biomes.size()]));
	}
	
	public static void removeSpawnByType(Class<? extends EntityLiving> entity, EnumCreatureType type, BiomeDictionary.Type... bTypes)
	{
		ArrayList<Biome> biomes = new ArrayList<Biome>();
		
		for (BiomeDictionary.Type bType : bTypes)
		{
			biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(bType)));
		}
		
		EntityRegistry.removeSpawn(entity, type, biomes.toArray(new Biome[biomes.size()]));
	}
}
