package net.skycade.koth.utils.scoreboard;

import net.skycade.koth.SkycadeKoth;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
public class ScoreboardManager {

    /** Cache of players with a scoreboard. */
    private Map<UUID, Scoreboard> scoreboards;

    /** Scoreboard update runnable. */
    private BukkitRunnable updateRunnable;

    public ScoreboardManager(SkycadeKoth plugin) {
        scoreboards = new HashMap<>();

        updateRunnable = new BukkitRunnable() {

            @Override
            public void run() {
                scoreboards.forEach((key, value) -> {
                    value.update();
                });
            }
        };

        updateRunnable.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    /**
     * Add a scoreboard to a player.
     * @param scoreboard - Scoreboard.
     */
    public void addScoreboard(Scoreboard scoreboard) {
        if (scoreboards.containsKey(scoreboard.getPlayer().getUniqueId())) {
            return;
        }

        scoreboards.put(scoreboard.getPlayer().getUniqueId(), scoreboard);
    }

    /**
     * Remove a players scoreboard from the cache.
     * @param player - player.
     */
    public void removeScoreboard(Player player) {
        scoreboards.remove(player.getUniqueId());
    }

    /**
     * Get a scoreboard from a player.
     * @param player - player.
     * @return Scoreboard
     */
    public Scoreboard getScoreboard(Player player) { return scoreboards.get(player.getUniqueId()); }

    /**
     * @return Scoreboard cache.
     */
    public Map<UUID, Scoreboard> getScoreboards() {
        return scoreboards;
    }
}
