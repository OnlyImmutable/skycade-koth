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

    private KOTHGame currentGame;
    private GamePhase previousPhase;
    private GamePhase newPhase;

    public PhaseChangeEvent(KOTHGame currentGame, GamePhase previousPhase, GamePhase newPhase) {
        this.currentGame = currentGame;
        this.previousPhase = previousPhase;
        this.newPhase = newPhase;
    }

    public KOTHGame getCurrentGame() {
        return currentGame;
    }

    public GamePhase getPreviousPhase() {
        return previousPhase;
    }

    public GamePhase getNewPhase() {
        return newPhase;
    }
}
