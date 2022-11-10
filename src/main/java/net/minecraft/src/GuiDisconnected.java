package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

import net.lax1dude.eaglercraft.ConfigConstants;
import net.lax1dude.eaglercraft.EaglerAdapter;

public class GuiDisconnected extends GuiScreen {
	/** The error message. */
	private String errorMessage;

	/** The details about the error. */
	private String errorDetail;

	private String errorDetail2;
	private String errorDetailTryAgain;
	private boolean kickForDoS;
	
	private Object[] field_74247_c;
	private List field_74245_d;
	private final GuiScreen field_98095_n;

	public GuiDisconnected(GuiScreen par1GuiScreen, String par2Str, String par3Str, Object... par4ArrayOfObj) {
		StringTranslate var5 = StringTranslate.getInstance();
		this.field_98095_n = par1GuiScreen;
		if(par2Str.startsWith("disconnect.ratelimit")) {
			this.errorMessage = var5.translateKey(par2Str + ".title");
			this.errorDetail = var5.translateKey(par2Str + ".description0");
			this.errorDetail2 = var5.translateKey(par2Str + ".description1");
			this.errorDetailTryAgain = var5.translateKey(par2Str + ".tryAgain");
			this.kickForDoS = true;
		}else {
			this.errorMessage = par2Str.equals("disconnect.requiresAuth") ? par2Str : var5.translateKey(par2Str);
			this.errorDetail = par3Str;
			this.errorDetail2 = null;
			this.errorDetailTryAgain = null;
			this.kickForDoS = false;
		}
		this.field_74247_c = par4ArrayOfObj;
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.buttonList.clear();
		
		if(!kickForDoS && !"disconnect.requiresAuth".equals(this.errorMessage)) {
			if (this.field_74247_c != null) {
				this.field_74245_d = this.fontRenderer.listFormattedStringToWidth(var1.translateKeyFormat(this.errorDetail, this.field_74247_c), this.width - 50);
			} else {
				this.field_74245_d = this.fontRenderer.listFormattedStringToWidth(var1.translateKey(this.errorDetail), this.width - 50);
			}
		}
		
		int i = 0;
		if(kickForDoS) {
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.height / 7, var1.translateKey("gui.toMenu")));
		}else if("disconnect.requiresAuth".equals(this.errorMessage)){
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.toMenu")));
		}else {
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - this.height / 5 - 40, var1.translateKey("gui.toMenu")));
		}
			
		
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(this.field_98095_n);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		int var4 = this.height / 2 - 30;
		
		if(kickForDoS) {
			var4 -= 20;
			this.drawCenteredString(this.fontRenderer, this.errorMessage, this.width / 2, var4 - 20, 11184810);
			this.drawCenteredString(this.fontRenderer, this.errorDetail, this.width / 2, var4 + 10, 16777215);
			String s = this.errorDetail2;
			boolean b = s.startsWith("$");
			if(b) {
				s = s.substring(1);
				var4 -= 2;
			}
			this.drawCenteredString(this.fontRenderer, s, this.width / 2, var4 + 24, b ? 16777215 : 0xFF5555);
			this.drawCenteredString(this.fontRenderer, this.errorDetailTryAgain, this.width / 2, var4 + 50, 0x777777);
		}else if("disconnect.requiresAuth".equals(this.errorMessage)) {//22w12a
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glScalef(1.5f, 1.5f, 1.5f);
			this.drawCenteredString(this.fontRenderer, "Authentication Required", this.width / 3, this.height / 4 - 30, 0xDD5555);
			EaglerAdapter.glPopMatrix();
			this.drawCenteredString(this.fontRenderer, "This server requires a paid Minecraft account to join", this.width / 2, this.height / 2 - 55, 0xDDDD44);
			this.drawCenteredString(this.fontRenderer, "You are running Eaglercraft " + EnumChatFormatting.GRAY + ConfigConstants.version + EnumChatFormatting.RESET + " which does not", this.width / 2, this.height / 2 - 35, 0x44BB44);
			this.drawCenteredString(this.fontRenderer, "support Xbox Live sign-in, so this server is inaccessable", this.width / 2, this.height / 2 - 23, 0x44BB44);

			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glScalef(1.0f, 1.0f, 1.0f);
			this.drawCenteredString(this.fontRenderer, "Please contact the owner of this site to update, or", this.width / 2, this.height / 2 - 3, 0x666666);
			this.drawCenteredString(this.fontRenderer, "if you want to continue playing Eaglercraft without a", this.width / 2, this.height / 2 + 5, 0x666666);
			this.drawCenteredString(this.fontRenderer, "real Minecraft account then please choose to play on", this.width / 2, this.height / 2 + 14, 0x666666);
			this.drawCenteredString(this.fontRenderer, "an older server that does still you to play for free", this.width / 2, this.height / 2 + 23, 0x666666);
			EaglerAdapter.glPopMatrix();
			
		}else {
			this.drawCenteredString(this.fontRenderer, this.errorMessage, this.width / 2, this.height / 2 - 50, 11184810);
			if (this.field_74245_d != null) {
				for (Iterator var5 = this.field_74245_d.iterator(); var5.hasNext(); var4 += this.fontRenderer.FONT_HEIGHT) {
					String var6 = (String) var5.next();
					this.drawCenteredString(this.fontRenderer, var6, this.width / 2, var4, 16777215);
				}
			}
		}

		super.drawScreen(par1, par2, par3);
	}
}
