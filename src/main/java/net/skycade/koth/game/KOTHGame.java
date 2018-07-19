package net.skycade.koth.game;

import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.game.arena.Arena;
import net.skycade.koth.game.countdown.Countdown;
import net.skycade.koth.utils.messages.MessageUtil;
import net.skycade.koth.utils.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
public class KOTHGame {

    private SkycadeKoth plugin;

    /** ID for the game.. typically a name. */
    private String gameId;

    /** List of players in the KOTH gamemode */
    private List<UUID> activePlayers;

    /** Current arena being used in the game. */
    private Arena currentArena;

    /** Current phase of the game */
    private GamePhase currentPhase;

    public KOTHGame(SkycadeKoth plugin, String gameId, Arena currentArena) {
        this.plugin = plugin;
        this.gameId = gameId;
        this.currentArena = currentArena;

        this.activePlayers = new ArrayList<>();
        this.currentPhase = GamePhase.WAITING;
    }

    public void startCountdown() {

        currentPhase = GamePhase.COUNTDOWN;

        // Phase countdown
        plugin.getCountdownManager().startCountdown(
                new Countdown(60, new int[] { 1, 2, 3, 4, 5, 10, 15, 30, 60 }),
                currentValue -> getActivePlayers().forEach(uuid -> MessageUtil.sendMessage(Bukkit.getPlayer(uuid), "countdown", Collections.singletonList(new Placeholder("%seconds%", currentValue)))), finished -> {
                    // Start the game and set it to in progress.

                    currentPhase = GamePhase.IN_PROGRESS;

                    MessageUtil.sendMessageToPlayer(Bukkit.getPlayer("ThatAbstractWolf"), "Game started.. Phase: " + currentPhase.name());

                    getActivePlayers().forEach(uuid -> {
                        Player player = Bukkit.getPlayer(uuid);
                        player.teleport(currentArena.getSpawnLocation());
                    });
                }
        );
    }

    public String getGameId() {
        return gameId;
    }

    public void addActivePlayer(Player player) {
        activePlayers.add(player.getUniqueId());
    }

    public void removeActivePlayer(Player player) {
        activePlayers.remove(player.getUniqueId());
    }

    public List<UUID> getActivePlayers() {
        return activePlayers;
    }
}
