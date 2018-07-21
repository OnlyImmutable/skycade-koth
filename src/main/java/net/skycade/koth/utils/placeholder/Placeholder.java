package net.skycade.koth.utils.placeholder;

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
public class Placeholder {

    /** Placeholder that will be replaced. */
    private String placeholder;
    /** Placeholder value which will be the value of a placeholder. */
    private Object value;

    /**
     * Crate a new {@link Placeholder} instance.
     * @param placeholder - placeholder.
     * @param value - placeholder value.
     */
    public Placeholder(String placeholder, Object value) {
        this.placeholder = placeholder;
        this.value = value;
    }

    /**
     * @return Placeholder
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Value for the placeholder.
     * @return Placeholder value.
     */
    public Object getValue() {
        return value;
    }
}
