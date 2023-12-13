package de.zillolp.ffa.hologram.holograms;

import de.zillolp.ffa.FFA;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TextHologram extends Hologram {
    private final HashMap<Integer, LineHologram> lineHolograms;
    private String[] lines;
    private int linesSize;
    private Location spawnLocation;

    public TextHologram(FFA plugin, String[] lines) {
        super(plugin);
        this.lines = lines;
        this.linesSize = lines.length;
        this.lineHolograms = new HashMap<>();
    }

    @Override
    public void spawn(Player player, Location location) {
        spawnLocation = location;
        double holoHeight = 0.3 * linesSize + 0.75;
        for (int number = 0; number < linesSize; number++) {
            String line = this.lines[number];
            if (line.equalsIgnoreCase("%empty%") || line.equalsIgnoreCase("")) {
                continue;
            }
            LineHologram lineHologram = new LineHologram(plugin, line);
            lineHologram.spawn(player, location.clone().add(0, holoHeight - 0.3 * number, 0));
            lineHolograms.put(number, lineHologram);
        }
    }

    @Override
    public void destroy(Player player) {
        for (LineHologram lineHologram : lineHolograms.values()) {
            lineHologram.destroy(player);
        }
    }

    public void changeLines(Player player, String[] changeLines) {
        if (lineHolograms.isEmpty()) {
            return;
        }
        int count = 0;
        for (String line : changeLines) {
            if (!(lineHolograms.containsKey(count))) {
                destroy(player);
                lines = changeLines;
                linesSize = lines.length;
                lineHolograms.clear();
                spawn(player, spawnLocation);
                break;
            }
            lineHolograms.get(count).changeLine(player, line);
            count++;
        }
    }
}
