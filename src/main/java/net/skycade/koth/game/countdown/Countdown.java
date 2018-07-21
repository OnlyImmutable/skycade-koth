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

    /** Current time the countdown is on. */
    private int currentTime;

    /** Total duration of the countdown. */
    private int totalDuration;
    /** Intervals of the countdown for messages etc.. */
    private int[] countdownPoints;

    /** Should intervals be called every second rather than at points. */
    private boolean everySecond;

    /** Runnable for the countdown. */
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

    /**
     * Start the countdown.
     * @param intervals - what should happen per interval call.
     * @param finished - what should happen once the countdown finishes.
     */
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

    /**
     * Stop the countdown.
     */
    public void stop() {
        runnable.cancel();
    }

    /**
     * @return Get the countdowns unique ID.
     */
    public String getCountdownId() {
        return countdownId;
    }

    /**
     * @return Get the current time of the countdown.
     */
    public int getCurrentTime() {
        return currentTime;
    }

    /**
     * @return Set the current time of the countdown.
     */
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * @return Get the total duration of the countdown.
     */
    public int getTotalDuration() {
        return totalDuration;
    }

    /**
     * @return Get the countdown points/intervals.
     */
    public int[] getCountdownPoints() {
        return countdownPoints;
    }

    /**
     * @return Set the runnable.
     */
    public void setRunnable(BukkitRunnable runnable) {
        this.runnable = runnable;
    }

    /**
     * @return Get the runnable.
     */
    public BukkitRunnable getRunnable() {
        return runnable;
    }
}
