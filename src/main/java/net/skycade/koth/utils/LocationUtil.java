package net.skycade.koth.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

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
public class LocationUtil {

    /**
     * Serialize a location.
     * @param location - location.
     * @return Serialized Location.
     */
    public static String getStringFromLocation(Location location) {
        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ() + ";" + location.getPitch() + ";" + location.getYaw();
    }

    /**
     * Un-serialize a location.
     * @param location - location.
     * @return Un-serialized Location.
     */
    public static Location getLocationFromString(String location) {
        String[] splitString = location.split(";");
        return new Location(Bukkit.getWorld(splitString[0]), Integer.parseInt(splitString[1]), Integer.parseInt(splitString[2]), Integer.parseInt(splitString[3]), Float.parseFloat(splitString[4]), Float.parseFloat(splitString[5]));
    }

    /**
     * Check if a location is within 2 sets of boundaries.
     * @param location - location being checked.
     * @param boundaryPoint1 - boundary point 1.
     * @param boundaryPoint2 - boundary point 2.
     * @return Is within boundaries.
     */
    public static boolean isWithinLocation(Location location, Location boundaryPoint1, Location boundaryPoint2) {

        if (!location.getWorld().getName().equalsIgnoreCase(boundaryPoint1.getWorld().getName())) return false;

        if ((location.getBlockX() >= boundaryPoint1.getBlockX() && location.getBlockX() <= boundaryPoint2.getBlockX()) || (location.getBlockX() <= boundaryPoint1.getBlockX() && location.getBlockX() >= boundaryPoint2.getBlockX())) {
            return (location.getBlockZ() >= boundaryPoint1.getBlockZ() && location.getBlockZ() <= boundaryPoint2.getBlockZ()) || (location.getBlockZ() <= boundaryPoint1.getBlockZ() && location.getBlockZ() >= boundaryPoint2.getBlockZ());
        }

        return false;
    }
}
