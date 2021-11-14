package dev.miinoo.ucore.sidebar;

public class SidebarException extends RuntimeException {

    public SidebarException(String message) {
        super(message);
    }

    public SidebarException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
