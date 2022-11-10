package net.lax1dude.eaglercraft;

import net.minecraft.src.Icon;

public class DebugIconImpl implements Icon {
	
	private final int sheetW;
	private final int sheetH;
	
	public DebugIconImpl() {
		this(1024, 1024);
	}
	
	public DebugIconImpl(int sheetW, int sheetH) {
		this.sheetW = sheetW;
		this.sheetH = sheetH;
	}

	@Override
	public int getOriginX() {
		return 0;
	}

	@Override
	public int getOriginY() {
		return 0;
	}

	@Override
	public float getMinU() {
		return 0;
	}

	@Override
	public float getMaxU() {
		return 1;
	}

	@Override
	public float getInterpolatedU(double var1) {
		return (float)var1 / 16.0f;
	}

	@Override
	public float getMinV() {
		return 0;
	}

	@Override
	public float getMaxV() {
		return 1;
	}

	@Override
	public float getInterpolatedV(double var1) {
		return (float)var1 / 16.0f;
	}

	@Override
	public String getIconName() {
		return "debug_icon";
	}

	@Override
	public int getSheetWidth() {
		return sheetW;
	}

	@Override
	public int getSheetHeight() {
		return sheetH;
	}


}
