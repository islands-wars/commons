package fr.islandswars.commons.utils;

import java.util.function.Function;

/**
 * File <b>Preconditions</b> located on fr.islandswars.commons.utils
 * Preconditions is a part of commons.
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
 * Created the 04/04/2024 at 16:17
 * @since 0.1
 */
public class Preconditions {

    /**
     * throw exception if the given reference is null (useless for primitive var)
     *
     * @param reference a reference to check
     * @param <T>       ref type
     */
    public static <T> void checkNotNull(T reference) {
        if (reference == null)
            throw new NullPointerException("Given reference is null");
    }

    /**
     * Test the given label (must be not null) to see if it match the function
     *
     * @param reference a ref to test
     * @param check     a function (return true means everything is ok)
     * @param <T>       ref type
     */
    public static <T> void checkState(T reference, Function<T, Boolean> check) {
        checkState(reference, check, "The given reference does not corresponding to the function");
    }

    /**
     * Test the given label (must be not null) to see if it match the function
     *
     * @param reference    a ref to test
     * @param check        a function (return true means everything is ok)
     * @param errorMessage a custom error message to log
     * @param <T>          ref type
     */
    public static <T> void checkState(T reference, Function<T, Boolean> check, String errorMessage) {
        checkNotNull(reference);
        if (!check.apply(reference))
            throw new UnsupportedOperationException(errorMessage);
    }

    /**
     * Test the given reference (must be not null) to see if it match the label (must be not null)
     *
     * @param reference a reference to test
     * @param to        a label to compare to
     * @param <T>       ref type
     */
    public static <T> void checkValue(T reference, T to) {
        checkValue(reference, to, "The reference is not equals to the given label, " + to);
    }

    /**
     * Test the given reference (must be not null) to see if it match the label (must be not null)
     *
     * @param reference    a reference to test
     * @param to           a label to compare to
     * @param errorMessage a custom error message to log
     * @param <T>          ref type
     * @see Object#equals(Object)
     */
    public static <T> void checkValue(T reference, T to, String errorMessage) {
        checkNotNull(reference);
        checkNotNull(to);
        if (!reference.equals(to))
            throw new UnsupportedOperationException(errorMessage);
    }
}

