package dev.miinoo.ucore.configuration;

/**
 * @author DotClass
 *
 */

import org.apache.commons.lang.Validate;

import java.util.Map;

public class ConfigurationSections implements ConfigurationSection {

	private final ConfigurationSection parent;
	private final org.bukkit.configuration.ConfigurationSection section;
	
	ConfigurationSections(ConfigurationSection parent, org.bukkit.configuration.ConfigurationSection section) {
		this.parent = parent;
		this.section = section;
	}
	
	public boolean contains(String path) {
		return contains(path);
	}
	
	public <T> T get(String path) {
		return (T) section.get(path);
	}

	public <T> T get(String path, T def) {
		Validate.notNull(def);
		Class<T> c = (Class<T>) def.getClass();
		Object e = section.get(path);
		if (e != null && c.isAssignableFrom(e.getClass())) {
			return (T) e;
		}
		set(path, def);
		return def;
	}

	public <T> T get(String path, Class<T> c) {
		Object e = section.get(path);
		return e != null && c.isAssignableFrom(e.getClass()) ? (T) e : null;
	}

	public void set(String path, Object obj) {
		section.set(path, obj);
		save();
	}
	
	public ConfigurationSection getSection(String path) {
		org.bukkit.configuration.ConfigurationSection section = this.section.getConfigurationSection(path);
		if(section == null) {
			section = section.createSection(path);
			save();
		}
		return new ConfigurationSections(this, section);
	}

	@Override
	public Map<String, Object> getValues() {
		return section.getValues(false);
	}
	
	public void save() {
		parent.save();
	}

}
