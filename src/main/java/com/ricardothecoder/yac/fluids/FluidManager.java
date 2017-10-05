package com.ricardothecoder.yac.fluids;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import com.ricardothecoder.yac.blocks.BlockCustomFluid;
import com.ricardothecoder.yac.util.ColorUtil;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FluidManager
{
	public static BlockCustomFluid registerPlacableCustomFluid(String name, String modid)
	{
		return new BlockCustomFluid(new CustomFluid(name, modid), modid);
	}
	
	public static CustomFluid registerCustomFluid(String name, String modid)
	{
		return new CustomFluid(name, modid);
	}
	
	public static Fluid[] getContainableFluids()
	{
	    if (!FluidRegistry.isUniversalBucketEnabled() || FluidRegistry.getBucketFluids().size() <= 0)
	        return new Fluid[] {};
	    
	    ArrayList<Fluid> bucketFluids = new ArrayList<Fluid>();
	    
	    for (Fluid fluid : FluidRegistry.getBucketFluids())
	    {
	    	ItemStack filledBucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid);
	    	boolean isWrongBucket = filledBucket.getDisplayName().equals("Universal Bucket") || filledBucket.getDisplayName().equals("Bucket");
	    	
	    	if (FluidRegistry.getFluidStack(fluid.getName(), 1000) != null && !isWrongBucket)
	    		bucketFluids.add(fluid);
	    }
	    
	    return bucketFluids.toArray(new Fluid[] {});
	}
}
