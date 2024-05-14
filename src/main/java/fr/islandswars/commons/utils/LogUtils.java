package fr.islandswars.commons.utils;

import java.util.function.Consumer;

/**
 * File <b>LogUtils</b> located on fr.islandswars.commons.utils
 * LogUtils is a part of commons.
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
 * Created the 02/05/2024 at 18:05
 * @since 0.1
 */
public class LogUtils {

    private static Consumer<Exception> errorConsumer;

    public static void error(Throwable t) {
        Preconditions.checkNotNull(t);
        error(new DatabaseError(t));
    }

    public static void error(Exception e) {
        Preconditions.checkNotNull(errorConsumer);
        Preconditions.checkNotNull(e);

        errorConsumer.accept(e);
    }

    public static void setErrorConsummer(Consumer<Exception> errorConsummer) {
        LogUtils.errorConsumer = errorConsummer;
    }

}
