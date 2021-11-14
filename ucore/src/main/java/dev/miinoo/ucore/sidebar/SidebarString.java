package dev.miinoo.ucore.sidebar;

import dev.miinoo.ucore.UCore;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SidebarString implements ConfigurationSerializable {

    public SidebarString generateScrollingAnimation(String text, String prefix, String suffix, int width) {

        if (text.length() <= width)
            return new SidebarString(text);

        SidebarString sidebarString = new SidebarString();

        for (int i = 0; i <= text.length() - width; i++) {
            String pre = text.substring(0, i);
            String suf = text.substring(i, width + i);
            if (suf.endsWith("§")) {
                suf = suf.substring(0, suf.length() - 1);
            }
            if (pre.endsWith("§")) {
                pre = pre.substring(0, pre.length() - 1);
                sidebarString.addVariation("§" + suf);
                continue;
            }
            sidebarString.addVariation(prefix + suf + suffix);
            if(text.charAt(i) == '§') {
                i += 2;
            } else if(text.charAt(i) == ' ') {
                i++;
            }
        }
        return sidebarString;
    }

    public SidebarString generateScrollingAnimation(String text, int width) {
        return generateScrollingAnimation(text, "", "", width);
    }

    static {
        ConfigurationSerialization.registerClass(SidebarString.class);
    }

    private List<String> animated = new ArrayList<>();
    private transient int i = 0, curStep;
    private int step = 1;

    public SidebarString(String... variations) {
        if (variations != null && variations.length > 0) {
            animated.addAll(Arrays.asList(variations));
        }
        curStep = step;
    }

    public SidebarString(int step, String... variations) {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be greater than 0!");
        }
        this.step = step;
        if (variations != null && variations.length > 0) {
            animated.addAll(Arrays.asList(variations));
        }
        curStep = step;
    }

    public SidebarString(Player player, String... variations) throws SidebarException {
        addVariation(player, variations);
        curStep = step;
    }

    public SidebarString(Player player, int step, String... variations) throws SidebarException {
        if(step <= 0) {
            throw new IllegalArgumentException("step must be greater than 0!");
        }
        addVariation(player, variations);
        this.step = step;
        curStep = step;
    }

    public SidebarString(Map<String, Object> args) {
        animated = (List<String>) args.get("data");
        try {
            step = args.get("step") == null ? 0 : (Integer) args.get("step");
        } catch (ClassCastException | NullPointerException e) {
            step = 0;
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("data", animated);
        result.put("step", step);
        return result;
    }

    public SidebarString cleanVariations(Player player) {
        List<String> newAnimated = new ArrayList<>();
        boolean lastStartedWithColorChar = false;

        for(String var : animated) {
            if(var.startsWith("§") && lastStartedWithColorChar) {
                newAnimated.add(var);
                lastStartedWithColorChar = true;
            } else if(var.startsWith("§")) {
                lastStartedWithColorChar = true;
            } else if(lastStartedWithColorChar) {
                lastStartedWithColorChar = false;
            } else {
                newAnimated.add(var);
            }
        }
        animated = newAnimated;
        return this;
    }

    public SidebarString setPlaceholders(Player player) throws SidebarException {
        if(UCore.getPlaceholderAPI() == null) {
            throw new SidebarException("Sidebar placeholders were not hooked! PlaceholderAPI was not found!");
        }
        for(int i = 0; i < animated.size(); i++) {
            animated.set(i, PlaceholderAPI.setPlaceholders(player, animated.get(i)));
        }
        return this;
    }

    public String getNext() {
        if(curStep == step) {
            i++;
        }
        curStep++;

        if(curStep > step) {
            curStep = 0;
        }

        if(i > animated.size()) {
            i = 1;
        }
        return animated.get(i - 1);
    }

    public String getNextAndTrim(Logger logger, boolean isLongText) {
        String next = getNext();
        if(next.startsWith("§r") || next.startsWith("§f"))
            next = next.substring(2);
        if(next.startsWith("§r§f") || next.startsWith("§f§r")) {
            next = next.substring(4);
        }
        if(isLongText) {
            if(next.length() > 64) {
                logger.warning("[UCore] Entry variation #" + (i + 1) + " was trimmed to 64 characters (originally \""
                        + next + ")");
                next = next.substring(0, 64);
                if(next.endsWith("§")) {
                    next = next.substring(0, 63);
                }
                animated.set(i - 1, next);
            }
        } else {
            if(next.length() > 28) {
                logger.warning("[UCore] Entry variation #" + (i + 1) + " was trimmed to 28 characters (originally \""
                        + next + ")");
                next = next.substring(0, 28);
                if(next.endsWith("§")) {
                    next = next.substring(0, 27);
                }
                animated.set(i - 1, next);
            }
        }
         return next;
    }

    public SidebarString reset() {
        i = 0;
        curStep = step;
        return this;
    }

    public int getStep() {
        return step;
    }

    public SidebarString setStep(int step) {
        if(step <= 0) {
            throw new IllegalArgumentException("step must be greater than 0!");
        }

        this.step = step;
        curStep = step;
        return this;
    }

    public List<String> getVariations() {
        return animated;
    }

    public SidebarString addVariation(String... variations) {
        animated.addAll(Arrays.asList(variations));
        return this;
    }

    public SidebarString addVariation(Player player, String... variations) {
        if(player != null) {
            throw new SidebarException("Sidebar was unabled to hook into PlaceholderAPI. The API could not be found!");
        }
        if(variations != null && variations.length > 0) {
            if(player != null)
                for(int i = 0; i < variations.length; i++)
                    variations[i] = PlaceholderAPI.setPlaceholders(player, variations[i]);

            animated.addAll(Arrays.asList(variations));
        }
        return this;
    }

    public SidebarString removeVariation(String variation) {
        animated.remove(variation);
        return this;
    }

}
