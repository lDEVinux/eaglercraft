// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.score;

import java.beans.ConstructorProperties;

public class Score {
	private final String itemName;
	private final String scoreName;
	private final int value;

	@ConstructorProperties({ "itemName", "scoreName", "value" })
	public Score(final String itemName, final String scoreName, final int value) {
		this.itemName = itemName;
		this.scoreName = scoreName;
		this.value = value;
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getScoreName() {
		return this.scoreName;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Score)) {
			return false;
		}
		final Score other = (Score) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$itemName = this.getItemName();
		final Object other$itemName = other.getItemName();
		Label_0065: {
			if (this$itemName == null) {
				if (other$itemName == null) {
					break Label_0065;
				}
			} else if (this$itemName.equals(other$itemName)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$scoreName = this.getScoreName();
		final Object other$scoreName = other.getScoreName();
		if (this$scoreName == null) {
			if (other$scoreName == null) {
				return this.getValue() == other.getValue();
			}
		} else if (this$scoreName.equals(other$scoreName)) {
			return this.getValue() == other.getValue();
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Score;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $itemName = this.getItemName();
		result = result * 31 + (($itemName == null) ? 0 : $itemName.hashCode());
		final Object $scoreName = this.getScoreName();
		result = result * 31 + (($scoreName == null) ? 0 : $scoreName.hashCode());
		result = result * 31 + this.getValue();
		return result;
	}

	@Override
	public String toString() {
		return "Score(itemName=" + this.getItemName() + ", scoreName=" + this.getScoreName() + ", value=" + this.getValue() + ")";
	}
}
