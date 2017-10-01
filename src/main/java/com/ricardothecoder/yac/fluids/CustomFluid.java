package com.ricardothecoder.yac.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CustomFluid extends Fluid
{
	public CustomFluid(String name, String modid) 
	{
		super(name, new ResourceLocation(modid, "fluids/" + name + "_still"), new ResourceLocation(modid, "fluids/" + name + "_flow"));
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
	}
}
