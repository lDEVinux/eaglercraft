package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.minecraft.client.Minecraft;

public class MouseHelper {
	private final GameSettings field_85184_d;

	/** Mouse delta X this frame */
	public int deltaX;

	/** Mouse delta Y this frame */
	public int deltaY;

	public MouseHelper(GameSettings par2GameSettings) {
		this.field_85184_d = par2GameSettings;
	}

	/**
	 * Grabs the mouse cursor it doesn't move and isn't seen.
	 */
	public void grabMouseCursor() {
		int var1 = Minecraft.getMinecraft().displayWidth;
		int var2 = Minecraft.getMinecraft().displayHeight;
		
		EaglerAdapter.mouseSetCursorPosition(var1 / 2, var2 / 2);
		EaglerAdapter.mouseSetGrabbed(true);
		this.deltaX = 0;
		this.deltaY = 0;
	}

	/**
	 * Ungrabs the mouse cursor so it can be moved and set it to the center of the
	 * screen
	 */
	public void ungrabMouseCursor() {
		//int var1 = Minecraft.getMinecraft().displayWidth;
		//int var2 = Minecraft.getMinecraft().displayHeight;
		//EaglerAdapter.mouseSetCursorPosition(var1 / 2, var2 / 2);
		EaglerAdapter.mouseSetGrabbed(false);
	}

	public void mouseXYChange() {
		this.deltaX = EaglerAdapter.mouseGetDX();
		this.deltaY = EaglerAdapter.mouseGetDY();
	}
}
