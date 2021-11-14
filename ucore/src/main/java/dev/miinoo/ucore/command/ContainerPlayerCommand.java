package dev.miinoo.ucore.command;

import org.bukkit.entity.Player;

public class ContainerPlayerCommand extends PlayerCommand {

    protected ContainerPlayerCommand(String name, CommandDescription description, String permission) {
        super(name, description, permission);
    }

    protected ContainerPlayerCommand(String name, CommandDescription description) {
        super(name, description);
    }

    protected ContainerPlayerCommand(String name, String permission) {
        super(name, permission);
    }

    protected ContainerPlayerCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(Player player, ArgumentParser args) {
        player.sendMessage("");
        player.sendMessage("§8§m               §r§8[§c " + getName() + " §8]§m               ");
        player.sendMessage("");
        for (Command command : getCommands()) {
            if (command.permission == null || player.hasPermission(command.permission)) {
                player.sendMessage(command.toDescriptionString(player));
            }
        }
        player.sendMessage("");
        return true;
    }

}
