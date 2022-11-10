package net.lax1dude.eaglercraft;

public enum HighPolySkin {

	LONG_ARMS(
			new TextureLocation("/mesh/longarms.png"),
			new ModelLocation("/mesh/longarms0.mdl"),
			null,
			new ModelLocation("/mesh/longarms2.mdl"),
			new ModelLocation[] {
				new ModelLocation("/mesh/longarms1.mdl")
			},
			new float[] {
				1.325f
			},
			0.0f,
			new TextureLocation("/mesh/longarms.fallback.png")
	),
	
	WEIRD_CLIMBER_DUDE(
			new TextureLocation("/mesh/weirdclimber.png"),
			new ModelLocation("/mesh/weirdclimber0.mdl"),
			null,
			new ModelLocation("/mesh/weirdclimber2.mdl"),
			new ModelLocation[] {
				new ModelLocation("/mesh/weirdclimber1.mdl")
			},
			new float[] {
				2.62f
			},
			-90.0f,
			new TextureLocation("/mesh/weirdclimber.fallback.png")
	),
	
	LAXATIVE_DUDE(
			new TextureLocation("/mesh/laxativedude.png"),
			new ModelLocation("/mesh/laxativedude0.mdl"),
			null,
			new ModelLocation("/mesh/laxativedude3.mdl"),
			new ModelLocation[] {
				new ModelLocation("/mesh/laxativedude1.mdl"),
				new ModelLocation("/mesh/laxativedude2.mdl")
			},
			new float[] {
				2.04f
			},
			0.0f,
			new TextureLocation("/mesh/laxativedude.fallback.png")
	),
	
	BABY_CHARLES(
			new TextureLocation("/mesh/charles.png"),
			new ModelLocation("/mesh/charles0.mdl"),
			new ModelLocation("/mesh/charles1.mdl"),
			new ModelLocation("/mesh/charles2.mdl"),
			new ModelLocation[] {},
			new float[] {},
			0.0f,
			new TextureLocation("/mesh/charles.fallback.png")
	),
	
	BABY_WINSTON(
			new TextureLocation("/mesh/winston.png"),
			new ModelLocation("/mesh/winston0.mdl"),
			null,
			new ModelLocation("/mesh/winston1.mdl"),
			new ModelLocation[] {},
			new float[] {},
			0.0f,
			new TextureLocation("/mesh/winston.fallback.png")
	);
	
	public static float highPolyScale = 0.5f;

	public final TextureLocation texture;
	public final ModelLocation bodyModel;
	public final ModelLocation headModel;
	public final ModelLocation eyesModel;
	public final ModelLocation[] limbsModel;
	public final float[] limbsOffset;
	public final float limbsInitialRotation;
	public final TextureLocation fallbackTexture;
	
	HighPolySkin(TextureLocation texture, ModelLocation bodyModel, ModelLocation headModel, ModelLocation eyesModel,
			ModelLocation[] limbsModel, float[] limbsOffset, float limbsInitialRotation, TextureLocation fallbackTexture) {
		this.texture = texture;
		this.bodyModel = bodyModel;
		this.headModel = headModel;
		this.eyesModel = eyesModel;
		this.limbsModel = limbsModel;
		this.limbsOffset = limbsOffset;
		this.limbsInitialRotation = limbsInitialRotation;
		this.fallbackTexture = fallbackTexture;
	}

}
