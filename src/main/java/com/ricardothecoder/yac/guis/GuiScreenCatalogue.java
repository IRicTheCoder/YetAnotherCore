package com.ricardothecoder.yac.guis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.ricardothecoder.yac.References;
import com.ricardothecoder.yac.items.ItemCatalogue;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScreenCatalogue extends GuiScreen
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
	private ResourceLocation CATALOGUE_GUI_TEXTURES = new ResourceLocation(References.MODID, "textures/gui/catalogue.png");
	/** The player editing the book */
	private final EntityPlayer editingPlayer;
	private final ItemStack bookObj;
	/** Whether the book's title or contents has been modified since being opened */
	private boolean bookIsModified;
	/** Update ticks since the gui was opened */
	private int updateCount;
	private final static int bookImageWidth = 256;
	private final static int bookImageHeight = 192;
	private int bookTotalPages = 1;
	private int currPage;
	private ArrayList<String> entries = new ArrayList<String>();
	private String bookTitle = "";
	private List<ITextComponent> cachedComponents;
	private int cachedPage = -1;
	private GuiScreenCatalogue.NextPageButton buttonNextPage;
	private GuiScreenCatalogue.NextPageButton buttonPreviousPage;
	private GuiButton buttonDone;
	/** The GuiButton to sign this book. */
	private GuiButton buttonSign;
	private GuiButton buttonFinalize;
	private GuiButton buttonCancel;

	public GuiScreenCatalogue(EntityPlayer player, ItemStack book)
	{
		this.editingPlayer = player;
		this.bookObj = book;

		if (book.getItem() instanceof ItemCatalogue)
		{
			CATALOGUE_GUI_TEXTURES = ((ItemCatalogue)book.getItem()).getGUITexture();
		}
		
		if (book.hasTagCompound())
		{
			NBTTagCompound tag = book.getTagCompound();

			int entryID = 0;
			boolean hasNextEntry = tag.hasKey("entry" + entryID);
			while (hasNextEntry)
			{
				entries.add(tag.getString("entry" + entryID));            	

				entryID++;
				hasNextEntry = tag.hasKey("entry" + entryID);
			}

			this.bookTotalPages = MathHelper.ceiling_double_int(entries.size() / 14);
			
			if (entries.size() > this.bookTotalPages * 14)
				this.bookTotalPages++;
			
			if (this.bookTotalPages <= 0)
				this.bookTotalPages = 1;
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		super.updateScreen();
		++this.updateCount;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui()
	{
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);

		this.buttonDone = this.addButton(new GuiButton(0, this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done", new Object[0])));

		int i = (this.width - 192) / 2;
		int j = 2;
		this.buttonNextPage = (GuiScreenCatalogue.NextPageButton)this.addButton(new GuiScreenCatalogue.NextPageButton(1, i + 120, 156, true));
		this.buttonPreviousPage = (GuiScreenCatalogue.NextPageButton)this.addButton(new GuiScreenCatalogue.NextPageButton(2, i + 38, 156, false));
		this.updateButtons();
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	private void updateButtons()
	{
		this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1;
		this.buttonPreviousPage.visible = this.currPage > 0;
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled)
		{
			if (button.id == 0)
			{
				this.mc.displayGuiScreen((GuiScreen)null);
			}
			else if (button.id == 1)
			{
				if (this.currPage < this.bookTotalPages - 1)
				{
					++this.currPage;
				}
			}
			else if (button.id == 2)
			{
				if (this.currPage > 0)
				{
					--this.currPage;
				}
			}

			this.updateButtons();
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CATALOGUE_GUI_TEXTURES);
		int i = (this.width - bookImageWidth) / 2;
		int j = 2;
		this.drawTexturedModalRect(i, 2, 0, 0, bookImageWidth, bookImageHeight);


		String s4 = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages)});
		String s5 = "";

		if (this.entries.size() > 0 && this.currPage >= 0 && this.currPage < this.bookTotalPages)
		{
			int startEntry = this.currPage * 14;
			for (int entry = startEntry; entry < startEntry + 14; entry++)
			{
				if (entries.size() <= entry)
					break;
				
				s5 += this.entries.get(entry) + "\n";
			}
		}

		if (this.cachedPage != this.currPage)
		{
			try
			{
				ITextComponent itextcomponent = new TextComponentString(s5);
				this.cachedComponents = itextcomponent != null ? GuiUtilRenderComponents.splitText(itextcomponent, bookImageWidth - 76, this.fontRendererObj, true, true) : null;
			}
			catch (JsonParseException var13)
			{
				this.cachedComponents = null;
			}

			this.cachedPage = this.currPage;
		}

		int j1 = this.fontRendererObj.getStringWidth(s4);
		this.fontRendererObj.drawString(s4, i - j1 + bookImageWidth - 44, 18, 0);

		if (this.cachedComponents == null)
		{
			this.fontRendererObj.drawSplitString(s5, i + 36, 34, bookImageWidth - 76, 0);
		}
		else
		{
			int k1 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.cachedComponents.size());

			for (int l1 = 0; l1 < k1; ++l1)
			{
				ITextComponent itextcomponent2 = (ITextComponent)this.cachedComponents.get(l1);
				this.fontRendererObj.drawString(itextcomponent2.getUnformattedText(), i + 36, 34 + l1 * this.fontRendererObj.FONT_HEIGHT, 0);
			}

			ITextComponent itextcomponent1 = this.getClickedComponentAt(mouseX, mouseY);

			if (itextcomponent1 != null)
			{
				this.handleComponentHover(itextcomponent1, mouseX, mouseY);
			}
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseButton == 0)
		{
			ITextComponent itextcomponent = this.getClickedComponentAt(mouseX, mouseY);

			if (itextcomponent != null && this.handleComponentClick(itextcomponent))
			{
				return;
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Executes the click event specified by the given chat component
	 */
	protected boolean handleComponentClick(ITextComponent component)
	{
		ClickEvent clickevent = component.getStyle().getClickEvent();

		if (clickevent == null)
		{
			return false;
		}
		else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE)
		{
			String s = clickevent.getValue();

			try
			{
				int i = Integer.parseInt(s) - 1;

				if (i >= 0 && i < this.bookTotalPages && i != this.currPage)
				{
					this.currPage = i;
					this.updateButtons();
					return true;
				}
			}
			catch (Throwable var5)
			{
				;
			}

			return false;
		}
		else
		{
			boolean flag = super.handleComponentClick(component);

			if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
			{
				this.mc.displayGuiScreen((GuiScreen)null);
			}

			return flag;
		}
	}

	@Nullable
	public ITextComponent getClickedComponentAt(int p_175385_1_, int p_175385_2_)
	{
		if (this.cachedComponents == null)
		{
			return null;
		}
		else
		{
			int i = p_175385_1_ - (this.width - bookImageWidth) / 2 - 36;
			int j = p_175385_2_ - 2 - 16 - 16;

			if (i >= 0 && j >= 0)
			{
				int k = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.cachedComponents.size());

				if (i <= 116 && j < this.mc.fontRendererObj.FONT_HEIGHT * k + k)
				{
					int l = j / this.mc.fontRendererObj.FONT_HEIGHT;

					if (l >= 0 && l < this.cachedComponents.size())
					{
						ITextComponent itextcomponent = (ITextComponent)this.cachedComponents.get(l);
						int i1 = 0;

						for (ITextComponent itextcomponent1 : itextcomponent)
						{
							if (itextcomponent1 instanceof TextComponentString)
							{
								i1 += this.mc.fontRendererObj.getStringWidth(((TextComponentString)itextcomponent1).getText());

								if (i1 > i)
								{
									return itextcomponent1;
								}
							}
						}
					}

					return null;
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	static class NextPageButton extends GuiButton
	{
		private final boolean isForward;

		public NextPageButton(int p_i46316_1_, int p_i46316_2_, int p_i46316_3_, boolean p_i46316_4_)
		{
			super(p_i46316_1_, p_i46316_2_, p_i46316_3_, 23, 13, "");
			this.isForward = p_i46316_4_;
		}

		/**
		 * Draws this button to the screen.
		 */
		public void drawButton(Minecraft mc, int mouseX, int mouseY)
		{
			if (this.visible)
			{
				boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(GuiScreenCatalogue.BOOK_GUI_TEXTURES);
				int i = 0;
				int j = 192;

				if (flag)
				{
					i += 23;
				}

				if (!this.isForward)
				{
					j += 13;
				}

				this.drawTexturedModalRect(this.xPosition, this.yPosition, i, j, 23, 13);
			}
		}
	}
}