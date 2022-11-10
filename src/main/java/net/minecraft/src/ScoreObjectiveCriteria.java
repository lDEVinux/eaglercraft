package net.minecraft.src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ScoreObjectiveCriteria {
	Map field_96643_a = new HashMap();
	ScoreObjectiveCriteria field_96641_b = new ScoreDummyCriteria("dummy");
	ScoreObjectiveCriteria field_96642_c = new ScoreDummyCriteria("deathCount");
	ScoreObjectiveCriteria field_96639_d = new ScoreDummyCriteria("playerKillCount");
	ScoreObjectiveCriteria field_96640_e = new ScoreDummyCriteria("totalKillCount");
	ScoreObjectiveCriteria field_96638_f = new ScoreHealthCriteria("health");

	String func_96636_a();

	int func_96635_a(List var1);

	boolean isReadOnly();
}
