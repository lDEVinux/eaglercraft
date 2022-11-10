// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.plugin;

import java.beans.ConstructorProperties;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class PluginDescription {
	private String name;
	private String main;
	private String version;
	private String author;
	private Set<String> depends;
	private File file;

	public String getName() {
		return this.name;
	}

	public String getMain() {
		return this.main;
	}

	public String getVersion() {
		return this.version;
	}

	public String getAuthor() {
		return this.author;
	}

	public Set<String> getDepends() {
		return this.depends;
	}

	public File getFile() {
		return this.file;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setMain(final String main) {
		this.main = main;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public void setDepends(final Set<String> depends) {
		this.depends = depends;
	}

	public void setFile(final File file) {
		this.file = file;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PluginDescription)) {
			return false;
		}
		final PluginDescription other = (PluginDescription) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		Label_0065: {
			if (this$name == null) {
				if (other$name == null) {
					break Label_0065;
				}
			} else if (this$name.equals(other$name)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$main = this.getMain();
		final Object other$main = other.getMain();
		Label_0102: {
			if (this$main == null) {
				if (other$main == null) {
					break Label_0102;
				}
			} else if (this$main.equals(other$main)) {
				break Label_0102;
			}
			return false;
		}
		final Object this$version = this.getVersion();
		final Object other$version = other.getVersion();
		Label_0139: {
			if (this$version == null) {
				if (other$version == null) {
					break Label_0139;
				}
			} else if (this$version.equals(other$version)) {
				break Label_0139;
			}
			return false;
		}
		final Object this$author = this.getAuthor();
		final Object other$author = other.getAuthor();
		Label_0176: {
			if (this$author == null) {
				if (other$author == null) {
					break Label_0176;
				}
			} else if (this$author.equals(other$author)) {
				break Label_0176;
			}
			return false;
		}
		final Object this$depends = this.getDepends();
		final Object other$depends = other.getDepends();
		Label_0213: {
			if (this$depends == null) {
				if (other$depends == null) {
					break Label_0213;
				}
			} else if (this$depends.equals(other$depends)) {
				break Label_0213;
			}
			return false;
		}
		final Object this$file = this.getFile();
		final Object other$file = other.getFile();
		if (this$file == null) {
			if (other$file == null) {
				return true;
			}
		} else if (this$file.equals(other$file)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PluginDescription;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $name = this.getName();
		result = result * 31 + (($name == null) ? 0 : $name.hashCode());
		final Object $main = this.getMain();
		result = result * 31 + (($main == null) ? 0 : $main.hashCode());
		final Object $version = this.getVersion();
		result = result * 31 + (($version == null) ? 0 : $version.hashCode());
		final Object $author = this.getAuthor();
		result = result * 31 + (($author == null) ? 0 : $author.hashCode());
		final Object $depends = this.getDepends();
		result = result * 31 + (($depends == null) ? 0 : $depends.hashCode());
		final Object $file = this.getFile();
		result = result * 31 + (($file == null) ? 0 : $file.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "PluginDescription(name=" + this.getName() + ", main=" + this.getMain() + ", version=" + this.getVersion() + ", author=" + this.getAuthor() + ", depends=" + this.getDepends() + ", file=" + this.getFile() + ")";
	}

	public PluginDescription() {
		this.depends = new HashSet<String>();
		this.file = null;
	}

	@ConstructorProperties({ "name", "main", "version", "author", "depends", "file" })
	public PluginDescription(final String name, final String main, final String version, final String author, final Set<String> depends, final File file) {
		this.depends = new HashSet<String>();
		this.file = null;
		this.name = name;
		this.main = main;
		this.version = version;
		this.author = author;
		this.depends = depends;
		this.file = file;
	}
}
