package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class ComponentNetherBridgeStartPiece extends ComponentNetherBridgeCrossing3 {
	/** Instance of StructureNetherBridgePieceWeight. */
	public StructureNetherBridgePieceWeight theNetherBridgePieceWeight;

	/**
	 * Contains the list of valid piece weights for the set of nether bridge
	 * structure pieces.
	 */
	public List primaryWeights = new ArrayList();

	/**
	 * Contains the list of valid piece weights for the secondary set of nether
	 * bridge structure pieces.
	 */
	public List secondaryWeights;
	public ArrayList field_74967_d = new ArrayList();

	public ComponentNetherBridgeStartPiece(EaglercraftRandom par1Random, int par2, int par3) {
		super(par1Random, par2, par3);
		StructureNetherBridgePieceWeight[] var4 = StructureNetherBridgePieces.getPrimaryComponents();
		int var5 = var4.length;
		int var6;
		StructureNetherBridgePieceWeight var7;

		for (var6 = 0; var6 < var5; ++var6) {
			var7 = var4[var6];
			var7.field_78827_c = 0;
			this.primaryWeights.add(var7);
		}

		this.secondaryWeights = new ArrayList();
		var4 = StructureNetherBridgePieces.getSecondaryComponents();
		var5 = var4.length;

		for (var6 = 0; var6 < var5; ++var6) {
			var7 = var4[var6];
			var7.field_78827_c = 0;
			this.secondaryWeights.add(var7);
		}
	}
}
