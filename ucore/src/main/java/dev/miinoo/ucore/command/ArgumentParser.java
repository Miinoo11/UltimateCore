package dev.miinoo.ucore.command;

import java.util.Arrays;
import java.util.List;

public final class ArgumentParser {

	private final String[] args;
	private Object parsed;

	public ArgumentParser(String[] args) {
		this.args = args;
	}

	public String[] getArguments() {
		return args;
	}
	
	public int length() {
		return args.length;
	}

	public boolean hasExactly(int length) {
		return args.length == length;
	}

	public boolean hasAtLeast(int length) {
		return args.length >= length;	
	}

	public String get(int index) {
		return args[index];
	}
	
	public String get(int index, String def) {
		return args.length > index ? args[index] : def;
	}
	
	public ArgumentParser subParser(int from, int to) {
		return new ArgumentParser(Arrays.copyOfRange(args, from, to));
	}
	
	public ArgumentParser subParser(int index) {
		return subParser(index, args.length);
	}
	
	public String asString() {
		String result = "";
		for(int i = 0; i < args.length; i++) {
			result+=args[i];
			if(i != args.length-1) {
				result+=" ";
			}
		}
		return result;
	}
	
	public List<String> asList() {
		return Arrays.asList(args);
	}
	
	public boolean equals(int index, Object obj) {
		return get(index).equals(obj);
	}
	
	public boolean equalsIgnoreCase(int index, Object obj) {
		return get(index).equalsIgnoreCase(String.valueOf(obj));
	}

	public boolean isInt(int index) {
		try {
			parsed = Integer.parseInt(get(index));
		} catch (NumberFormatException exception) {
			return false;
		}
		return true;
	}

	public int getInt(int index) {
		if(isInt(index)) {
			return (int) parsed;
		}
		return 0;
	}

	public boolean isLong(int index) {
		try {
			parsed = Long.parseLong(get(index));
		} catch (NumberFormatException exception) {
			return false;
		}
		return true;
	}

	public long getLong(int index) {
		if(isLong(index)) {
			return (long) parsed;
		}
		return 0;
	}

	public boolean isDouble(int index) {
		try {
			parsed = Double.parseDouble(get(index));
		} catch (NumberFormatException exception) {
			return false;
		}
		return true;
	}
	
	public double getDouble(int index) {
		if(isDouble(index)) {
			return (double) parsed;
		}
		return 0;
	}

	public boolean isFloat(int index) {
		try {
			parsed = Float.parseFloat(get(index));
		} catch (NumberFormatException exception) {
			return false;
		}
		return true;
	}

	public float getFloat(int index) {
		if(isFloat(index)) {
			return (float) parsed;
		}
		return 0;
	}
	
}
