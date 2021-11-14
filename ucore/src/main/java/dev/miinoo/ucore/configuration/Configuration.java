package dev.miinoo.ucore.configuration;

/**
 * @author DotClass
 *
 */

import dev.miinoo.ucore.configuration.json.JsonConfiguration;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Configuration implements ConfigurationSection {

	protected File file;
	protected FileConfiguration configuration;
	private boolean virgin;

	public Configuration(File file) {
		this.file = file;
		if(!file.isFile()) {
			virgin = true;
		}
		if (file.getName().endsWith(".json")) {
			configuration = JsonConfiguration.loadConfiguration(file);
		} else {
			configuration = YamlConfiguration.loadConfiguration(file);
		}
		loadConfiguration();
	}

	public Configuration(String path) {
		this(new File(path));
	}

	protected void loadConfiguration() {
	}

	protected boolean isVirgin() {
		return virgin;
	}

	public boolean contains(String path) {
		return configuration.contains(path);
	}

	public <T> T get(String path) {
		return (T) configuration.get(path);
	}

	public <T> T get(String path, T def) {
		Validate.notNull(def);
		Class<T> c = (Class<T>) def.getClass();
		Object e = configuration.get(path);
		if (e != null && c.isAssignableFrom(e.getClass())) {
			return (T) e;
		}
		set(path, def);
		return def;
	}

	public <T> T get(String path, Class<T> c) {
		Object e = configuration.get(path);
		return e != null && c.isAssignableFrom(e.getClass()) ? (T) e : null;
	}

	public void set(String path, Object obj) {
		configuration.set(path, obj);
		save();
	}

	public boolean isSection(String path) {
		return configuration.isConfigurationSection(path);
	}

	public ConfigurationSection getSection(String path) {
		org.bukkit.configuration.ConfigurationSection section = configuration.getConfigurationSection(path);
		if (section == null) {
			section = configuration.createSection(path);
			save();
		}
		return new ConfigurationSections(this, section);
	}

	@Override
	public Map<String, Object> getValues() {
		return configuration.getValues(false);
	}

	public void save() {
		try {
			configuration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
