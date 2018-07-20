package net.skycade.koth.game.arena;

import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.database.SkycadeFlatfileDatabase;
import net.skycade.koth.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**************************************************************************************************
 *     Copyright 2018 Jake Brown                                                                  *
 *                                                                                                *
 *     Licensed under the Apache License, Version 2.0 (the "License");                            *
 *     you may not use this file except in compliance with the License.                           *
 *     You may obtain a copy of the License at                                                    *
 *                                                                                                *
 *         http://www.apache.org/licenses/LICENSE-2.0                                             *
 *                                                                                                *
 *     Unless required by applicable law or agreed to in writing, software                        *
 *     distributed under the License is distributed on an "AS IS" BASIS,                          *
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.                   *
 *     See the License for the specific language governing permissions and                        *
 *     limitations under the License.                                                             *
 **************************************************************************************************/
public class ArenaManager {

    /** Configuration instance for arenas. */
    private SkycadeFlatfileDatabase arenaConfig;

    /** Arena cache. */
    private Map<String, Arena> arenaCache;

    public ArenaManager(SkycadeKoth plugin) {

        arenaCache = new HashMap<>();
        arenaConfig = new SkycadeFlatfileDatabase(plugin, "arenas.yml");

        loadArenas();
    }

    /**
     * Load arenas from a configuration file.
     */
    public void loadArenas() {

        FileConfiguration config = arenaConfig.getFileConfiguration();

        for (String arenaName : config.getConfigurationSection("arenas").getKeys(false)) {
            int startingDuration = config.getInt("arenas." + arenaName + ".startDuration");
            Location spawnPoint = LocationUtil.getLocationFromString(config.getString("arenas." + arenaName + ".spawnLocation"));
            Location boundaryPoint1 = LocationUtil.getLocationFromString(config.getString("arenas." + arenaName + ".boundaries.point1"));
            Location boundaryPoint2 = LocationUtil.getLocationFromString(config.getString("arenas." + arenaName + ".boundaries.point2"));

            List<String> lootCommands = config.getStringList("arenas." + arenaName + ".lootCommands");

            arenaCache.put(arenaName, new Arena(arenaName, startingDuration, spawnPoint, boundaryPoint1, boundaryPoint2, lootCommands));
            System.out.println("Cached a new arena.. " + arenaName);
        }
    }

    /**
     * Get an arena by its specific name.
     * @param name
     * @return
     */
    public Arena getArenaByName(String name) {
        return arenaCache.get(name);
    }

    /**
     * Get an arena by a specific location.
     * @param location - location.
     * @return Arena
     */
    public Arena getArenaByLocation(Location location) {
        for (Arena arena : arenaCache.values()) {
            if (LocationUtil.isWithinLocation(location, arena.getArenaBoundaryPoint1(), arena.getArenaBoundaryPoint2())) {
                return arena;
            }
        }
        return null;
    }

    public Map<String, Arena> getArenaCache() {
        return arenaCache;
    }

    public SkycadeFlatfileDatabase getArenaConfig() {
        return arenaConfig;
    }
}
