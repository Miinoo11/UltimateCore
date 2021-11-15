package dev.miinoo.ucore.version;

import org.bukkit.Bukkit;

public class VersionMatcher {

    public VersionWrapper match() {
        final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
        try {
            return (VersionWrapper) Class.forName(getClass().getPackage().getName() + ".AnvilGUI_v" + serverVersion).newInstance();
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalStateException("Failed to create version wrapper for version " + serverVersion, exception);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("UCore does not support server version \"" + serverVersion + "\"", exception);
        }
    }

}
