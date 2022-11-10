package net.minecraft.src;

public interface IconRegister {
	
	Icon registerIcon(String var1, int w);
	
	default Icon registerIcon(String var1) {
		return registerIcon(var1, 1);
	}
	
}
