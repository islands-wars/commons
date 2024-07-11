package fr.islandswars.commons.log;

import fr.islandswars.commons.log.internal.DefaultLog;
import fr.islandswars.commons.log.internal.ErrorLog;
import fr.islandswars.commons.utils.IslandsError;
import fr.islandswars.commons.utils.ReflectionUtil;

import java.util.logging.Level;

/**
 * File <b>IslandsLogger</b> located on fr.islandswars.commons.log
 * IslandsLogger is a part of commons.
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
 * Created the 10/07/2024 at 17:31
 * @since 0.4.3
 */
public abstract class IslandsLogger {

    private static boolean       DEBUG = Boolean.valueOf(System.getenv("DEBUG"));
    private static IslandsLogger logger;
    private final  String        containerName;

    public IslandsLogger(String containerName) {
        if (logger == null)
            logger = this;
        this.containerName = containerName;
    }

    public static IslandsLogger getLogger() {
        return logger;
    }

    public void logInfo(Object message) {
        log(Level.INFO, message.toString());
    }

    public void logDebug(Object message) {
        if (DEBUG)
            log(Level.FINE, message.toString());
    }

    public void logError(Exception e) {
        new ErrorLog(Level.SEVERE, e.getMessage() == null ? "Error" : e.getMessage(), containerName).supplyStacktrace(e.fillInStackTrace()).log();
    }

    public abstract void sysout(Log log);

    public void log(Level level, String msg) {
        if (level.equals(Level.SEVERE))
            logError(new IslandsError(msg));
        else
            new DefaultLog(level, msg, containerName).log();
    }

    public <T extends Log> T createCustomLog(Class<T> clazz, Level level, String message) {
        return ReflectionUtil.getConstructorAccessor(clazz, Level.class, String.class, String.class).newInstance(level, message, containerName);
    }
}
