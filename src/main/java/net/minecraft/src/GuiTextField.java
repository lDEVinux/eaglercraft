package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class GuiTextField extends Gui {
	/**
	 * Have the font renderer from GuiScreen to render the textbox text into the
	 * screen.
	 */
	private final FontRenderer fontRenderer;
	private final int xPos;
	private final int yPos;

	/** The width of this text field. */
	private final int width;
	private final int height;

	/** Have the current text beign edited on the textbox. */
	private String text = "";
	private int maxStringLength = 32;
	private int cursorCounter;
	private boolean enableBackgroundDrawing = true;

	/**
	 * if true the textbox can lose focus by clicking elsewhere on the screen
	 */
	private boolean canLoseFocus = true;

	/**
	 * If this value is true along isEnabled, keyTyped will process the keys.
	 */
	private boolean isFocused = false;

	/**
	 * If this value is true along isFocused, keyTyped will process the keys.
	 */
	private boolean isEnabled = true;

	/**
	 * The current character index that should be used as start of the rendered
	 * text.
	 */
	private int lineScrollOffset = 0;
	private int cursorPosition = 0;

	/** other selection position, maybe the same as the cursor */
	private int selectionEnd = 0;
	private int enabledColor = 14737632;
	private int disabledColor = 7368816;

	/** True if this textbox is visible */
	private boolean visible = true;

	public GuiTextField(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5) {
		this.fontRenderer = par1FontRenderer;
		this.xPos = par2;
		this.yPos = par3;
		this.width = par4;
		this.height = par5;
	}

	/**
	 * Increments the cursor counter
	 */
	public void updateCursorCounter() {
		++this.cursorCounter;
	}

	/**
	 * Sets the text of the textbox.
	 */
	public void setText(String par1Str) {
		if(text.equals(par1Str)) {
			return;
		}
		if (par1Str.length() > this.maxStringLength) {
			this.text = par1Str.substring(0, this.maxStringLength);
		} else {
			this.text = par1Str;
		}

		this.setCursorPositionEnd();
	}

	/**
	 * Returns the text beign edited on the textbox.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @return returns the text between the cursor and selectionEnd
	 */
	public String getSelectedtext() {
		int var1 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int var2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		return this.text.substring(var1, var2);
	}

	/**
	 * replaces selected text, or inserts text at the position on the cursor
	 */
	public void writeText(String par1Str) {
		String var2 = "";
		String var3 = ChatAllowedCharacters.filerAllowedCharacters(par1Str);
		int var4 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int var5 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		int var6 = this.maxStringLength - this.text.length() - (var4 - this.selectionEnd);
		boolean var7 = false;

		if (this.text.length() > 0) {
			var2 = var2 + this.text.substring(0, var4);
		}

		int var8;

		if (var6 < var3.length()) {
			var2 = var2 + var3.substring(0, var6);
			var8 = var6;
		} else {
			var2 = var2 + var3;
			var8 = var3.length();
		}

		if (this.text.length() > 0 && var5 < this.text.length()) {
			var2 = var2 + this.text.substring(var5);
		}

		this.text = var2;
		this.moveCursorBy(var4 - this.selectionEnd + var8);
	}

	/**
	 * Deletes the specified number of words starting at the cursor position.
	 * Negative numbers will delete words left of the cursor.
	 */
	public void deleteWords(int par1) {
		if (this.text.length() != 0) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				this.deleteFromCursor(this.getNthWordFromCursor(par1) - this.cursorPosition);
			}
		}
	}

	/**
	 * delete the selected text, otherwsie deletes characters from either side of
	 * the cursor. params: delete num
	 */
	public void deleteFromCursor(int par1) {
		if (this.text.length() != 0) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				boolean var2 = par1 < 0;
				int var3 = var2 ? this.cursorPosition + par1 : this.cursorPosition;
				int var4 = var2 ? this.cursorPosition : this.cursorPosition + par1;
				String var5 = "";

				if (var3 >= 0) {
					var5 = this.text.substring(0, var3);
				}

				if (var4 < this.text.length()) {
					var5 = var5 + this.text.substring(var4);
				}

				this.text = var5;

				if (var2) {
					this.moveCursorBy(par1);
				}
			}
		}
	}

	/**
	 * see @getNthNextWordFromPos() params: N, position
	 */
	public int getNthWordFromCursor(int par1) {
		return this.getNthWordFromPos(par1, this.getCursorPosition());
	}

	/**
	 * gets the position of the nth word. N may be negative, then it looks
	 * backwards. params: N, position
	 */
	public int getNthWordFromPos(int par1, int par2) {
		return this.func_73798_a(par1, this.getCursorPosition(), true);
	}

	public int func_73798_a(int par1, int par2, boolean par3) {
		int var4 = par2;
		boolean var5 = par1 < 0;
		int var6 = Math.abs(par1);

		for (int var7 = 0; var7 < var6; ++var7) {
			if (var5) {
				while (par3 && var4 > 0 && this.text.charAt(var4 - 1) == 32) {
					--var4;
				}

				while (var4 > 0 && this.text.charAt(var4 - 1) != 32) {
					--var4;
				}
			} else {
				int var8 = this.text.length();
				var4 = this.text.indexOf(32, var4);

				if (var4 == -1) {
					var4 = var8;
				} else {
					while (par3 && var4 < var8 && this.text.charAt(var4) == 32) {
						++var4;
					}
				}
			}
		}

		return var4;
	}

	/**
	 * Moves the text cursor by a specified number of characters and clears the
	 * selection
	 */
	public void moveCursorBy(int par1) {
		this.setCursorPosition(this.selectionEnd + par1);
	}

	/**
	 * sets the position of the cursor to the provided index
	 */
	public void setCursorPosition(int par1) {
		this.cursorPosition = par1;
		int var2 = this.text.length();

		if (this.cursorPosition < 0) {
			this.cursorPosition = 0;
		}

		if (this.cursorPosition > var2) {
			this.cursorPosition = var2;
		}

		this.setSelectionPos(this.cursorPosition);
	}

	/**
	 * sets the cursors position to the beginning
	 */
	public void setCursorPositionZero() {
		this.setCursorPosition(0);
	}

	/**
	 * sets the cursors position to after the text
	 */
	public void setCursorPositionEnd() {
		this.setCursorPosition(this.text.length());
	}

	/**
	 * Call this method from you GuiScreen to process the keys into textbox.
	 */
	public boolean textboxKeyTyped(char par1, int par2) {
		if (this.isEnabled && this.isFocused) {
			switch (par1) {
			case 1:
				this.setCursorPositionEnd();
				this.setSelectionPos(0);
				return true;
				
			case 3:
				String s = this.getSelectedtext();
				if(s != null && s.length() > 0) {
					GuiScreen.setClipboardString(s);
				}
				return true;

			case 22:
				String s3 = GuiScreen.getClipboardString();
				if(s3 != null && s3.length() > 0) {
					this.writeText(s3);
				}
				return true;

			case 24:
				String s2 = this.getSelectedtext();
				if(s2 != null && s2.length() > 0) {
					GuiScreen.setClipboardString(s2);
				}
				this.writeText("");
				return true;
				
			default:
				switch (par2) {
				case 14:
					if (GuiScreen.isCtrlKeyDown()) {
						this.deleteWords(-1);
					} else {
						this.deleteFromCursor(-1);
					}

					return true;

				case 200:
					if (GuiScreen.isShiftKeyDown()) {
						this.setSelectionPos(0);
					} else {
						this.setCursorPositionZero();
					}

					return true;

				case 203:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
						} else {
							this.setSelectionPos(this.getSelectionEnd() - 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						this.setCursorPosition(this.getNthWordFromCursor(-1));
					} else {
						this.moveCursorBy(-1);
					}

					return true;

				case 205:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
						} else {
							this.setSelectionPos(this.getSelectionEnd() + 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						this.setCursorPosition(this.getNthWordFromCursor(1));
					} else {
						this.moveCursorBy(1);
					}

					return true;

				case 208:
					if (GuiScreen.isShiftKeyDown()) {
						this.setSelectionPos(this.text.length());
					} else {
						this.setCursorPositionEnd();
					}

					return true;

				case 211:
					if (GuiScreen.isCtrlKeyDown()) {
						this.deleteWords(1);
					} else {
						this.deleteFromCursor(1);
					}

					return true;

				default:
					boolean ctrl = GuiScreen.isCtrlKeyDown();
					if(ctrl && (par1 == 'c' || par1 == 'C')) {
						String s5 = this.getSelectedtext();
						if(s5.length() > 0) {
							GuiScreen.setClipboardString(s5);
						}
						return true;
					}else if(ctrl && (par1 == 'x' || par1 == 'X')) {
						String s6 = this.getSelectedtext();
						if(s6.length() > 0) {
							GuiScreen.setClipboardString(s6);
							this.writeText("");
						}
						return true;
					}else if(ctrl && (par1 == 'v' || par1 == 'V')) {
						String s4 = GuiScreen.getClipboardString();
						if(s4 != null && s4.length() > 0) {
							this.writeText(s4);
						}
						return true;
					}else if (ChatAllowedCharacters.isAllowedCharacter(par1)) {
						this.writeText(Character.toString(par1));
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * Args: x, y, buttonClicked
	 */
	public void mouseClicked(int par1, int par2, int par3) {
		boolean var4 = par1 >= this.xPos && par1 < this.xPos + this.width && par2 >= this.yPos && par2 < this.yPos + this.height;

		if (this.canLoseFocus) {
			this.setFocused(this.isEnabled && var4);
		}

		if (this.isFocused && par3 == 0) {
			int var5 = par1 - this.xPos;

			if (this.enableBackgroundDrawing) {
				var5 -= 4;
			}

			String var6 = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
			this.setCursorPosition(this.fontRenderer.trimStringToWidth(var6, var5).length() + this.lineScrollOffset);
		}
	}

	/**
	 * Draws the textbox
	 */
	public void drawTextBox() {
		if (this.getVisible()) {
			if (this.getEnableBackgroundDrawing()) {
				drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
				drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
			}

			int var1 = this.isEnabled ? this.enabledColor : this.disabledColor;
			int var2 = this.cursorPosition - this.lineScrollOffset;
			int var3 = this.selectionEnd - this.lineScrollOffset;
			String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
			boolean var5 = var2 >= 0 && var2 <= var4.length();
			boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
			int var7 = this.enableBackgroundDrawing ? this.xPos + 4 : this.xPos;
			int var8 = this.enableBackgroundDrawing ? this.yPos + (this.height - 8) / 2 : this.yPos;
			int var9 = var7;

			if (var3 > var4.length()) {
				var3 = var4.length();
			}

			if (var4.length() > 0) {
				String var10 = var5 ? var4.substring(0, var2) : var4;
				var9 = this.fontRenderer.drawStringWithShadow(var10, var7, var8, var1);
			}

			boolean var13 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
			int var11 = var9;

			if (!var5) {
				var11 = var2 > 0 ? var7 + this.width : var7;
			} else if (var13) {
				var11 = var9 - 1;
				--var9;
			}

			if (var4.length() > 0 && var5 && var2 < var4.length()) {
				this.fontRenderer.drawStringWithShadow(var4.substring(var2), var9, var8, var1);
			}

			if (var6) {
				if (var13) {
					Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT, var1 | 0xFF000000);
				} else {
					this.fontRenderer.drawStringWithShadow("_", var11, var8, var1);
				}
			}

			if (var3 != var2) {
				int var12 = var7 + this.fontRenderer.getStringWidth(var4.substring(0, var3));
				this.drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT);
			}
		}
	}

	/**
	 * draws the vertical line cursor in the textbox
	 */
	private void drawCursorVertical(int par1, int par2, int par3, int par4) {
		int var5;

		if (par1 < par3) {
			var5 = par1;
			par1 = par3;
			par3 = var5;
		}

		if (par2 < par4) {
			var5 = par2;
			par2 = par4;
			par4 = var5;
		}

		Tessellator var6 = Tessellator.instance;
		EaglerAdapter.glColor4f(0.2F, 0.2F, 1.0F, 1.0F);
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE_MINUS_DST_COLOR, EaglerAdapter.GL_SRC_ALPHA);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
		var6.startDrawingQuads();
		var6.addVertex((double) par1, (double) par4, 0.0D);
		var6.addVertex((double) par3, (double) par4, 0.0D);
		var6.addVertex((double) par3, (double) par2, 0.0D);
		var6.addVertex((double) par1, (double) par2, 0.0D);
		var6.draw();
		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
	}

	public void setMaxStringLength(int par1) {
		this.maxStringLength = par1;

		if (this.text.length() > par1) {
			this.text = this.text.substring(0, par1);
		}
	}

	/**
	 * returns the maximum number of character that can be contained in this textbox
	 */
	public int getMaxStringLength() {
		return this.maxStringLength;
	}

	/**
	 * returns the current position of the cursor
	 */
	public int getCursorPosition() {
		return this.cursorPosition;
	}

	/**
	 * get enable drawing background and outline
	 */
	public boolean getEnableBackgroundDrawing() {
		return this.enableBackgroundDrawing;
	}

	/**
	 * enable drawing background and outline
	 */
	public void setEnableBackgroundDrawing(boolean par1) {
		this.enableBackgroundDrawing = par1;
	}

	/**
	 * Sets the text colour for this textbox (disabled text will not use this
	 * colour)
	 */
	public void setTextColor(int par1) {
		this.enabledColor = par1;
	}

	public void setDisabledTextColour(int par1) {
		this.disabledColor = par1;
	}

	/**
	 * setter for the focused field
	 */
	public void setFocused(boolean par1) {
		if (par1 && !this.isFocused) {
			this.cursorCounter = 0;
		}

		this.isFocused = par1;
	}

	/**
	 * getter for the focused field
	 */
	public boolean isFocused() {
		return this.isFocused;
	}

	public void setEnabled(boolean par1) {
		this.isEnabled = par1;
	}

	/**
	 * the side of the selection that is not the cursor, maye be the same as the
	 * cursor
	 */
	public int getSelectionEnd() {
		return this.selectionEnd;
	}

	/**
	 * returns the width of the textbox depending on if the the box is enabled
	 */
	public int getWidth() {
		return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
	}

	/**
	 * Sets the position of the selection anchor (i.e. position the selection was
	 * started at)
	 */
	public void setSelectionPos(int par1) {
		int var2 = this.text.length();

		if (par1 > var2) {
			par1 = var2;
		}

		if (par1 < 0) {
			par1 = 0;
		}
		
		this.selectionEnd = par1;

		if (this.fontRenderer != null) {
			if (this.lineScrollOffset > var2) {
				this.lineScrollOffset = var2;
			}

			int var3 = this.getWidth();
			String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), var3);
			int var5 = var4.length() + this.lineScrollOffset;

			if (par1 == this.lineScrollOffset) {
				this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text, var3, true).length();
			}

			if (par1 > var5) {
				this.lineScrollOffset += par1 - var5;
			} else if (par1 <= this.lineScrollOffset) {
				this.lineScrollOffset -= this.lineScrollOffset - par1;
			}

			if (this.lineScrollOffset < 0) {
				this.lineScrollOffset = 0;
			}

			if (this.lineScrollOffset > var2) {
				this.lineScrollOffset = var2;
			}
		}
	}

	/**
	 * if true the textbox can lose focus by clicking elsewhere on the screen
	 */
	public void setCanLoseFocus(boolean par1) {
		this.canLoseFocus = par1;
	}

	/**
	 * @return {@code true} if this textbox is visible
	 */
	public boolean getVisible() {
		return this.visible;
	}

	/**
	 * Sets whether or not this textbox is visible
	 */
	public void setVisible(boolean par1) {
		this.visible = par1;
	}
}
