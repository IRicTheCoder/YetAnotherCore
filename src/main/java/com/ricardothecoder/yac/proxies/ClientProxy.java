package com.ricardothecoder.yac.proxies;

import com.ricardothecoder.yac.guis.GuiScreenCatalogue;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenders() { }
	
	@Override
	public void displayCatalogue(ItemStack book)
	{
		super.displayCatalogue(book);
		Minecraft.getMinecraft().displayGuiScreen(new GuiScreenCatalogue(book));
	}
}
