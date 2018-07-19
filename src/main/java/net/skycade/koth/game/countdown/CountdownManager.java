package net.skycade.koth.game.countdown;

import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.utils.Callback;
import org.bukkit.scheduler.BukkitRunnable;

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

    private SkycadeKoth plugin;

    private Countdown currentCountdown;
    private BukkitRunnable runnable;

    public CountdownManager(SkycadeKoth plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param countdown - the countdown instance.
     * @param intervals - what happens for every interval you specified in countdown.
     * @param finished - what happens once the countdown finishes.
     */
    public void startCountdown(Countdown countdown, Callback<Integer> intervals, Callback<Boolean> finished) {

        if (currentCountdown != null) {
            stopCountdown();
        }

        currentCountdown = countdown;

        runnable = new BukkitRunnable() {

            @Override
            public void run() {

                if (currentCountdown.getCurrentTime() < 1) {
                    finished.call(false);
                    stopCountdown();
                    return;
                }

                for (int interval : countdown.getCountdownPoints()) {
                    if (currentCountdown.getCurrentTime() == interval) {
                        intervals.call(interval);
                    }
                }

                currentCountdown.setCurrentTime(currentCountdown.getCurrentTime() - 1);
            }
        };

        runnable.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * Stop the countdown.
     */
    public void stopCountdown() {

        if (currentCountdown == null) {
            return;
        }

        runnable.cancel();
        currentCountdown = null;
    }
}
