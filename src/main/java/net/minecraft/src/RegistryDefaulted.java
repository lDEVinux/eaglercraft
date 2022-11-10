package net.minecraft.src;

public class RegistryDefaulted extends RegistrySimple {
	/**
	 * Default object for this registry, returned when an object is not found.
	 */
	private final Object defaultObject;

	public RegistryDefaulted(Object par1Obj) {
		this.defaultObject = par1Obj;
	}

	public Object func_82594_a(Object par1Obj) {
		Object var2 = super.func_82594_a(par1Obj);
		return var2 == null ? this.defaultObject : var2;
	}
}
