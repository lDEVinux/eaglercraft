package net.minecraft.src;

import java.util.Vector;

import javax.swing.JList;

import net.minecraft.server.MinecraftServer;

public class PlayerListBox extends JList implements IUpdatePlayerListBox {
	/** Reference to the MinecraftServer object. */
	private MinecraftServer mcServer;

	/** Counts the number of updates. */
	private int updateCounter = 0;

	public PlayerListBox(MinecraftServer par1MinecraftServer) {
		this.mcServer = par1MinecraftServer;
		par1MinecraftServer.func_82010_a(this);
	}

	/**
	 * Updates the JList with a new model.
	 */
	public void update() {
		if (this.updateCounter++ % 20 == 0) {
			Vector var1 = new Vector();

			for (int var2 = 0; var2 < this.mcServer.getConfigurationManager().playerEntityList.size(); ++var2) {
				var1.add(
						((EntityPlayerMP) this.mcServer.getConfigurationManager().playerEntityList.get(var2)).username);
			}

			this.setListData(var1);
		}
	}
}
