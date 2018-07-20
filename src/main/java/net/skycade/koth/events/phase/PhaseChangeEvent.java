package net.skycade.koth.events.phase;

import net.skycade.koth.events.SkycadeEvent;
import net.skycade.koth.game.GamePhase;
import net.skycade.koth.game.KOTHGame;

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
public class PhaseChangeEvent extends SkycadeEvent {

    /** The current game the phase is changing in. */
    private KOTHGame currentGame;
    /** The last phase before changing. */
    private GamePhase previousPhase;
    /** The current phase after changing. */
    private GamePhase newPhase;

    /**
     * Create a new instance of {@link PhaseChangeEvent}
     * @param currentGame - current game.
     * @param previousPhase - last phase.
     * @param newPhase - new phase.
     */
    public PhaseChangeEvent(KOTHGame currentGame, GamePhase previousPhase, GamePhase newPhase) {
        this.currentGame = currentGame;
        this.previousPhase = previousPhase;
        this.newPhase = newPhase;
    }

    /**
     * @return Get the current game.
     */
    public KOTHGame getCurrentGame() {
        return currentGame;
    }

    /**
     * @return Get the previous state.
     */
    public GamePhase getPreviousPhase() {
        return previousPhase;
    }

    /**
     * @return Get the next state.
     */
    public GamePhase getNewPhase() {
        return newPhase;
    }
}
