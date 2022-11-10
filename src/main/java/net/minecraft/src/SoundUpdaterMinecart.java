package net.minecraft.src;

public class SoundUpdaterMinecart implements IUpdatePlayerListBox {
	private final SoundManager theSoundManager;

	/** Minecart which sound is being updated. */
	private final EntityMinecart theMinecart;

	/** The player that is getting the minecart sound updates. */
	private final EntityPlayerSP thePlayer;
	private boolean playerSPRidingMinecart = false;
	private boolean minecartIsDead = false;
	private boolean minecartIsMoving = false;
	private boolean silent = false;
	private float minecartSoundPitch = 0.0F;
	private float minecartMoveSoundVolume = 0.0F;
	private float minecartRideSoundVolume = 0.0F;
	private double minecartSpeed = 0.0D;

	public SoundUpdaterMinecart(SoundManager par1SoundManager, EntityMinecart par2EntityMinecart, EntityPlayerSP par3EntityPlayerSP) {
		this.theSoundManager = par1SoundManager;
		this.theMinecart = par2EntityMinecart;
		this.thePlayer = par3EntityPlayerSP;
	}

	/**
	 * Updates the JList with a new model.
	 */
	public void update() {
		boolean var1 = false;
		boolean var2 = this.playerSPRidingMinecart;
		boolean var3 = this.minecartIsDead;
		boolean var4 = this.minecartIsMoving;
		float var5 = this.minecartMoveSoundVolume;
		float var6 = this.minecartSoundPitch;
		float var7 = this.minecartRideSoundVolume;
		double var8 = this.minecartSpeed;
		this.playerSPRidingMinecart = this.thePlayer != null && this.thePlayer.equals(this.theMinecart.riddenByEntity);
		this.minecartIsDead = this.theMinecart.isDead;
		this.minecartSpeed = (double) MathHelper.sqrt_double(this.theMinecart.motionX * this.theMinecart.motionX + this.theMinecart.motionZ * this.theMinecart.motionZ);
		this.minecartIsMoving = this.minecartSpeed >= 0.01D;

		if (var2 && !this.playerSPRidingMinecart) {
			this.theSoundManager.stopEntitySound(this.thePlayer);
		}

		if (this.minecartIsDead || (!this.silent && this.minecartMoveSoundVolume == 0.0F && this.minecartRideSoundVolume == 0.0F)) {
			if (!var3) {
				this.theSoundManager.stopEntitySound(this.theMinecart);

				if (var2 || this.playerSPRidingMinecart) {
					this.theSoundManager.stopEntitySound(this.thePlayer);
				}
			}

			this.silent = true;

			if (this.minecartIsDead) {
				return;
			}
		}

		if (!this.theSoundManager.isEntitySoundPlaying(this.theMinecart) && this.minecartMoveSoundVolume > 0.0F) {
			this.theSoundManager.playEntitySound("minecart.base", this.theMinecart, this.minecartMoveSoundVolume, this.minecartSoundPitch, false);
			this.silent = false;
			var1 = true;
		}

		if (this.playerSPRidingMinecart && !this.theSoundManager.isEntitySoundPlaying(this.thePlayer) && this.minecartRideSoundVolume > 0.0F) {
			this.theSoundManager.playEntitySound("minecart.inside", this.thePlayer, this.minecartRideSoundVolume, 1.0F, true);
			this.silent = false;
			var1 = true;
		}

		if (this.minecartIsMoving) {
			if (this.minecartSoundPitch < 1.0F) {
				this.minecartSoundPitch += 0.0025F;
			}

			if (this.minecartSoundPitch > 1.0F) {
				this.minecartSoundPitch = 1.0F;
			}

			float var10 = MathHelper.clamp_float((float) this.minecartSpeed, 0.0F, 4.0F) / 4.0F;
			this.minecartRideSoundVolume = 0.0F + var10 * 0.75F;
			var10 = MathHelper.clamp_float(var10 * 2.0F, 0.0F, 1.0F);
			this.minecartMoveSoundVolume = 0.0F + var10 * 0.7F;
		} else if (var4) {
			this.minecartMoveSoundVolume = 0.0F;
			this.minecartSoundPitch = 0.0F;
			this.minecartRideSoundVolume = 0.0F;
		}

		if (!this.silent) {
			if (this.minecartSoundPitch != var6) {
				this.theSoundManager.setEntitySoundPitch(this.theMinecart, this.minecartSoundPitch);
			}

			if (this.minecartMoveSoundVolume != var5) {
				this.theSoundManager.setEntitySoundVolume(this.theMinecart, this.minecartMoveSoundVolume);
			}

			if (this.minecartRideSoundVolume != var7) {
				this.theSoundManager.setEntitySoundVolume(this.thePlayer, this.minecartRideSoundVolume);
			}
		}

		if (this.minecartMoveSoundVolume > 0.0F || this.minecartRideSoundVolume > 0.0F) {
			this.theSoundManager.updateSoundLocation(this.theMinecart);

			if (this.playerSPRidingMinecart) {
				this.theSoundManager.updateSoundLocation(this.thePlayer, this.theMinecart);
			}
		} else {
			if (this.theSoundManager.isEntitySoundPlaying(this.theMinecart)) {
				this.theSoundManager.stopEntitySound(this.theMinecart);
			}

			if (this.playerSPRidingMinecart && this.theSoundManager.isEntitySoundPlaying(this.thePlayer)) {
				this.theSoundManager.stopEntitySound(this.thePlayer);
			}
		}
	}
}
