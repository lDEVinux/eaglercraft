package net.minecraft.src;

public class CreativeTabs {
	public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
	public static final CreativeTabs tabBlock = new CreativeTabBlock(0, "buildingBlocks");
	public static final CreativeTabs tabDecorations = new CreativeTabDeco(1, "decorations");
	public static final CreativeTabs tabRedstone = new CreativeTabRedstone(2, "redstone");
	public static final CreativeTabs tabTransport = new CreativeTabTransport(3, "transportation");
	public static final CreativeTabs tabMisc = new CreativeTabMisc(4, "misc");
	public static final CreativeTabs tabAllSearch = (new CreativeTabSearch(5, "search"))
			.setBackgroundImageName("search.png");
	public static final CreativeTabs tabFood = new CreativeTabFood(6, "food");
	public static final CreativeTabs tabTools = new CreativeTabTools(7, "tools");
	public static final CreativeTabs tabCombat = new CreativeTabCombat(8, "combat");
	public static final CreativeTabs tabBrewing = new CreativeTabBrewing(9, "brewing");
	public static final CreativeTabs tabMaterials = new CreativeTabMaterial(10, "materials");
	public static final CreativeTabs tabInventory = (new CreativeTabInventory(11, "inventory"))
			.setBackgroundImageName("survival_inv.png").setNoScrollbar().setNoTitle();
	private final int tabIndex;
	private final String tabLabel;

	/** Texture to use. */
	private String theTexture = "list_items.png";
	private boolean hasScrollbar = true;

	/** Whether to draw the title in the foreground of the creative GUI */
	private boolean drawTitle = true;

	public CreativeTabs(int par1, String par2Str) {
		this.tabIndex = par1;
		this.tabLabel = par2Str;
		creativeTabArray[par1] = this;
	}

	public CreativeTabs setBackgroundImageName(String par1Str) {
		this.theTexture = par1Str;
		return this;
	}

	public CreativeTabs setNoTitle() {
		this.drawTitle = false;
		return this;
	}

	public CreativeTabs setNoScrollbar() {
		this.hasScrollbar = false;
		return this;
	}
}
