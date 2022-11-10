// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.score;

import java.beans.ConstructorProperties;

public class Objective {
	private final String name;
	private final String value;

	@ConstructorProperties({ "name", "value" })
	public Objective(final String name, final String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Objective)) {
			return false;
		}
		final Objective other = (Objective) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		Label_0065: {
			if (this$name == null) {
				if (other$name == null) {
					break Label_0065;
				}
			} else if (this$name.equals(other$name)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$value = this.getValue();
		final Object other$value = other.getValue();
		if (this$value == null) {
			if (other$value == null) {
				return true;
			}
		} else if (this$value.equals(other$value)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Objective;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $name = this.getName();
		result = result * 31 + (($name == null) ? 0 : $name.hashCode());
		final Object $value = this.getValue();
		result = result * 31 + (($value == null) ? 0 : $value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Objective(name=" + this.getName() + ", value=" + this.getValue() + ")";
	}
}
