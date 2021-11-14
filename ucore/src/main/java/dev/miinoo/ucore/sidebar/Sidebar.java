package dev.miinoo.ucore.sidebar;

import com.google.common.collect.ImmutableSet;
import dev.miinoo.ucore.UCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.*;

public class Sidebar implements ConfigurationSerializable, Sidebars {

    private static final Set<String> EMPTY_COLOR_STRINGS = ImmutableSet.of("§f", "§f§r", "§r", "§r§f");
    private static transient ScoreboardManager manager = Bukkit.getScoreboardManager();

    static {
        ConfigurationSerialization.registerClass(Sidebar.class);
    }

    private final transient Plugin plugin;
    private List<SidebarString> entries;
    private transient Scoreboard scoreboard;
    private transient Objective objective;
    private transient BukkitTask updateTask;
    private String title;
    private Player player = null;
    private transient Team[] teams = new Team[15];
    private int prevEntries = 0;

    public Sidebar(Plugin plugin, String title, int delay, SidebarString... entries) {
        this.plugin = plugin;
        this.title = title;
        this.entries = new ArrayList<>(Arrays.asList(entries));

        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("obj", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(this.title);

        for (int i = 0; i < 15; i++) {
            Team team = scoreboard.registerNewTeam("team" + String.valueOf(i));
            team.addEntry(ChatColor.values()[i].toString() + "§r");
            teams[i] = team;
        }
        update();

        setUpdateDelay(plugin, delay);

        SidebarAPI.registerSidebar(this);
    }

    public Sidebar(Map<String, Object> args) {
        entries = (List<SidebarString>) args.get("entries");
        title = (String) args.get("title");

        if(args.containsKey("placeholders")) {
            player = Bukkit.getPlayer(UUID.fromString((String) args.get("placeholders")));
        }
        plugin = UCore.getSidebarAPI().getPlugin();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("entries", entries);
        result.put("title", title);

        if(player != null) {
            result.put("placeholders", player.getUniqueId().toString());
        }

        return result;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Sidebar setUpdateDelay(Plugin plugin, int delay) {
        if(delay < 1) {
            throw new IllegalArgumentException("delay can not be less than 1!");
        }

        if(updateTask != null) {
            updateTask.cancel();
        }

        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimer(plugin, delay, delay);
        return this;
    }

    public Sidebar setAllPlaceholders(Player player) {
        for(SidebarString entry : entries) {
            entry.setPlaceholders(player);
        }
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public Sidebar setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public List<SidebarString> getEntries() {
        return entries;
    }

    public Sidebar setEntries(List<SidebarString> entries) {
        this.entries = entries;
        return this;
    }

    public Sidebar addEntry(SidebarString... entry) {
        entries.addAll(Arrays.asList(entry));
        return this;
    }

    public Sidebar removeEntry(SidebarString entry) {
        entries.remove(entry);
        return this;
    }

    public Sidebar removeEntry(int i) {
        entries.remove(i);
        return this;
    }

    public Sidebar showTo(Player player) {
        player.setScoreboard(scoreboard);
        return this;
    }

    public Sidebar hideFrom(Player player) {
        player.setScoreboard(manager.getMainScoreboard());
        return this;
    }

    public Sidebar update() {
        if(player != null) {
            setAllPlaceholders(player);
        }

        if(entries.size() <= 15) {
            updateAntiFlicker();
        } else {
            updateFallback();
        }
        return this;
    }

    private void updateFallback() {
        if(player != null) {
            setAllPlaceholders(player);
        }

        redoBukkitObjective();

        for(int i = entries.size(); i > 0; i--) {
            objective.getScore(entries.get(entries.size() - i).getNext()).setScore(i);
        }
    }

    private void redoBukkitObjective() {
        objective.unregister();
        objective = scoreboard.registerNewObjective("obj", "dummy");
        objective.setDisplayName(title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private void updateAntiFlicker() {
        if(prevEntries != entries.size()) {
            redoBukkitTeams();
            for(int i = 0; i < entries.size(); i++) {
                objective.getScore(ChatColor.values()[i]+"§r").setScore(entries.size() - 1 - i);
            }
        }

        prevEntries = entries.size();
        for(int i = 0; i < prevEntries; i++) {
            SidebarString entry = entries.get(i);
            String entryString = ChatColor.translateAlternateColorCodes('&', entry.getNextAndTrim(plugin.getLogger(), false));

            if(entryString.startsWith("§r") || entryString.startsWith("§f")) {
                entryString = entryString.substring(2);
            }
            if(entryString.length() <= 16) {
                teams[i].setPrefix(entryString);
                teams[i].setSuffix("");
            } else {
                if(!entryString.contains("§")) {
                    teams[i].setPrefix(entryString.substring(0, 16));
                    teams[i].setSuffix(entryString.substring(0, 16));
                } else {
                    boolean carryingColor = false;
                    String[] sections = entryString.split("§");
                    StringBuilder color = new StringBuilder();
                    int len = 0;
                    for (String section : sections) {
                        if (section.length() == 0)
                            continue;

                        if (section.length() == 1) {
                            if (carryingColor)
                                color.append("§").append(section);
                            else
                                color = new StringBuilder("§").append(section);
                            carryingColor = true;
                        } else {
                            if (carryingColor)
                                color.append('§').append(section, 0, 1);
                            else
                                color = new StringBuilder("§").append(section, 0, 1);
                            len += section.length() - 1;
                            carryingColor = false;
                        }
                        if (len >= 16) {
                            String teamSuffix = entryString.substring(16, entryString.length());
                            String teamPrefix = entryString.substring(0, 16);

                            teams[i].setPrefix(teamPrefix);

                            // Set suffix
                            // Color needs only be included if it has any effect
                            // (i.e. if it is not "empty"/"whitespace")
                            String colorStr = color.toString().toLowerCase(Locale.ENGLISH);
                            teams[i].setSuffix(
                                    EMPTY_COLOR_STRINGS.contains(colorStr) ? teamSuffix : color + teamSuffix);

                            break;
                        }

                    }
                }
            }
        }

    }

    public Sidebar addEmpty() {
        entries.add(new SidebarString(new String(new char[entries.size()]).replace("\0", " ")));
        return this;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private void redoBukkitTeams() {
        for (int i = 0; i < 15; i++) {
            scoreboard.resetScores(ChatColor.values()[i] + "§r");
            teams[i].setSuffix("");
            teams[i].setPrefix("");
        }
    }
}
