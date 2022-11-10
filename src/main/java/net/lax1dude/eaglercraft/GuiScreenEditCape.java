package net.lax1dude.eaglercraft;

import net.lax1dude.eaglercraft.EaglerProfile.EaglerProfileCape;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

public class GuiScreenEditCape extends GuiScreen {
	
	private boolean dropDownOpen = false;
	private String[] dropDownOptions;
	private int slotsVisible = 0;
	private int selectedSlot = 0;
	private int scrollPos = -1;
	private int skinsHeight = 0;
	private boolean dragging = false;
	private int mousex = 0;
	private int mousey = 0;
	
	public static final String[] defaultVanillaCapeNames = new String[] {
			"No Cape",
			"Minecon 2011",
			"Minecon 2012",
			"Minecon 2013",
			"Minecon 2015",
			"Minecon 2016",
			"Microsoft Account",
			"Realms Mapmaker",
			"Mojang Old",
			"Mojang New",
			"Jira Moderator",
			"Mojang Very Old",
			"Scrolls",
			"Cobalt",
			"Lang Translator",
			"Millionth Player",
			"Prismarine",
			"Snowman",
			"Spade",
			"Birthday",
			"dB"
	};
	
	protected String screenTitle = "Select Cape";
	
	private GuiScreen parent;
	private int skinToShow;
	
	public GuiScreenEditCape(GuiScreen parent, int skinToShow) {
		this.parent = parent;
		this.skinToShow = skinToShow;
		reconcatDD();
		this.selectedSlot = EaglerProfile.presetCapeId < 0 ? EaglerProfile.customCapeId : (EaglerProfile.presetCapeId + EaglerProfile.capes.size());
	}
	
