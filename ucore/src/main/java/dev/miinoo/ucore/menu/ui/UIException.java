package dev.miinoo.ucore.menu.ui;

/**
 * @author DotClass
 *
 */
public class UIException extends RuntimeException {

	private static final long serialVersionUID = 3596783864833388373L;

	public UIException() {
		super();
	}

	public UIException(String message, Throwable cause) {
		super(message, cause);
	}

	public UIException(String message) {
		super(message);
	}

	public UIException(Throwable cause) {
		super(cause);
	}

}
