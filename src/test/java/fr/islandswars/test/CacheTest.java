package fr.islandswars.test;

import fr.islandswars.commons.service.redis.RedisConnection;
import fr.islandswars.commons.service.redis.cache.CacheManager;
import fr.islandswars.commons.service.redis.cache.RedisCache;
import fr.islandswars.commons.utils.LogUtils;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * File <b>TestCache</b> located on fr.islandswars.test
 * TestCache is a part of commons.
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
 * Created the 09/07/2024 at 19:34
 * @since 0.4
 */
public class CacheTest {

    private final  String                   PREFIX = "test:";
    private final  String                   SUFFIX = "id";
    private static RedisConnection          redisClient;
    private        ScheduledExecutorService executor;
    private        CacheExample             manager;

    @BeforeAll
    public static void setup() throws Exception {
        redisClient = new RedisConnection();
        redisClient.load();
        redisClient.connect();
        LogUtils.setErrorConsummer((e) -> e.printStackTrace());
    }

    @AfterAll
    public static void tearDown() throws Exception {
        redisClient.close();
    }

    @BeforeEach
    public void setupEach() {
        executor = Executors.newScheduledThreadPool(1);
        this.manager = new CacheExample(PREFIX, redisClient);
        executor.scheduleAtFixedRate(manager.updateCache(), 0, 1, TimeUnit.MILLISECONDS);
    }

    @AfterEach
    public void tearDownEach() throws Exception {
        executor.shutdownNow();
        executor.awaitTermination(100, TimeUnit.MILLISECONDS);
        redisClient.getConnection().del(PREFIX + SUFFIX).get();
    }

    @Test
    public void testLocalUpdateIsInstant() {
        var i = 2;
        manager.setUuid(i);
        assertEquals(i, manager.getUuid());
    }

    @Test
    public void testRemoteUpdateMaj() throws ExecutionException, InterruptedException {
        var i    = 2;
        var newI = 4;
        manager.setUuid(i);
        assertEquals(i, manager.getUuid());
        redisClient.getConnection().set(PREFIX + SUFFIX, String.valueOf(newI)).get();
        Thread.sleep(10);
        assertEquals(newI, manager.getUuid());
    }

    @Test
    public void testRemoteUpdate() throws ExecutionException, InterruptedException {
        var i = 3;
        assertEquals(0, manager.getUuid());
        redisClient.getConnection().set(PREFIX + SUFFIX, String.valueOf(i)).get();
        Thread.sleep(10);
        assertEquals(i, manager.getUuid());
    }

    @Test
    public void testTimeBetweenUpdate() throws ExecutionException, InterruptedException {
        var i = 5;
        assertEquals(0, manager.getUuid());
        redisClient.getConnection().set(PREFIX + SUFFIX, String.valueOf(i)).get();
        assertEquals(0, manager.getUuid());
    }

    private class CacheExample extends CacheManager {

        @RedisCache(time = 5, keySuffix = "id")
        private int uuid;

        public CacheExample(String keyPrefix, RedisConnection connection) {
            super(keyPrefix, connection);
        }

        public int getUuid() {
            return uuid;
        }

        public void setUuid(int uuid) {
            update(uuid, "uuid");
        }
    }
}
