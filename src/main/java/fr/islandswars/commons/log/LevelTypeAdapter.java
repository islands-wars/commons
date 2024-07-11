package fr.islandswars.commons.log;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.logging.Level;

/**
 * File <b>LevelTypeAdapter</b> located on fr.islandswars.commons.log
 * LevelTypeAdapter is a part of commons.
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
 * Created the 11/07/2024 at 19:55
 * @since 0.4.3
 */
public class LevelTypeAdapter implements JsonSerializer<Level> {

    @Override
    public JsonElement serialize(Level level, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(level.getName());
    }
}
