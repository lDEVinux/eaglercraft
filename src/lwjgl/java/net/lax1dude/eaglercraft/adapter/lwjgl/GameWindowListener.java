package net.lax1dude.eaglercraft.adapter.lwjgl;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.minecraft.client.Minecraft;

public final class GameWindowListener extends WindowAdapter {
	public void windowClosing(WindowEvent par1WindowEvent) {
		Minecraft.getMinecraft().shutdown();
	}
}
