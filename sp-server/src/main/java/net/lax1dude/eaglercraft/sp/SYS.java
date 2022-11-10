package net.lax1dude.eaglercraft.sp;

import org.teavm.jso.JSBody;

import net.lax1dude.eaglercraft.sp.VirtualFilesystem.VFSHandle;

public class SYS {

	public static final VirtualFilesystem VFS;
	
	@JSBody(params = { }, script = "return eaglercraftServerOpts.worldDatabaseName;")
	private static native String getWorldDatabaseName();
	
	static {
		
		VFSHandle vh = VirtualFilesystem.openVFS("_net_lax1dude_eaglercraft_sp_VirtualFilesystem_1_5_2_" + getWorldDatabaseName());
		
		if(vh.vfs == null) {
			System.err.println("Could not init filesystem!");
			IntegratedServer.throwExceptionToClient("Could not init filesystem!", new RuntimeException("VFSHandle.vfs was null"));
		}
		
		VFS = vh.vfs;
		
	}
	
	
}
