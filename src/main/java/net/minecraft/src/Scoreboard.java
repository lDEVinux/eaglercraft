package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Scoreboard {
	/** Map of objective names to ScoreObjective objects. */
	private final Map scoreObjectives = new HashMap();
	private final Map field_96543_b = new HashMap();
	private final Map field_96544_c = new HashMap();
	private final ScoreObjective[] field_96541_d = new ScoreObjective[3];
	private final Map field_96542_e = new HashMap();

	/** Map of usernames to ScorePlayerTeam objects. */
	private final Map teamMemberships = new HashMap();

	/**
	 * Returns a ScoreObjective for the objective name
	 */
	public ScoreObjective getObjective(String par1Str) {
		return (ScoreObjective) this.scoreObjectives.get(par1Str);
	}

	public ScoreObjective func_96535_a(String par1Str, ScoreObjectiveCriteria par2ScoreObjectiveCriteria) {
		ScoreObjective var3 = this.getObjective(par1Str);

		if (var3 != null) {
			throw new IllegalArgumentException("An objective with the name \'" + par1Str + "\' already exists!");
		} else {
			var3 = new ScoreObjective(this, par1Str, par2ScoreObjectiveCriteria);
			Object var4 = (List) this.field_96543_b.get(par2ScoreObjectiveCriteria);

			if (var4 == null) {
				var4 = new ArrayList();
				this.field_96543_b.put(par2ScoreObjectiveCriteria, var4);
			}

			((List) var4).add(var3);
			this.scoreObjectives.put(par1Str, var3);
			this.func_96522_a(var3);
			return var3;
		}
	}

	public Collection func_96520_a(ScoreObjectiveCriteria par1ScoreObjectiveCriteria) {
		Collection var2 = (Collection) this.field_96543_b.get(par1ScoreObjectiveCriteria);
		return var2 == null ? new ArrayList() : new ArrayList(var2);
	}

	public Score func_96529_a(String par1Str, ScoreObjective par2ScoreObjective) {
		Object var3 = (Map) this.field_96544_c.get(par1Str);

		if (var3 == null) {
			var3 = new HashMap();
			this.field_96544_c.put(par1Str, var3);
		}

		Score var4 = (Score) ((Map) var3).get(par2ScoreObjective);

		if (var4 == null) {
			var4 = new Score(this, par2ScoreObjective, par1Str);
			((Map) var3).put(par2ScoreObjective, var4);
		}

		return var4;
	}

	public Collection func_96534_i(ScoreObjective par1ScoreObjective) {
		ArrayList var2 = new ArrayList();
		Iterator var3 = this.field_96544_c.values().iterator();

		while (var3.hasNext()) {
			Map var4 = (Map) var3.next();
			Score var5 = (Score) var4.get(par1ScoreObjective);

			if (var5 != null) {
				var2.add(var5);
			}
		}

		Collections.sort(var2, Score.field_96658_a);
		return var2;
	}

	public Collection getScoreObjectives() {
		return this.scoreObjectives.values();
	}

	public Collection getObjectiveNames() {
		return this.field_96544_c.keySet();
	}

	public void func_96515_c(String par1Str) {
		Map var2 = (Map) this.field_96544_c.remove(par1Str);

		if (var2 != null) {
			this.func_96516_a(par1Str);
		}
	}

	public Collection func_96528_e() {
		Collection var1 = this.field_96544_c.values();
		ArrayList var2 = new ArrayList();

		if (var1 != null) {
			Iterator var3 = var1.iterator();

			while (var3.hasNext()) {
				Map var4 = (Map) var3.next();
				var2.addAll(var4.values());
			}
		}

		return var2;
	}

	public Map func_96510_d(String par1Str) {
		Object var2 = (Map) this.field_96544_c.get(par1Str);

		if (var2 == null) {
			var2 = new HashMap();
		}

		return (Map) var2;
	}

	public void func_96519_k(ScoreObjective par1ScoreObjective) {
		this.scoreObjectives.remove(par1ScoreObjective.getName());

		for (int var2 = 0; var2 < 3; ++var2) {
			if (this.func_96539_a(var2) == par1ScoreObjective) {
				this.func_96530_a(var2, (ScoreObjective) null);
			}
		}

		List var5 = (List) this.field_96543_b.get(par1ScoreObjective.getCriteria());

		if (var5 != null) {
			var5.remove(par1ScoreObjective);
		}

		Iterator var3 = this.field_96544_c.values().iterator();

		while (var3.hasNext()) {
			Map var4 = (Map) var3.next();
			var4.remove(par1ScoreObjective);
		}

		this.func_96533_c(par1ScoreObjective);
	}

	public void func_96530_a(int par1, ScoreObjective par2ScoreObjective) {
		this.field_96541_d[par1] = par2ScoreObjective;
	}

	public ScoreObjective func_96539_a(int par1) {
		return this.field_96541_d[par1];
	}

	public ScorePlayerTeam func_96508_e(String par1Str) {
		return (ScorePlayerTeam) this.field_96542_e.get(par1Str);
	}

	public ScorePlayerTeam func_96527_f(String par1Str) {
		ScorePlayerTeam var2 = this.func_96508_e(par1Str);

		if (var2 != null) {
			throw new IllegalArgumentException("An objective with the name \'" + par1Str + "\' already exists!");
		} else {
			var2 = new ScorePlayerTeam(this, par1Str);
			this.field_96542_e.put(par1Str, var2);
			this.func_96523_a(var2);
			return var2;
		}
	}

	public void func_96511_d(ScorePlayerTeam par1ScorePlayerTeam) {
		this.field_96542_e.remove(par1ScorePlayerTeam.func_96661_b());
		Iterator var2 = par1ScorePlayerTeam.getMembershipCollection().iterator();

		while (var2.hasNext()) {
			String var3 = (String) var2.next();
			this.teamMemberships.remove(var3);
		}

		this.func_96513_c(par1ScorePlayerTeam);
	}

	public void func_96521_a(String par1Str, ScorePlayerTeam par2ScorePlayerTeam) {
		if (this.getPlayersTeam(par1Str) != null) {
			this.func_96524_g(par1Str);
		}

		this.teamMemberships.put(par1Str, par2ScorePlayerTeam);
		par2ScorePlayerTeam.getMembershipCollection().add(par1Str);
	}

	public boolean func_96524_g(String par1Str) {
		ScorePlayerTeam var2 = this.getPlayersTeam(par1Str);

		if (var2 != null) {
			this.removePlayerFromTeam(par1Str, var2);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes the given username from the given ScorePlayerTeam. If the player is
	 * not on the team then an IllegalStateException is thrown.
	 */
	public void removePlayerFromTeam(String par1Str, ScorePlayerTeam par2ScorePlayerTeam) {
		if (this.getPlayersTeam(par1Str) != par2ScorePlayerTeam) {
			throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team \'" + par2ScorePlayerTeam.func_96661_b() + "\'.");
		} else {
			this.teamMemberships.remove(par1Str);
			par2ScorePlayerTeam.getMembershipCollection().remove(par1Str);
		}
	}

	public Collection func_96531_f() {
		return this.field_96542_e.keySet();
	}

	public Collection func_96525_g() {
		return this.field_96542_e.values();
	}

	/**
	 * Gets the ScorePlayerTeam object for the given username.
	 */
	public ScorePlayerTeam getPlayersTeam(String par1Str) {
		return (ScorePlayerTeam) this.teamMemberships.get(par1Str);
	}

	public void func_96522_a(ScoreObjective par1ScoreObjective) {
	}

	public void func_96532_b(ScoreObjective par1ScoreObjective) {
	}

	public void func_96533_c(ScoreObjective par1ScoreObjective) {
	}

	public void func_96536_a(Score par1Score) {
	}

	public void func_96516_a(String par1Str) {
	}

	public void func_96523_a(ScorePlayerTeam par1ScorePlayerTeam) {
	}

	public void func_96538_b(ScorePlayerTeam par1ScorePlayerTeam) {
	}

	public void func_96513_c(ScorePlayerTeam par1ScorePlayerTeam) {
	}

	/**
	 * Returns 'list' for 0, 'sidebar' for 1, 'belowName for 2, otherwise null.
	 */
	public static String getObjectiveDisplaySlot(int par0) {
		switch (par0) {
		case 0:
			return "list";

		case 1:
			return "sidebar";

		case 2:
			return "belowName";

		default:
			return null;
		}
	}

	/**
	 * Returns 0 for (case-insensitive) 'list', 1 for 'sidebar', 2 for 'belowName',
	 * otherwise -1.
	 */
	public static int getObjectiveDisplaySlotNumber(String par0Str) {
		return par0Str.equalsIgnoreCase("list") ? 0 : (par0Str.equalsIgnoreCase("sidebar") ? 1 : (par0Str.equalsIgnoreCase("belowName") ? 2 : -1));
	}
}
