package net.skycade.koth.game.countdown;

import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.utils.Callback;

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
public class CountdownManager {

    /** Cache of countdowns. */
    private Map<String, Countdown> currentCountdowns;

    public CountdownManager() {
        this.currentCountdowns = new HashMap<>();
    }

    /**
     * Start a countdown.
     * @param countdown - countdown instance.
     * @param intervals - what should happen per interval call.
     * @param finished - what should happen once the countdown finishes.
     */
    public void startCountdown(Countdown countdown, Callback<Integer> intervals, Callback<Boolean> finished) {
        if (currentCountdowns.containsKey(countdown.getCountdownId())) {
            return;
        }

        countdown.start(intervals, finished);
        currentCountdowns.put(countdown.getCountdownId(), countdown);
    }

    /**
     * Remove a countdown.
     * @param countdownId - unique countdown id.
     */
    public void removeCountdown(String countdownId) {
        currentCountdowns.remove(countdownId);
    }

    /**
     * Get a countdowns instance.
     * @param countdownId - countdown id.
     * @return Countdown
     */
    public Countdown getCountdown(String countdownId) {
        return currentCountdowns.get(countdownId);
    }
}
