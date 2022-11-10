// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public class PluginClassloader extends URLClassLoader {
	private static final Set<PluginClassloader> allLoaders;

	public PluginClassloader(final URL[] urls) {
		super(urls);
		PluginClassloader.allLoaders.add(this);
	}

	@Override
	protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
		return this.loadClass0(name, resolve, true);
	}

	private Class<?> loadClass0(final String name, final boolean resolve, final boolean checkOther) throws ClassNotFoundException {
		try {
			return super.loadClass(name, resolve);
		} catch (ClassNotFoundException ex) {
			if (checkOther) {
				for (final PluginClassloader loader : PluginClassloader.allLoaders) {
					if (loader != this) {
						try {
							return loader.loadClass0(name, resolve, false);
						} catch (ClassNotFoundException ex2) {
						}
					}
				}
			}
			throw new ClassNotFoundException(name);
		}
	}

	static {
		allLoaders = new HashSet<PluginClassloader>();
	}
}
