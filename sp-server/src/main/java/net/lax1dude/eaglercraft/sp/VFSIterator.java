package net.lax1dude.eaglercraft.sp;

public interface VFSIterator {
	
	public static class BreakLoop extends RuntimeException {
		public BreakLoop() {
			super("iterator loop break request");
		}
	}
	
	public default void end() {
		throw new BreakLoop();
	}
	
	public void next(VIteratorFile entry);
	
}
