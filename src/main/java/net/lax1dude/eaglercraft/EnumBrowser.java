package net.lax1dude.eaglercraft;

public enum EnumBrowser {

	DESKTOP("Desktop"), CHROME("Chrome"), EDGE("Edge"), IE("IE"), FIREFOX("Firefox"), SAFARI("Safari"), OPERA("Opera"), WEBKIT("WebKit"), GECKO("Gecko"), UNKNOWN("Unknown");

	public final String name;
	
	private EnumBrowser(String string) {
		this.name = string;
	} 
	
	private static EnumBrowser identifiedBrowser = null;
	
	public static EnumBrowser getBrowser() {
		if(identifiedBrowser == null) {
			String ua = " " + EaglerAdapter.getUserAgent().toLowerCase();
			if(ua.contains(" edg/")) {
				identifiedBrowser = EDGE;
			}else if(ua.contains(" opr/")) {
				identifiedBrowser = OPERA;
			}else if(ua.contains(" chrome/")) {
				identifiedBrowser = CHROME;
			}else if(ua.contains(" firefox/")) {
				identifiedBrowser = FIREFOX;
			}else if(ua.contains(" safari/")) {
				identifiedBrowser = SAFARI;
			}else if(ua.contains(" trident/") || ua.contains(" msie")) {
				identifiedBrowser = IE;
			}else if(ua.contains(" webkit/")) {
				identifiedBrowser = WEBKIT;
			}else if(ua.contains(" gecko/")) {
				identifiedBrowser = GECKO;
			}else if(ua.contains(" desktop/")) {
				identifiedBrowser = DESKTOP;
			}else {
				identifiedBrowser = UNKNOWN;
			}
		}
		return identifiedBrowser;
	}
	
	public String toString() {
		return name;
	}
	
}
