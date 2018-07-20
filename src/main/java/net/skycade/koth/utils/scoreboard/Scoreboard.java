package net.skycade.koth.utils.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Collections;
import java.util.HashMap;
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
public class Scoreboard {

    private Player player;

    private ScoreboardManager scoreboardManager;
    private org.bukkit.scoreboard.Scoreboard scoreboard;

    private Objective objective;
    private Map<Integer, String> scoreboardLines;

    public Scoreboard(Player player, String title, String[] scoreboardLines) {

        this.player = player;
        this.scoreboardLines = new HashMap<>();

        scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();

        objective = scoreboard.registerNewObjective(player.getName(), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));

        int score = scoreboardLines.length;

        for (String line : scoreboardLines) {
            this.scoreboardLines.put(score, ChatColor.translateAlternateColorCodes('&', line));
            score -= 1;
        }
    }

    public void remove() {
        player.setScoreboard(scoreboardManager.getNewScoreboard());
    }

    public void update() {

        scoreboardLines.forEach((lineId, line) -> {
            Score score = objective.getScore(line);
            score.setScore(lineId);
        });

        player.setScoreboard(scoreboard);
    }

    public void updateTeam(String teamName, String entry, String prefix, String suffix) {
        Team team = (scoreboard.getTeam(teamName) != null ? scoreboard.getTeam(teamName) : scoreboard.registerNewTeam(teamName));

        if (!team.getEntries().contains(entry)) {
            team.addEntry(entry);
        }

        team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
        team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));
    }

    public Map<Integer, String> getScoreboardLines() {
        return scoreboardLines;
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Player getPlayer() {
        return player;
    }
}
