package net.minecraft.src;

import java.util.Comparator;

final class ScoreComparator implements Comparator {
	public int func_96659_a(Score par1Score, Score par2Score) {
		return par1Score.func_96652_c() > par2Score.func_96652_c() ? 1 : (par1Score.func_96652_c() < par2Score.func_96652_c() ? -1 : 0);
	}

	public int compare(Object par1Obj, Object par2Obj) {
		return this.func_96659_a((Score) par1Obj, (Score) par2Obj);
	}
}
