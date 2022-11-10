package net.lax1dude.eaglercraft;

import net.lax1dude.eaglercraft.glemu.HighPolyMesh;

public class ModelLocation {
	
	public final String path;
	private boolean loadAttempted = false;
	private HighPolyMesh mesh;
	
	public ModelLocation(String path) {
		this.path = path;
	}
	
	public HighPolyMesh getModel() {
		if(!loadAttempted) {
			mesh = EaglerAdapter.loadMesh(path);
			loadAttempted = true;
		}
		return mesh;
	}
	
}
