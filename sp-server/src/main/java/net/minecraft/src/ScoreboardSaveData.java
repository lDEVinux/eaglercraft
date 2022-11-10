package net.minecraft.src;

import java.util.Collection;
import java.util.Iterator;

import net.minecraft.server.MinecraftServer;

public class ScoreboardSaveData extends WorldSavedData {
	private Scoreboard field_96507_a;
	private NBTTagCompound field_96506_b;

	public ScoreboardSaveData() {
		this("scoreboard");
	}

	public ScoreboardSaveData(String par1Str) {
		super(par1Str);
	}

	public void func_96499_a(Scoreboard par1Scoreboard) {
		this.field_96507_a = par1Scoreboard;

		if (this.field_96506_b != null) {
			this.readFromNBT(this.field_96506_b);
		}
	}

	/**
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		if (this.field_96507_a == null) {
			this.field_96506_b = par1NBTTagCompound;
		} else {
			this.func_96501_b(par1NBTTagCompound.getTagList("Objectives"));
			this.func_96500_c(par1NBTTagCompound.getTagList("PlayerScores"));

			if (par1NBTTagCompound.hasKey("DisplaySlots")) {
				this.func_96504_c(par1NBTTagCompound.getCompoundTag("DisplaySlots"));
			}

			if (par1NBTTagCompound.hasKey("Teams")) {
				this.func_96498_a(par1NBTTagCompound.getTagList("Teams"));
			}
		}
	}

	protected void func_96498_a(NBTTagList par1NBTTagList) {
		for (int var2 = 0; var2 < par1NBTTagList.tagCount(); ++var2) {
			NBTTagCompound var3 = (NBTTagCompound) par1NBTTagList.tagAt(var2);
			ScorePlayerTeam var4 = this.field_96507_a.func_96527_f(var3.getString("Name"));
			var4.func_96664_a(var3.getString("DisplayName"));
			var4.func_96666_b(var3.getString("Prefix"));
			var4.func_96662_c(var3.getString("Suffix"));

			if (var3.hasKey("AllowFriendlyFire")) {
				var4.func_96660_a(var3.getBoolean("AllowFriendlyFire"));
			}

			if (var3.hasKey("SeeFriendlyInvisibles")) {
				var4.func_98300_b(var3.getBoolean("SeeFriendlyInvisibles"));
			}

			this.func_96502_a(var4, var3.getTagList("Players"));
		}
	}

	protected void func_96502_a(ScorePlayerTeam par1ScorePlayerTeam, NBTTagList par2NBTTagList) {
		for (int var3 = 0; var3 < par2NBTTagList.tagCount(); ++var3) {
			this.field_96507_a.func_96521_a(((NBTTagString) par2NBTTagList.tagAt(var3)).data, par1ScorePlayerTeam);
		}
	}

	protected void func_96504_c(NBTTagCompound par1NBTTagCompound) {
		for (int var2 = 0; var2 < 3; ++var2) {
			if (par1NBTTagCompound.hasKey("slot_" + var2)) {
				String var3 = par1NBTTagCompound.getString("slot_" + var2);
				ScoreObjective var4 = this.field_96507_a.getObjective(var3);
				this.field_96507_a.func_96530_a(var2, var4);
			}
		}
	}

	protected void func_96501_b(NBTTagList par1NBTTagList) {
		for (int var2 = 0; var2 < par1NBTTagList.tagCount(); ++var2) {
			NBTTagCompound var3 = (NBTTagCompound) par1NBTTagList.tagAt(var2);
			ScoreObjectiveCriteria var4 = (ScoreObjectiveCriteria) ScoreObjectiveCriteria.field_96643_a
					.get(var3.getString("CriteriaName"));
			ScoreObjective var5 = this.field_96507_a.func_96535_a(var3.getString("Name"), var4);
			var5.setDisplayName(var3.getString("DisplayName"));
		}
	}

	protected void func_96500_c(NBTTagList par1NBTTagList) {
		for (int var2 = 0; var2 < par1NBTTagList.tagCount(); ++var2) {
			NBTTagCompound var3 = (NBTTagCompound) par1NBTTagList.tagAt(var2);
			ScoreObjective var4 = this.field_96507_a.getObjective(var3.getString("Objective"));
			Score var5 = this.field_96507_a.func_96529_a(var3.getString("Name"), var4);
			var5.func_96647_c(var3.getInteger("Score"));
		}
	}

	/**
	 * write data to NBTTagCompound from this MapDataBase, similar to Entities and
	 * TileEntities
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		if (this.field_96507_a == null) {
			MinecraftServer.getServer().getLogAgent()
					.func_98236_b("Tried to save scoreboard without having a scoreboard...");
		} else {
			par1NBTTagCompound.setTag("Objectives", this.func_96505_b());
			par1NBTTagCompound.setTag("PlayerScores", this.func_96503_e());
			par1NBTTagCompound.setTag("Teams", this.func_96496_a());
			this.func_96497_d(par1NBTTagCompound);
		}
	}

	protected NBTTagList func_96496_a() {
		NBTTagList var1 = new NBTTagList();
		Collection var2 = this.field_96507_a.func_96525_g();
		Iterator var3 = var2.iterator();

		while (var3.hasNext()) {
			ScorePlayerTeam var4 = (ScorePlayerTeam) var3.next();
			NBTTagCompound var5 = new NBTTagCompound();
			var5.setString("Name", var4.func_96661_b());
			var5.setString("DisplayName", var4.func_96669_c());
			var5.setString("Prefix", var4.func_96668_e());
			var5.setString("Suffix", var4.func_96663_f());
			var5.setBoolean("AllowFriendlyFire", var4.func_96665_g());
			var5.setBoolean("SeeFriendlyInvisibles", var4.func_98297_h());
			NBTTagList var6 = new NBTTagList();
			Iterator var7 = var4.getMembershipCollection().iterator();

			while (var7.hasNext()) {
				String var8 = (String) var7.next();
				var6.appendTag(new NBTTagString("", var8));
			}

			var5.setTag("Players", var6);
			var1.appendTag(var5);
		}

		return var1;
	}

	protected void func_96497_d(NBTTagCompound par1NBTTagCompound) {
		NBTTagCompound var2 = new NBTTagCompound();
		boolean var3 = false;

		for (int var4 = 0; var4 < 3; ++var4) {
			ScoreObjective var5 = this.field_96507_a.func_96539_a(var4);

			if (var5 != null) {
				var2.setString("slot_" + var4, var5.getName());
				var3 = true;
			}
		}

		if (var3) {
			par1NBTTagCompound.setCompoundTag("DisplaySlots", var2);
		}
	}

	protected NBTTagList func_96505_b() {
		NBTTagList var1 = new NBTTagList();
		Collection var2 = this.field_96507_a.getScoreObjectives();
		Iterator var3 = var2.iterator();

		while (var3.hasNext()) {
			ScoreObjective var4 = (ScoreObjective) var3.next();
			NBTTagCompound var5 = new NBTTagCompound();
			var5.setString("Name", var4.getName());
			var5.setString("CriteriaName", var4.getCriteria().func_96636_a());
			var5.setString("DisplayName", var4.getDisplayName());
			var1.appendTag(var5);
		}

		return var1;
	}

	protected NBTTagList func_96503_e() {
		NBTTagList var1 = new NBTTagList();
		Collection var2 = this.field_96507_a.func_96528_e();
		Iterator var3 = var2.iterator();

		while (var3.hasNext()) {
			Score var4 = (Score) var3.next();
			NBTTagCompound var5 = new NBTTagCompound();
			var5.setString("Name", var4.func_96653_e());
			var5.setString("Objective", var4.func_96645_d().getName());
			var5.setInteger("Score", var4.func_96652_c());
			var1.appendTag(var5);
		}

		return var1;
	}
}
