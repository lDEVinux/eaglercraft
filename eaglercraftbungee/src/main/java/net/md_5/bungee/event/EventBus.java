// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventBus {
	private final Map<Class<?>, Map<Object, Method[]>> eventToHandler;
	private final ReadWriteLock lock;
	private final Logger logger;
	private final Class<? extends Annotation>[] annotations;

	public EventBus() {
		this((Logger) null, (Class<? extends Annotation>[]) null);
	}

	public EventBus(final Logger logger) {
		this(logger, (Class<? extends Annotation>[]) null);
	}

	public EventBus(final Class<? extends Annotation>... annotations) {
		this((Logger) null, annotations);
	}

	public EventBus(final Logger logger, final Class<? extends Annotation>... annotations) {
		this.eventToHandler = new HashMap<Class<?>, Map<Object, Method[]>>();
		this.lock = new ReentrantReadWriteLock();
		this.logger = ((logger == null) ? Logger.getGlobal() : logger);
		this.annotations = ((annotations == null || annotations.length == 0) ? new Class[] { EventHandler.class } : annotations);
	}

	public void post(final Object event) {
		this.lock.readLock().lock();
		try {
			final Map<Object, Method[]> handlers = this.eventToHandler.get(event.getClass());
			if (handlers != null) {
				for (final Map.Entry<Object, Method[]> handler : handlers.entrySet()) {
					for (final Method method : handler.getValue()) {
						try {
							method.invoke(handler.getKey(), event);
						} catch (IllegalAccessException ex) {
							throw new Error("Method became inaccessible: " + event, ex);
						} catch (IllegalArgumentException ex2) {
							throw new Error("Method rejected target/argument: " + event, ex2);
						} catch (InvocationTargetException ex3) {
							this.logger.log(Level.WARNING, MessageFormat.format("Error dispatching event {0} to listener {1}", event, handler.getKey()), ex3.getCause());
						}
					}
				}
			}
		} finally {
			this.lock.readLock().unlock();
		}
	}

	private Map<Class<?>, Set<Method>> findHandlers(final Object listener) {
		final Map<Class<?>, Set<Method>> handler = new HashMap<Class<?>, Set<Method>>();
		for (final Method m : listener.getClass().getDeclaredMethods()) {
			for (final Class<? extends Annotation> annotation : this.annotations) {
				if (m.isAnnotationPresent(annotation)) {
					final Class<?>[] params = m.getParameterTypes();
					if (params.length == 1) {
						Set<Method> existing = handler.get(params[0]);
						if (existing == null) {
							existing = new HashSet<Method>();
							handler.put(params[0], existing);
						}
						existing.add(m);
						break;
					}
					this.logger.log(Level.INFO, "Method {0} in class {1} annotated with {2} does not have single argument", new Object[] { m, listener.getClass(), annotation });
				}
			}
		}
		return handler;
	}

	public void register(final Object listener) {
		final Map<Class<?>, Set<Method>> handler = this.findHandlers(listener);
		this.lock.writeLock().lock();
		try {
			for (final Map.Entry<Class<?>, Set<Method>> e : handler.entrySet()) {
				Map<Object, Method[]> a = this.eventToHandler.get(e.getKey());
				if (a == null) {
					a = new HashMap<Object, Method[]>();
					this.eventToHandler.put(e.getKey(), a);
				}
				final Method[] baked = new Method[e.getValue().size()];
				a.put(listener, e.getValue().toArray(baked));
			}
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	public void unregister(final Object listener) {
		final Map<Class<?>, Set<Method>> handler = this.findHandlers(listener);
		this.lock.writeLock().lock();
		try {
			for (final Map.Entry<Class<?>, Set<Method>> e : handler.entrySet()) {
				final Map<Object, Method[]> a = this.eventToHandler.get(e.getKey());
				if (a != null) {
					a.remove(listener);
					if (!a.isEmpty()) {
						continue;
					}
					this.eventToHandler.remove(e.getKey());
				}
			}
		} finally {
			this.lock.writeLock().unlock();
		}
	}
}
