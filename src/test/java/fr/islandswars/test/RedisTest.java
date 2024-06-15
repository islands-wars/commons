package fr.islandswars.test;

import fr.islandswars.commons.service.redis.RedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * File <b>RedisTest</b> located on fr.islandswars.test
 * RedisTest is a part of commons.
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
 * Created the 17/05/2024 at 16:25
 * @since 0.1
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisTest {

    private static RedisConnection                    redisClient;
    private static RedisAsyncCommands<String, String> connection;

    @BeforeAll
    public static void setup() throws Exception {
        redisClient = new RedisConnection();
        redisClient.load();
        redisClient.connect();
        connection = redisClient.getConnection();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        redisClient.close();
    }

    @Test
    @Order(1)
    public void testConnection() throws ExecutionException, InterruptedException {
        assertNotNull(connection, "Database connection should not be null");

        var result = connection.ping().get();
        assertEquals(result, "PONG");
    }

    @Test
    @Order(2)
    public void testSetValue() throws ExecutionException, InterruptedException {
        assertNotNull(connection, "Database connection should not be null");

        var result = connection.set("test", "value").get();
        assertEquals(result, "OK");
    }

    @Test
    @Order(3)
    public void testGetValue() throws ExecutionException, InterruptedException {
        assertNotNull(connection, "Database connection should not be null");

        var result = connection.get("test").get();
        assertEquals(result, "value");
    }
}
