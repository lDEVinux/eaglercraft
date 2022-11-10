package net.minecraft.src;

public class WeightedRandomMinecart extends WeightedRandomItem {
	public final NBTTagCompound field_98222_b;
	public final String minecartName;

	final MobSpawnerBaseLogic field_98221_d;

	public WeightedRandomMinecart(MobSpawnerBaseLogic par1MobSpawnerBaseLogic, NBTTagCompound par2NBTTagCompound) {
		super(par2NBTTagCompound.getInteger("Weight"));
		this.field_98221_d = par1MobSpawnerBaseLogic;
		NBTTagCompound var3 = par2NBTTagCompound.getCompoundTag("Properties");
		String var4 = par2NBTTagCompound.getString("Type");

		if (var4.equals("Minecart")) {
			if (var3 != null) {
				switch (var3.getInteger("Type")) {
				case 0:
					var4 = "MinecartRideable";
					break;

				case 1:
					var4 = "MinecartChest";
					break;

				case 2:
					var4 = "MinecartFurnace";
				}
			} else {
				var4 = "MinecartRideable";
			}
		}

		this.field_98222_b = var3;
		this.minecartName = var4;
	}

	public WeightedRandomMinecart(MobSpawnerBaseLogic par1MobSpawnerBaseLogic, NBTTagCompound par2NBTTagCompound,
			String par3Str) {
		super(1);
		this.field_98221_d = par1MobSpawnerBaseLogic;

		if (par3Str.equals("Minecart")) {
			if (par2NBTTagCompound != null) {
				switch (par2NBTTagCompound.getInteger("Type")) {
				case 0:
					par3Str = "MinecartRideable";
					break;

				case 1:
					par3Str = "MinecartChest";
					break;

				case 2:
					par3Str = "MinecartFurnace";
				}
			} else {
				par3Str = "MinecartRideable";
			}
		}

		this.field_98222_b = par2NBTTagCompound;
		this.minecartName = par3Str;
	}

	public NBTTagCompound func_98220_a() {
		NBTTagCompound var1 = new NBTTagCompound();
		var1.setCompoundTag("Properties", this.field_98222_b);
		var1.setString("Type", this.minecartName);
		var1.setInteger("Weight", this.itemWeight);
		return var1;
	}
}
