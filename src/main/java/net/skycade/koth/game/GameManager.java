package net.skycade.koth.game;

import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.game.arena.Arena;
import net.skycade.koth.game.countdown.Countdown;
import net.skycade.koth.utils.messages.MessageUtil;
import net.skycade.koth.utils.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

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
public class GameManager {

    private SkycadeKoth plugin;

    private Map<String, KOTHGame> activeKOTHGames;

    public GameManager(SkycadeKoth plugin) {
        this.plugin = plugin;
        this.activeKOTHGames = new HashMap<>();
    }

    /**
     * Start a new game of KOTH.
     */
    public void startGame(String username, KOTHGame game) {
        activeKOTHGames.put(game.getGameId(), game);

        if (Bukkit.getPlayer(username).isOnline()) {
            game.addActivePlayer(Bukkit.getPlayer(username));
        }

        game.startCountdown();
    }

    /**
     * End a game of KOTH.
     */
    public void endGame(KOTHGame game) {

    }

    public KOTHGame getGameById(String gameId) {
        return activeKOTHGames.get(gameId);
    }

    public Map<String, KOTHGame> getActiveKOTHGames() {
        return activeKOTHGames;
    }
}
