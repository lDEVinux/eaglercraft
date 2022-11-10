package net.md_5.bungee.eaglercraft;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebSocketRateLimiter {
	
	public static enum RateLimit {
		NONE, LIMIT, LOCKED_OUT, NOW_LOCKED_OUT;
		public boolean blocked() {
			return this != NONE;
		}
	}
	
	public final int period;
	public final int limit;
	public final int lockoutLimit;
	public final int lockoutTime;
	public final Collection<String> exceptions;
	
	protected final Map<String, RateLimiter> ratelimiters = new HashMap();
	
	public WebSocketRateLimiter(int period, int limit, int lockoutLimit, int lockoutTime, Collection<String> exceptions) {
		this.period = period;
		this.limit = limit;
		this.lockoutLimit = lockoutLimit;
		this.lockoutTime = lockoutTime;
		this.exceptions = exceptions;
	}
	
	protected static class RateLimiter {
		
		protected final WebSocketRateLimiter limiterConfig;
		
		protected RateLimiter(WebSocketRateLimiter limiterConfig) {
			this.limiterConfig = limiterConfig;
			this.cooldownTimestamp = System.currentTimeMillis();
		}
		
		protected int requestCounter = 0;
		protected long lockoutTimestamp = 0l;
		protected long cooldownTimestamp;
		
		private boolean checkLockout(long currentTimeMillis) {
			if(lockoutTimestamp > 0l) {
				if(currentTimeMillis - lockoutTimestamp < (long)(limiterConfig.lockoutTime * 1000l)) {
					return true;
				}else {
					lockoutTimestamp = 0l;
					requestCounter = 0;
					cooldownTimestamp = currentTimeMillis;
				}
			}
			return false;
		}
		
		private boolean checkCooldown(long currentTimeMillis) {
			long cooldownIncrement = limiterConfig.period * 1000 / limiterConfig.limit;
			while(currentTimeMillis - cooldownTimestamp > cooldownIncrement && requestCounter > 0) {
				--requestCounter;
				cooldownTimestamp += cooldownIncrement;
			}
			if(requestCounter == 0) {
				cooldownTimestamp = currentTimeMillis;
				return false;
			}else {
				return requestCounter >= limiterConfig.limit;
			}
		}
		
		protected RateLimit increment() {
			long t = System.currentTimeMillis();
			if(checkLockout(t)) {
				return RateLimit.LOCKED_OUT;
			}
			++requestCounter;
			boolean blockByCooldown = checkCooldown(t);
			if(requestCounter >= limiterConfig.lockoutLimit) {
				requestCounter = 0;
				cooldownTimestamp = t;
				lockoutTimestamp = t;
				return RateLimit.NOW_LOCKED_OUT;
			}
			if(blockByCooldown) {
				return RateLimit.LIMIT;
			}else {
				return RateLimit.NONE;
			}
		}
		
		protected RateLimit checkLimited() {
			long t = System.currentTimeMillis();
			if(checkLockout(t)) {
				return RateLimit.LOCKED_OUT;
			}else if(checkCooldown(t)) {
				return RateLimit.LIMIT;
			}else {
				return RateLimit.NONE;
			}
		}
		
		protected boolean checkClear() {
			long t = System.currentTimeMillis();
			if(checkLockout(t) || checkCooldown(t)) {
				return false;
			}else if(requestCounter > 0) {
				return false;
			}else {
				return true;
			}
		}
		
	}
	
	public void resetLimiters() {
		synchronized(ratelimiters) {
			ratelimiters.clear();
		}
	}
	
	public void deleteClearLimiters() {
		synchronized(ratelimiters) {
			Iterator<RateLimiter> itr = ratelimiters.values().iterator();
			while(itr.hasNext()) {
				if(itr.next().checkClear()) {
					itr.remove();
				}
			}
		}
	}
	
	public RateLimit checkLimit(InetAddress identifier) {
		return checkLimit(identifier.getHostAddress());
	}
	
	public RateLimit rateLimit(InetAddress identifier) {
		return rateLimit(identifier.getHostAddress());
	}
	
	public RateLimit checkLimit(String identifier) {
		if(exceptions.contains(identifier)) {
			return RateLimit.NONE;
		}
		synchronized(ratelimiters) {
			RateLimiter l = ratelimiters.get(identifier);
			if(l == null) {
				return RateLimit.NONE;
			}else {
				return l.checkLimited();
			}
		}
	}
	
	public RateLimit rateLimit(String identifier) {
		if(exceptions.contains(identifier)) {
			return RateLimit.NONE;
		}
		synchronized(ratelimiters) {
			RateLimiter l = ratelimiters.get(identifier);
			if(l == null) {
				l = new RateLimiter(this);
				ratelimiters.put(identifier, l);
			}
			return l.increment();
		}
	}

}
