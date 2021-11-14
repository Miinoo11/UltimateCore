package dev.miinoo.ucore.menu.ui.input;

/**
 * @author DotClass
 *
 */

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class GUIInput<T> {

	private static final Map<Class, GUIInput> inputs = new HashMap<>();
	
	static {
		register(new AnvilInput());
		register(new AnvilLongInput());
	}

	public static void register(GUIInput<?> input) {
		inputs.put(input.getClass(), input);
	}

	public static <T> boolean get(Player player, T def, Consumer<T> input, String name) {
		GUIInput<T> _input = inputs.values().stream().filter(ginput-> ginput.name == name).findFirst().get();
		if(_input != null) {
			_input.get(player, def, input);
			return true;
		}
		return false;
	}

	public static <T> boolean get(Player player, T def, Consumer<T> input, Class<? extends GUIInput<T>> clazz) {
		GUIInput<T> _input = inputs.get(clazz);
		if(_input != null) {
			_input.get(player, def, input);
			return true;
		}
		return false;
	}

	private final String name;

	protected GUIInput(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	abstract void get(Player player, T def, Consumer<T> input);

}