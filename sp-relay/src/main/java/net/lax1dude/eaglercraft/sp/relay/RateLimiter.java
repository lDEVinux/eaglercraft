package net.lax1dude.eaglercraft.sp.relay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RateLimiter {
	
	private final int period;
	private final int limit;
	private final int lockoutLimit;
	private final int lockoutDuration;
	
	private class RateLimitEntry {
		
		protected long timer;
		protected int count;
		protected long lockedTimer;
		protected boolean locked;
		
		protected RateLimitEntry() {
			timer = System.currentTimeMillis();
			count = 0;
			lockedTimer = 0l;
			locked = false;
		}
		
		protected void update() {
			long millis = System.currentTimeMillis();
			if(locked) {
				if(millis - lockedTimer > RateLimiter.this.lockoutDuration) {
					timer = millis;
					count = 0;
					lockedTimer = 0l;
					locked = false;
				}
			}else {
				long p = RateLimiter.this.period / RateLimiter.this.limit;
				int breaker = 0;
				while(millis - timer > p) {
					timer += p;
					--count;
					if(count < 0 || ++breaker > 100) {
						timer = millis;
						count = 0;
						break;
					}
				}
			}
		}
		
	}
	
	public static enum RateLimit {
		NONE, LIMIT, LIMIT_NOW_LOCKOUT, LOCKOUT;
	}
	
	private final Map<String, RateLimitEntry> limiters = new HashMap();
	
	public RateLimiter(int period, int limit, int lockoutLimit, int lockoutDuration) {
		this.period = period;
		this.limit = limit;
		this.lockoutLimit = lockoutLimit;
		this.lockoutDuration = lockoutDuration;
	}
	
	public RateLimit limit(String addr) {
		synchronized(this) {
			RateLimitEntry etr = limiters.get(addr);
			
			if(etr == null) {
				etr = new RateLimitEntry();
				limiters.put(addr, etr);
			}else {
				etr.update();
			}
			
			if(etr.locked) {
				return RateLimit.LOCKOUT;
			}
			
			++etr.count;
			if(etr.count >= lockoutLimit) {
				etr.count = 0;
				etr.locked = true;
				etr.lockedTimer = System.currentTimeMillis();
				return RateLimit.LIMIT_NOW_LOCKOUT;
			}else if(etr.count > limit) {
				return RateLimit.LIMIT;
			}else {
				return RateLimit.NONE;
			}
		}
	}
	
	public void update() {
		synchronized(this) {
			Iterator<RateLimitEntry> itr = limiters.values().iterator();
			while(itr.hasNext()) {
				if(itr.next().count == 0) {
					itr.remove();
				}
			}
		}
	}
	
	public void reset() {
		synchronized(this) {
			limiters.clear();
		}
	}

}
