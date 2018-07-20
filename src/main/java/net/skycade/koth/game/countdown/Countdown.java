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
public class Countdown {

    /** {@link SkycadeKoth} plugin instance. */
    private SkycadeKoth plugin;

    /** ID for the countdown. */
    private String countdownId;

    private int currentTime;

    private int totalDuration;
    private int[] countdownPoints;

    private boolean everySecond;

    private BukkitRunnable runnable;

    public Countdown(SkycadeKoth plugin, String countdownId, int totalDuration, int[] countdownPoints) {
        this.plugin = plugin;
        this.countdownId = countdownId;
        this.totalDuration = totalDuration;
        this.countdownPoints = countdownPoints;

        this.currentTime = totalDuration;
    }

    public Countdown(SkycadeKoth plugin, String countdownId, int totalDuration, boolean everySecond) {
        this.plugin = plugin;
        this.countdownId = countdownId;
        this.totalDuration = totalDuration;
        this.everySecond = everySecond;
        this.currentTime = totalDuration;
    }

    public void start(Callback<Integer> intervals, Callback<Boolean> finished) {

        runnable = new BukkitRunnable() {

            @Override
            public void run() {

                if (getCurrentTime() < 1) {
                    finished.call(false);
                    runnable.cancel();
                    return;
                }

                if (!everySecond) {
                    for (int interval : getCountdownPoints()) {
                        if (getCurrentTime() == interval) {
                            intervals.call(interval);
                        }
                    }
                }

                setCurrentTime(getCurrentTime() - 1);

                if (everySecond) {
                    intervals.call(getCurrentTime());
                }
            }
        };

        runnable.runTaskTimer(plugin, 0L, 20L);
    }

    public void stop() {
        runnable.cancel();
    }

    public String getCountdownId() {
        return countdownId;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public int[] getCountdownPoints() {
        return countdownPoints;
    }

    public void setRunnable(BukkitRunnable runnable) {
        this.runnable = runnable;
    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }
}
