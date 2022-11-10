package net.md_5.bungee.api.config;

public class MOTDCacheConfiguration {
	
	public final int cacheTTL;
	public final boolean cacheServerListAnimation;
	public final boolean cacheServerListResults;
	public final boolean cacheServerListTrending;
	public final boolean cacheServerListPortfolios;
	
	public MOTDCacheConfiguration(int cacheTTL, boolean cacheServerListAnimation, boolean cacheServerListResults,
			boolean cacheServerListTrending, boolean cacheServerListPortfolios) {
		this.cacheTTL = cacheTTL;
		this.cacheServerListAnimation = cacheServerListAnimation;
		this.cacheServerListResults = cacheServerListResults;
		this.cacheServerListTrending = cacheServerListTrending;
		this.cacheServerListPortfolios = cacheServerListPortfolios;
	}
	

}
