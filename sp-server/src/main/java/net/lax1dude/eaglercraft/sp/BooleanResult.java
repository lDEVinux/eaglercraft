package net.lax1dude.eaglercraft.sp;

public class BooleanResult {
	
	public static final BooleanResult TRUE = new BooleanResult(true);
	public static final BooleanResult FALSE = new BooleanResult(false);
	
	public final boolean bool;
	
	private BooleanResult(boolean b) {
		bool = b;
	}
	
	public static BooleanResult _new(boolean b) {
		return b ? TRUE : FALSE;
	}
	
}