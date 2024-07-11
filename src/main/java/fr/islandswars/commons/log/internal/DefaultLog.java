package fr.islandswars.commons.log.internal;

import fr.islandswars.commons.log.Log;
import fr.islandswars.commons.utils.Preconditions;

import java.util.logging.Level;

/**
 * File <b>DefaultLog</b> located on fr.islandswars.commons.log.internal
 * DefaultLog is a part of commons.
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
 * Created the 10/07/2024 at 17:42
 * @since 0.4.3
 */
public class DefaultLog extends Log {

    public DefaultLog(Level level, String msg, String containerName) {
        super(level, msg, containerName);
    }

    @Override
    protected void checkValue() {
        Preconditions.checkNotNull(level);
        Preconditions.checkNotNull(msg);
        Preconditions.checkNotNull(date);
        Preconditions.checkNotNull(containerName);
    }
}
