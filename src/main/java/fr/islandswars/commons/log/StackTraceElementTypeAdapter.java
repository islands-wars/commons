package fr.islandswars.commons.log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * File <b>StackTraceElementTypeAdapter</b> located on fr.islandswars.commons.log
 * StackTraceElementTypeAdapter is a part of commons.
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
 * Created the 11/07/2024 at 20:03
 * @since 0.4.3
 */
public class StackTraceElementTypeAdapter extends TypeAdapter<StackTraceElement> {

    @Override
    public void write(JsonWriter out, StackTraceElement value) throws IOException {
        out.beginObject();
        out.name("className").value(value.getClassName());
        out.name("methodName").value(value.getMethodName());
        out.name("fileName").value(value.getFileName());
        out.name("lineNumber").value(value.getLineNumber());
        out.endObject();
    }

    @Override
    public StackTraceElement read(JsonReader in) {
        IslandsLogger.getLogger().logError(new UnsupportedOperationException("Deserialization of StackTraceElement is not supported."));
        return null;
    }
}
