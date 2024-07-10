package fr.islandswars.commons.service.docker;

/**
 * File <b>ContainerType</b> located on fr.islandswars.commons.service.docker
 * ContainerType is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2024 Islands Wars.
 * <p>
 * commons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">GNU license</a>.
 * <p>
 *
 * @author Jangliu, {@literal <jangliu@islandswars.fr>}
 * Created the 10/07/2024 at 17:18
 * @since 0.4.3
 */
public enum ContainerType {

    ISLANDS(20),
    HUB(50);

    private static final ContainerType[] VALUES = values();
    private final        int             maxPlayerCount;

    ContainerType(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public static ContainerType[] cachedValues() {
        return VALUES;
    }
}
