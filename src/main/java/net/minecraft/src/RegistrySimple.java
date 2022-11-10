package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class RegistrySimple implements IRegistry {
	/** Objects registered on this registry. */
	protected final Map registryObjects = new HashMap();

	public Object func_82594_a(Object par1Obj) {
		return this.registryObjects.get(par1Obj);
	}

	/**
	 * Register an object on this registry.
	 */
	public void putObject(Object par1Obj, Object par2Obj) {
		this.registryObjects.put(par1Obj, par2Obj);
	}
}
