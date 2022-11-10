package net.minecraft.src;

public class DamageSource {
	public static DamageSource inFire = (new DamageSource("inFire")).setFireDamage();
	public static DamageSource onFire = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
	public static DamageSource lava = (new DamageSource("lava")).setFireDamage();
	public static DamageSource inWall = (new DamageSource("inWall")).setDamageBypassesArmor();
	public static DamageSource drown = (new DamageSource("drown")).setDamageBypassesArmor();
	public static DamageSource starve = (new DamageSource("starve")).setDamageBypassesArmor();
	public static DamageSource cactus = new DamageSource("cactus");
	public static DamageSource fall = (new DamageSource("fall")).setDamageBypassesArmor();
	public static DamageSource outOfWorld = (new DamageSource("outOfWorld")).setDamageBypassesArmor()
			.setDamageAllowedInCreativeMode();
	public static DamageSource generic = (new DamageSource("generic")).setDamageBypassesArmor();
	public static DamageSource magic = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
	public static DamageSource wither = (new DamageSource("wither")).setDamageBypassesArmor();
	public static DamageSource anvil = new DamageSource("anvil");
	public static DamageSource fallingBlock = new DamageSource("fallingBlock");

	/** This kind of damage can be blocked or not. */
	private boolean isUnblockable = false;
	private boolean isDamageAllowedInCreativeMode = false;
	private float hungerDamage = 0.3F;

	/** This kind of damage is based on fire or not. */
	private boolean fireDamage;

	/** This kind of damage is based on a projectile or not. */
	private boolean projectile;

	/**
	 * Whether this damage source will have its damage amount scaled based on the
	 * current difficulty.
	 */
	private boolean difficultyScaled;

	/** Whether the damage is magic based. */
	private boolean magicDamage = false;
	private boolean explosion = false;
	public String damageType;

	public static DamageSource causeMobDamage(EntityLiving par0EntityLiving) {
		return new EntityDamageSource("mob", par0EntityLiving);
	}

	/**
	 * returns an EntityDamageSource of type player
	 */
	public static DamageSource causePlayerDamage(EntityPlayer par0EntityPlayer) {
		return new EntityDamageSource("player", par0EntityPlayer);
	}

	/**
	 * returns EntityDamageSourceIndirect of an arrow
	 */
	public static DamageSource causeArrowDamage(EntityArrow par0EntityArrow, Entity par1Entity) {
		return (new EntityDamageSourceIndirect("arrow", par0EntityArrow, par1Entity)).setProjectile();
	}

	/**
	 * returns EntityDamageSourceIndirect of a fireball
	 */
	public static DamageSource causeFireballDamage(EntityFireball par0EntityFireball, Entity par1Entity) {
		return par1Entity == null
				? (new EntityDamageSourceIndirect("onFire", par0EntityFireball, par0EntityFireball)).setFireDamage()
						.setProjectile()
				: (new EntityDamageSourceIndirect("fireball", par0EntityFireball, par1Entity)).setFireDamage()
						.setProjectile();
	}

	public static DamageSource causeThrownDamage(Entity par0Entity, Entity par1Entity) {
		return (new EntityDamageSourceIndirect("thrown", par0Entity, par1Entity)).setProjectile();
	}

	public static DamageSource causeIndirectMagicDamage(Entity par0Entity, Entity par1Entity) {
		return (new EntityDamageSourceIndirect("indirectMagic", par0Entity, par1Entity)).setDamageBypassesArmor()
				.setMagicDamage();
	}

	/**
	 * Returns the EntityDamageSource of the Thorns enchantment
	 */
	public static DamageSource causeThornsDamage(Entity par0Entity) {
		return (new EntityDamageSource("thorns", par0Entity)).setMagicDamage();
	}

	public static DamageSource setExplosionSource(Explosion par0Explosion) {
		return par0Explosion != null && par0Explosion.func_94613_c() != null
				? (new EntityDamageSource("explosion.player", par0Explosion.func_94613_c())).setDifficultyScaled()
						.setExplosion()
				: (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
	}

	/**
	 * Returns true if the damage is projectile based.
	 */
	public boolean isProjectile() {
		return this.projectile;
	}

	/**
	 * Define the damage type as projectile based.
	 */
	public DamageSource setProjectile() {
		this.projectile = true;
		return this;
	}

	public boolean isExplosion() {
		return this.explosion;
	}

	public DamageSource setExplosion() {
		this.explosion = true;
		return this;
	}

	public boolean isUnblockable() {
		return this.isUnblockable;
	}

	/**
	 * How much satiate(food) is consumed by this DamageSource
	 */
	public float getHungerDamage() {
		return this.hungerDamage;
	}

	public boolean canHarmInCreative() {
		return this.isDamageAllowedInCreativeMode;
	}

	protected DamageSource(String par1Str) {
		this.damageType = par1Str;
	}

	public Entity getSourceOfDamage() {
		return this.getEntity();
	}

	public Entity getEntity() {
		return null;
	}

	protected DamageSource setDamageBypassesArmor() {
		this.isUnblockable = true;
		this.hungerDamage = 0.0F;
		return this;
	}

	protected DamageSource setDamageAllowedInCreativeMode() {
		this.isDamageAllowedInCreativeMode = true;
		return this;
	}

	/**
	 * Define the damage type as fire based.
	 */
	protected DamageSource setFireDamage() {
		this.fireDamage = true;
		return this;
	}

	/**
	 * Returns the message to be displayed on player death.
	 */
	public String getDeathMessage(EntityLiving par1EntityLiving) {
		EntityLiving var2 = par1EntityLiving.func_94060_bK();
		String var3 = "death.attack." + this.damageType;
		String var4 = var3 + ".player";
		return var2 != null && StatCollector.func_94522_b(var4)
				? StatCollector.translateToLocalFormatted(var4,
						new Object[] { par1EntityLiving.getTranslatedEntityName(), var2.getTranslatedEntityName() })
				: StatCollector.translateToLocalFormatted(var3,
						new Object[] { par1EntityLiving.getTranslatedEntityName() });
	}

	/**
	 * Returns true if the damage is fire based.
	 */
	public boolean isFireDamage() {
		return this.fireDamage;
	}

	/**
	 * Return the name of damage type.
	 */
	public String getDamageType() {
		return this.damageType;
	}

	/**
	 * Set whether this damage source will have its damage amount scaled based on
	 * the current difficulty.
	 */
	public DamageSource setDifficultyScaled() {
		this.difficultyScaled = true;
		return this;
	}

	/**
	 * Return whether this damage source will have its damage amount scaled based on
	 * the current difficulty.
	 */
	public boolean isDifficultyScaled() {
		return this.difficultyScaled;
	}

	/**
	 * Returns true if the damage is magic based.
	 */
	public boolean isMagicDamage() {
		return this.magicDamage;
	}

	/**
	 * Define the damage type as magic based.
	 */
	public DamageSource setMagicDamage() {
		this.magicDamage = true;
		return this;
	}
}
