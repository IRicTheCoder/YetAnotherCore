package com.ricardothecoder.yac.blocks;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCustomFluid extends BlockFluidClassic
{	
	public BlockCustomFluid(Fluid fluid, String modid)
	{
		super(fluid, Material.WATER);
		setRegistryName(modid, fluid.getName());
		setUnlocalizedName(fluid.getName());
		
		disableStats();
		
		GameRegistry.register(this);
	}
	
	private boolean isFluid(@Nonnull IBlockState blockstate)
	{
		return blockstate.getMaterial().isLiquid() || blockstate.getBlock() instanceof IFluidBlock;
	}
	
	@Override
	@Nonnull
	public IBlockState getExtendedState(IBlockState oldState, IBlockAccess worldIn, BlockPos pos) 
	{
		IExtendedBlockState state = (IExtendedBlockState) oldState;
		state = state.withProperty(FLOW_DIRECTION, (float)getFlowDirection(worldIn, pos));
		IBlockState[][] upBlockState = new IBlockState[3][3];
		float[][] height = new float[3][3];
		float[][] corner = new float[2][2];

		upBlockState[1][1] = worldIn.getBlockState(pos.down(densityDir));
		height[1][1] = this.getFluidHeightForRender(worldIn, pos, upBlockState[1][1]);

		if(height[1][1] == 1)
		{
			for(int i = 0; i < 2; i++)
			{
				for(int j = 0; j < 2; j++)
				{
					corner[i][j] = 1;
				}
			}
		}
		else
		{
			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 3; j++)
				{
					if (i != 1 || j != 1)
					{
						upBlockState[i][j] = worldIn.getBlockState(pos.add(i - 1, 0, j - 1).down(densityDir));
						height[i][j] = this.getFluidHeightForRender(worldIn, pos.add(i - 1, 0, j - 1), upBlockState[i][j]);
					}
				}
			}


			for (int i = 0; i < 2; i++)
			{
				for (int j = 0; j < 2; j++)
				{
					corner[i][j] = getFluidHeightAverage(height[i][j], height[i][j + 1], height[i + 1][j], height[i + 1][j + 1]);
				}
			}

			//check for downflow above corners
			boolean n =  isFluid(upBlockState[0][1]);
			boolean s =  isFluid(upBlockState[2][1]);
			boolean w =  isFluid(upBlockState[1][0]);
			boolean e =  isFluid(upBlockState[1][2]);
			boolean nw = isFluid(upBlockState[0][0]);
			boolean ne = isFluid(upBlockState[0][2]);
			boolean sw = isFluid(upBlockState[2][0]);
			boolean se = isFluid(upBlockState[2][2]);
			if (nw || n || w)
			{
				corner[0][0] = 1;
			}
			if (ne || n || e)
			{
				corner[0][1] = 1;
			}
			if (sw || s || w)
			{
				corner[1][0] = 1;
			}
			if (se || s || e)
			{
				corner[1][1] = 1;
			}
		}

		state = state.withProperty(LEVEL_CORNERS[0], corner[0][0]);
		state = state.withProperty(LEVEL_CORNERS[1], corner[0][1]);
		state = state.withProperty(LEVEL_CORNERS[2], corner[1][1]);
		state = state.withProperty(LEVEL_CORNERS[3], corner[1][0]);
		return state;
	}


	public float getFluidHeightForRender(IBlockAccess world, BlockPos pos, @Nonnull IBlockState up) 
	{
		IBlockState here = world.getBlockState(pos);
		if (here.getBlock() == this)
		{
			if (up.getMaterial().isLiquid() || up.getBlock() instanceof IFluidBlock)
			{
				return 1;
			}

			if (getMetaFromState(here) == getMaxRenderHeightMeta())
			{
				return 0.875F;
			}
		}
		if (here.getBlock() instanceof BlockLiquid)
		{
			return Math.min(1 - BlockLiquid.getLiquidHeightPercent(here.getValue(BlockLiquid.LEVEL)), 14f / 16);
		}
		return !here.getMaterial().isSolid() && up.getBlock() == this ? 1 : this.getQuantaPercentage(world, pos) * 0.875F;
	}
	
	@SideOnly(Side.CLIENT)
	public void render()
	{
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
	}
}