	public void initGui() {
		super.initGui();
		EaglerAdapter.enableRepeatEvents(true);
		StringTranslate var1 = StringTranslate.getInstance();
		screenTitle = var1.translateKey("profile.capeTitle");
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 21, this.height / 6 + 81, 71, 20, var1.translateKey("profile.addCape")));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 21 + 71, this.height / 6 + 81, 72, 20, var1.translateKey("profile.clearSkin")));
	}
	
	public void onGuiClosed() {
		EaglerAdapter.enableRepeatEvents(false);
	}
	
	private void reconcatDD() {
		String[] n = new String[EaglerProfile.capes.size()];
		for(int i = 0; i < n.length; ++i) {
			n[i] = EaglerProfile.capes.get(i).name;
		}
		
		this.dropDownOptions = EaglerProfile.concatArrays(n, defaultVanillaCapeNames);
	}
	
	private static final TextureLocation gui = new TextureLocation("/gui/gui.png");
	
	public void drawScreen(int mx, int my, float par3) {
		StringTranslate var1 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 15, 16777215);
		
		this.drawString(this.fontRenderer, "Player Cape", this.width / 2 - 20, this.height / 6 + 37, 10526880);
		mousex = mx;
		mousey = my;
		
		int skinX = this.width / 2 - 120;
		int skinY = this.height / 6 + 8;
		int skinWidth = 80;
		int skinHeight = 130;
		
		drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
		drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, 0xff000015);
		
		if(dropDownOpen) {
			super.drawScreen(-1, -1, par3);
		}else {
			super.drawScreen(mx, my, par3);
		}
		
		skinX = this.width / 2 - 20;
		skinY = this.height / 6 + 53;
		skinWidth = 140;
		skinHeight = 22;
		
		drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
		drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 21, skinY + skinHeight - 1, -16777216);
		drawRect(skinX + skinWidth - 20, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, -16777216);
		
		EaglerAdapter.glColor4f(1f, 1f, 1f, 1f);
		gui.bindTexture();
		drawTexturedModalRect(skinX + skinWidth - 18, skinY + 3, 0, 240, 16, 16);
		
		this.fontRenderer.drawStringWithShadow(dropDownOptions[selectedSlot], skinX + 5, skinY + 7, 14737632);
		
		EaglerProfile.customCapeId = (selectedSlot < EaglerProfile.capes.size()) ? selectedSlot : -1;
		EaglerProfile.presetCapeId = EaglerProfile.customCapeId < 0 ? (selectedSlot - EaglerProfile.capes.size()) : -1;
		
		skinX = this.width / 2 - 20;
		skinY = this.height / 6 + 74;
		skinWidth = 140;
		skinHeight = (this.height - skinY - 4);
		slotsVisible = (skinHeight / 10);
		if(slotsVisible > dropDownOptions.length) slotsVisible = dropDownOptions.length;
		skinHeight = slotsVisible * 10 + 7;
		skinsHeight = skinHeight;
		if(scrollPos == -1) {
			scrollPos = selectedSlot - 2;
		}
		if(scrollPos > (dropDownOptions.length - slotsVisible)) {
			scrollPos = (dropDownOptions.length - slotsVisible);
		}
		if(scrollPos < 0) {
			scrollPos = 0;
		}
		if(dropDownOpen) {
			drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
			drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, -16777216);
			for(int i = 0; i < slotsVisible; i++) {
				if(i + scrollPos < dropDownOptions.length) {
					int idx = i + scrollPos - EaglerProfile.capes.size();
					if(selectedSlot == i + scrollPos) {
						drawRect(skinX + 1, skinY + i*10 + 4, skinX + skinWidth - 1, skinY + i*10 + 14, 0x77ffffff);
					}else if(mx >= skinX && mx < (skinX + skinWidth - 10) && my >= (skinY + i*10 + 5) && my < (skinY + i*10 + 15)) {
						drawRect(skinX + 1, skinY + i*10 + 4, skinX + skinWidth - 1, skinY + i*10 + 14, 0x55ffffff);
					}
					this.fontRenderer.drawStringWithShadow(dropDownOptions[i + scrollPos], skinX + 5, skinY + 5 + i*10, 14737632);
				}
			}
			int scrollerSize = skinHeight * slotsVisible / dropDownOptions.length;
			int scrollerPos = skinHeight * scrollPos / dropDownOptions.length;
			drawRect(skinX + skinWidth - 4, skinY + scrollerPos + 1, skinX + skinWidth - 1, skinY + scrollerPos + scrollerSize, 0xff888888);
		}
		
		int xx = this.width / 2 - 80;
		int yy = this.height / 6 + 130;
		
		DefaultSkinRenderer.renderPlayerPreview(xx, yy, mx, my, skinToShow | 0x10000);
		 
	}
	
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(!dropDownOpen) {
			if(par1GuiButton.id == 200) {
				mc.displayGuiScreen(parent);
			}else if(par1GuiButton.id == 2) {
				EaglerAdapter.openFileChooser("png", "image/png");
			}else if(par1GuiButton.id == 3) {
				for(EaglerProfileCape i : EaglerProfile.capes) {
					this.mc.renderEngine.deleteTexture(i.glTex);
				}
				EaglerProfile.capes.clear();
				this.dropDownOptions = defaultVanillaCapeNames;
				this.selectedSlot = 0;
			}
		}
	}
	
	public void updateScreen() {
		if(dropDownOpen) {
			if(EaglerAdapter.mouseIsButtonDown(0)) {
				int skinX = this.width / 2 - 20;
				int skinY = this.height / 6 + 74;
				int skinWidth = 140;
				if(mousex >= (skinX + skinWidth - 10) && mousex < (skinX + skinWidth) && mousey >= skinY && mousey < (skinY + skinsHeight)) {
					dragging = true;
				}
				if(dragging) {
					int scrollerSize = skinsHeight * slotsVisible / dropDownOptions.length;
					scrollPos = (mousey - skinY - (scrollerSize / 2)) * dropDownOptions.length / skinsHeight;
				}
			}else {
				dragging = false;
			}
		}else {
			dragging = false;
		}
		
		byte[] b;
		if((b = EaglerAdapter.getFileChooserResult()) != null && b.length > 0) {
			EaglerImage img = EaglerImage.loadImage(b);
			
			if(!((img.w == 32 && img.h == 32) || (img.w == 64 && img.h == 32))) return;
			
			int[] loadSkin = img.data;
			if(img.w == 64 && img.h == 32) {
				loadSkin = grabPiece(loadSkin, 32, 32, 64);
			}
			
			byte[] rawSkin = new byte[loadSkin.length * 4];
			for(int i = 0; i < loadSkin.length; i++) {
				int i2 = i * 4; int i3 = loadSkin[i];
				rawSkin[i2] = (byte)(i3);
				rawSkin[i2 + 1] = (byte)(i3 >> 8);
				rawSkin[i2 + 2] = (byte)(i3 >> 16);
				rawSkin[i2 + 3] = (byte)(i3 >> 24);
			}
			String name = EaglerAdapter.getFileChooserResultName();
			if(name.length() > 32) {
				name = name.substring(0, 32);
			}
			int k;
			if((k = EaglerProfile.addCape(name, rawSkin)) != -1) {
				selectedSlot = k;
				reconcatDD();
			}
		}
	}
	
	private int[] grabPiece(int[] input, int w, int h, int sw) {
		int[] ret = new int[w * h];
		for(int i = 0; i < h; ++i) {
			System.arraycopy(input, i * sw, ret, i * w, w);
		}
		return ret;
	}
	
	public void handleMouseInput() {
		super.handleMouseInput();
		if(dropDownOpen) {
			int var1 = EaglerAdapter.mouseGetEventDWheel();
			if(var1 < 0) {
				scrollPos += 3;
			}
			if(var1 > 0) {
				scrollPos -= 3;
				if(scrollPos < 0) {
					scrollPos = 0;
				}
			}
		}
	}

	protected void keyTyped(char par1, int par2) {
		if(par2 == 200 && selectedSlot > 0) {
			--selectedSlot;
			scrollPos = selectedSlot - 2;
		}
		if(par2 == 208 && selectedSlot < (dropDownOptions.length - 1)) {
			++selectedSlot;
			scrollPos = selectedSlot - 2;
		}
	}
	
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		
		if (par3 == 0) {
			int skinX = this.width / 2 + 140 - 40;
			int skinY = this.height / 6 + 53;
		
			if(par1 >= skinX && par1 < (skinX + 20) && par2 >= skinY && par2 < (skinY + 22)) {
				dropDownOpen = !dropDownOpen;
			}
			
			skinX = this.width / 2 - 20;
			skinY = this.height / 6 + 53;
			int skinWidth = 140;
			int skinHeight = skinsHeight;
			
			if(!(par1 >= skinX && par1 < (skinX + skinWidth) && par2 >= skinY && par2 < (skinY + skinHeight + 22))) {
				dropDownOpen = false;
				dragging = false;
			}
			
			skinY += 21;
			
			if(dropDownOpen && !dragging) {
				for(int i = 0; i < slotsVisible; i++) {
					if(i + scrollPos < dropDownOptions.length) {
						if(selectedSlot != i + scrollPos) {
							if(par1 >= skinX && par1 < (skinX + skinWidth - 10) && par2 >= (skinY + i*10 + 5) && par2 < (skinY + i*10 + 15) && selectedSlot != i + scrollPos) {
								selectedSlot = i + scrollPos;
								dropDownOpen = false;
								dragging = false;
							}
						}
					}
				}
			}
		}
	}
	
}
