package dev.miinoo.ucore.menu.ui.callback;

/**
 * @author DotClass
 *
 */
import java.io.Serializable;

@FunctionalInterface
public interface Callback extends Serializable {

	void call();
	
}
