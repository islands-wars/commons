package fr.islandswars.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.islandswars.commons.log.IslandsLogger;
import fr.islandswars.commons.log.LevelTypeAdapter;
import fr.islandswars.commons.log.Log;
import fr.islandswars.commons.log.StackTraceElementTypeAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

/**
 * File <b>LogTest</b> located on fr.islandswars.test
 * LogTest is a part of commons.
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
 * Created the 11/07/2024 at 19:45
 * @since 0.4.3
 */
public class LogTest {

    private static final String  CONTAINER_NAME = "is";
    private static       LogImpl impl;

    @BeforeAll
    public static void init() {
        impl = new LogImpl(CONTAINER_NAME);
    }

    @Test
    public void testLogInfo() {
        IslandsLogger.getLogger().logInfo("coucou");

        Assertions.assertTrue(impl.buffer.contains("\"message\":\"coucou\""));
        Assertions.assertTrue(impl.buffer.contains("\"level\":\"INFO\""));
    }

    @Test
    public void testLogError() {
        try {
            // one of the most beautiful unit test written by man kind
            int i = 1/0;
        } catch(Exception e) {
            IslandsLogger.getLogger().logError(e);
        }

        Assertions.assertTrue(impl.buffer.contains("\"level\":\"SEVERE\""));
        Assertions.assertTrue(impl.buffer.contains("\"message\":\"/ by zero\""));
    }

    private static class LogImpl extends IslandsLogger {

        private       String buffer;
        private final Gson   gson;

        public LogImpl(String containerName) {
            super(containerName);
            this.gson = new GsonBuilder()
                    .registerTypeAdapter(Level.class, new LevelTypeAdapter())
                    .registerTypeAdapter(StackTraceElement.class, new StackTraceElementTypeAdapter()).create();
        }

        @Override
        public void sysout(Log log) {
            buffer = gson.toJson(log);
        }

        public String getBuffer() {
            return buffer;
        }
    }
}