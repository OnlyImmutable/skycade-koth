package net.skycade.koth.utils.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class PlaceholderManager {

    private static List<Placeholder> placeholders;

    static {
        placeholders = new ArrayList<>();
    }

    /**
     * Register a new placeholder into the placeholder cache.
     * @param placeholder - Placeholder.
     * @param value - Placeholder value.
     */
    public static void registerPlaceholder(String placeholder, Object value) {
        if (placeholderExists(placeholder)) {
            return;
        }

        placeholders.add(new Placeholder(placeholder, value));
    }

    /**
     * Replace an input with custom placeholders.
     * @param input - Input
     * @param placeholder - Placeholder.
     * @param value - Placeholder value.
     * @return Input with placeholders replaced.
     */
    public static String replaceCustomPlaceholder(String input, String placeholder, Object value) {

        Pattern pattern = Pattern.compile("(?i)" + placeholder);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            input = matcher.replaceAll(String.valueOf(value));
        }
        return input;
    }

    /**
     * Replace an input with custom placeholders.
     * @param input - Input
     * @param placeholders - Placeholders.
     * @return Input with placeholders replaced.
     */
    public static String replaceCustomPlaceholders(String input, List<Placeholder> placeholders) {
        String output = input;

        if (output.length() < 1) {
            return output;
        }

        for (Placeholder holder : placeholders) {
            Pattern pattern = Pattern.compile("(?i)" + holder.getPlaceholder());
            Matcher matcher = pattern.matcher(output);
            if (matcher.find()) {
                output = matcher.replaceAll(String.valueOf(holder.getValue()));
            }
        }
        return output;
    }

    /**
     * Replace an input with any registered placeholders.
     * @param input - input
     * @return Input with placeholders replaced.
     */
    public static String replaceInputWithRegistredPlaceholders(String input) {
        return replaceCustomPlaceholders(input, placeholders);
    }

    /**
     * Check if a placeholder is registered.
     * @param placeholder - placeholder.
     * @return Is registered.
     */
    private static boolean placeholderExists(String placeholder) {
        for (Placeholder temp : placeholders) {
            if (temp.getPlaceholder().toLowerCase().equalsIgnoreCase(placeholder.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all the placeholders.
     * @return Placeholders.
     */
    public static List<Placeholder> getPlaceholders() {
        return placeholders;
    }
}
