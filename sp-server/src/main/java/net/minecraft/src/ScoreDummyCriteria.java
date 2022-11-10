package net.minecraft.src;

import java.util.List;

public class ScoreDummyCriteria implements ScoreObjectiveCriteria {
	private final String field_96644_g;

	public ScoreDummyCriteria(String par1Str) {
		this.field_96644_g = par1Str;
		ScoreObjectiveCriteria.field_96643_a.put(par1Str, this);
	}

	public String func_96636_a() {
		return this.field_96644_g;
	}

	public int func_96635_a(List par1List) {
		return 0;
	}

	public boolean isReadOnly() {
		return false;
	}
}
