// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.config;

import java.beans.ConstructorProperties;

public class TexturePackInfo {
	private final String url;
	private final int size;

	@ConstructorProperties({ "url", "size" })
	public TexturePackInfo(final String url, final int size) {
		this.url = url;
		this.size = size;
	}

	public String getUrl() {
		return this.url;
	}

	public int getSize() {
		return this.size;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof TexturePackInfo)) {
			return false;
		}
		final TexturePackInfo other = (TexturePackInfo) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$url = this.getUrl();
		final Object other$url = other.getUrl();
		if (this$url == null) {
			if (other$url == null) {
				return this.getSize() == other.getSize();
			}
		} else if (this$url.equals(other$url)) {
			return this.getSize() == other.getSize();
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof TexturePackInfo;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $url = this.getUrl();
		result = result * 31 + (($url == null) ? 0 : $url.hashCode());
		result = result * 31 + this.getSize();
		return result;
	}

	@Override
	public String toString() {
		return "TexturePackInfo(url=" + this.getUrl() + ", size=" + this.getSize() + ")";
	}
}
