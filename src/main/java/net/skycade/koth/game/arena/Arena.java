package net.skycade.koth.game.arena;

import org.bukkit.Location;

import java.util.List;

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
public class Arena {

    /** Name for the arena. */
    private String arenaName;

    /** The duration the game will start at, for example 900 = 900 seconds = 15 minutes. */
    private int startingDuration;

    /** Spawn point everyone is transported to. */
    private Location spawnLocation;

    /** Boundary points for an arena. */
    private Location arenaBoundaryPoint1;
    private Location arenaBoundaryPoint2;

    /** The commands executed when a player wins.. */
    private List<String> lootCommands;

    public Arena(String arenaName, int startingDuration, Location spawnLocation, Location arenaBoundaryPoint1, Location arenaBoundaryPoint2, List<String> lootCommands) {
        this.arenaName = arenaName;
        this.startingDuration = startingDuration;
        this.spawnLocation = spawnLocation;
        this.arenaBoundaryPoint1 = arenaBoundaryPoint1;
        this.arenaBoundaryPoint2 = arenaBoundaryPoint2;
        this.lootCommands = lootCommands;
    }

    /**
     * @return Get arena name.
     */
    public String getArenaName() {
        return arenaName;
    }

    /**
     * @return Get starting duration.
     */
    public int getStartingDuration() {
        return startingDuration;
    }

    /**
     * @return Get spawn location.
     */
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    /**
     * @return Get point 1 for the arena boundary.
     */
    public Location getArenaBoundaryPoint1() {
        return arenaBoundaryPoint1;
    }

    /**
     * @return Get point 2 for the arena boundary.
     */
    public Location getArenaBoundaryPoint2() {
        return arenaBoundaryPoint2;
    }

    /**
     * @return Get all the commands executed at the end of a game.
     */
    public List<String> getLootCommands() {
        return lootCommands;
    }
}
