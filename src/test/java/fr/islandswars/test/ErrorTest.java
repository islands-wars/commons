package fr.islandswars.test;

import fr.islandswars.commons.utils.DatabaseError;
import fr.islandswars.commons.utils.LogUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * File <b>ErrorTest</b> located on fr.islandswars.test
 * ErrorTest is a part of commons.
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
 * Created the 27/05/2024 at 18:51
 * @since 0.2.6
 */
public class ErrorTest {

    private final String MESSAGE = "Custom message";

    @Test
    public void testError() {
        LogUtils.setErrorConsummer((e)-> {
            assertEquals(e.getMessage(), MESSAGE, "Error message should be the same");
        });
        LogUtils.error(new DatabaseError("Custom message",new Throwable()));
    }
}
