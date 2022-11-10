package net.minecraft.src;

public class CombatEntry {
	private final DamageSource field_94569_a;
	private final int field_94567_b;
	private final int field_94568_c;
	private final int field_94565_d;
	private final String field_94566_e;
	private final float field_94564_f;

	public CombatEntry(DamageSource par1DamageSource, int par2, int par3, int par4, String par5Str, float par6) {
		this.field_94569_a = par1DamageSource;
		this.field_94567_b = par2;
		this.field_94568_c = par4;
		this.field_94565_d = par3;
		this.field_94566_e = par5Str;
		this.field_94564_f = par6;
	}

	public DamageSource func_94560_a() {
		return this.field_94569_a;
	}

	public int func_94563_c() {
		return this.field_94568_c;
	}

	public boolean func_94559_f() {
		return this.field_94569_a.getEntity() instanceof EntityLiving;
	}

	public String func_94562_g() {
		return this.field_94566_e;
	}

	public String func_94558_h() {
		return this.func_94560_a().getEntity() == null ? null : this.func_94560_a().getEntity().getTranslatedEntityName();
	}

	public float func_94561_i() {
		return this.field_94569_a == DamageSource.outOfWorld ? Float.MAX_VALUE : this.field_94564_f;
	}
}
