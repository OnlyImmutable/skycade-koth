package net.skycade.koth.game.countdown;

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

    private int currentTime;

    private int totalDuration;
    private int[] countdownPoints;

    public Countdown(int totalDuration, int[] countdownPoints) {
        this.totalDuration = totalDuration;
        this.countdownPoints = countdownPoints;

        this.currentTime = totalDuration;
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
}
