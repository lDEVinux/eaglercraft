package net.lax1dude.eaglercraft.sp;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.ServerConfigurationManager;

public class EAGPlayerList extends ServerConfigurationManager {
	
	private NBTTagCompound hostPlayerNBT = null;

	public EAGPlayerList(MinecraftServer par1MinecraftServer) {
		super(par1MinecraftServer);
		this.viewDistance = 4;
	}

	protected void writePlayerData(EntityPlayerMP par1EntityPlayerMP) {
		if (par1EntityPlayerMP.getCommandSenderName().equals(this.getServerInstance().getServerOwner())) {
			this.hostPlayerNBT = new NBTTagCompound();
			par1EntityPlayerMP.writeToNBT(hostPlayerNBT);
		}
		super.writePlayerData(par1EntityPlayerMP);
	}
	
	public NBTTagCompound getHostPlayerData() {
		return this.hostPlayerNBT;
	}
}
