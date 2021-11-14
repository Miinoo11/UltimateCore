package dev.miinoo.ucore.menu.ui;

import com.google.common.base.Preconditions;
import org.bukkit.permissions.Permissible;

/**
 * @author DotClass
 *
 */
public class Permissions {

	public static final String notPermitted = "§cYou are not permitted to do that!";
	
	public static boolean isPermitted(Permissible permissible, String permission) {
		Preconditions.checkNotNull(permissible);
		if(permission != null && !permission.isEmpty()) {
			return permissible.hasPermission(permission);
		}
		return true;
	}
	
}
