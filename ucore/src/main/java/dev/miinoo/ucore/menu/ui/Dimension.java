package dev.miinoo.ucore.menu.ui;

public final class Dimension {

	private int width;
	private int height;

	public Dimension() {
		this(0, 0);
	}

	public Dimension(Dimension d) {
		this(d.width, d.height);
	}

	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setSize(double width, double height) {
		this.width = (int) Math.ceil(width);
		this.height = (int) Math.ceil(height);
	}

	public int getSize() {
		return width * height;
	}

	public void setSize(Dimension d) {
		setSize(d.width, d.height);
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Dimension) {
			Dimension d = (Dimension) obj;
			return (width == d.width) && (height == d.height);
		}
		return false;
	}

	public int hashCode() {
		int sum = width + height;
		return sum * (sum + 1) / 2 + width;
	}

	public String toString() {
		return getClass().getName() + "[width=" + width + ",height=" + height + "]";
	}

}
