package fr.islandswars.commons.service.redis.cache;

import fr.islandswars.commons.service.redis.RedisConnection;
import fr.islandswars.commons.utils.DatabaseError;
import fr.islandswars.commons.utils.LogUtils;
import fr.islandswars.commons.utils.Preconditions;
import io.lettuce.core.api.async.RedisAsyncCommands;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * File <b>CacheManager</b> located on fr.islandswars.commons.service.redis.cache
 * CacheManager is a part of commons.
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
 * Created the 09/07/2024 at 19:18
 * @since 0.4
 */
public abstract class CacheManager {

    private final RedisAsyncCommands<String, String> redis;
    private final String                               keyPrefix;
    private final Object2ObjectMap<String, EntryCache> fieldsCache;

    public CacheManager(String keyPrefix, RedisConnection connection) {
        this.redis = connection.getConnection();
        this.keyPrefix = keyPrefix;
        this.fieldsCache = new Object2ObjectOpenHashMap<>();
        initializeFieldCache();
        updateCache().run();
    }

    //only one thread can update a value at a time
    protected synchronized void update(Object value, String name) {
        EntryCache entryCache = fieldsCache.get(name);
        if (entryCache != null) {
            redis.set(entryCache.redisKey, value.toString()).whenCompleteAsync((re, th) -> {
                if (th != null) {
                    LogUtils.error(new DatabaseError("Cannot set the cache on redis", th));
                }
            });
            try {
                entryCache.fieldLock.lock();
                try {
                    entryCache.field.set(this, value);
                } finally {
                    entryCache.fieldLock.unlock();
                }
                entryCache.timestamp = System.currentTimeMillis();
            } catch (IllegalAccessException e) {
                LogUtils.error(e);
            }
        }
    }

    public Runnable updateCache() {
        return () -> {
            long currentTimeMillis = System.currentTimeMillis();
            fieldsCache.forEach((fieldName, cache) -> {
                if ((currentTimeMillis - cache.timestamp) >= cache.delta) {
                    fetchFromRedisAndUpdateLocalCache(cache, currentTimeMillis);
                }
            });
        };
    }

    private void fetchFromRedisAndUpdateLocalCache(EntryCache entryCache, long currentTimeMillis) {
        redis.get(entryCache.redisKey).whenCompleteAsync((re, th) -> {
            if (th != null)
                LogUtils.error(new DatabaseError("Canno't retrieve key from cache", th));
            else if (re != null) {
                var value = convertToType(re, entryCache.field.getType());
                try {
                    entryCache.fieldLock.lock();
                    try {
                        entryCache.field.set(this, value);
                    } finally {
                        entryCache.fieldLock.unlock();
                    }
                    entryCache.timestamp = currentTimeMillis;
                } catch (IllegalAccessException e) {
                    LogUtils.error(e);
                }
            }
        });
    }

    private Object convertToType(String valueStr, Class<?> type) {
        if (valueStr == null || valueStr.isEmpty()) {
            return getDefaultValue(type);
        }
        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(valueStr);
        }
        if (type == Long.class || type == long.class) {
            return Long.parseLong(valueStr);
        }
        if (type == Double.class || type == double.class) {
            return Double.parseDouble(valueStr);
        }
        if (type == Float.class || type == float.class) {
            return Float.parseFloat(valueStr);
        }
        if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(valueStr);
        }
        if (type == UUID.class) {
            return UUID.fromString(valueStr);
        }
        return valueStr; // Default to String type
    }

    private Object getDefaultValue(Class<?> type) {
        if (type == Integer.class || type == int.class) {
            return 0;
        }
        if (type == Long.class || type == long.class) {
            return 0L;
        }
        if (type == Double.class || type == double.class) {
            return 0.0;
        }
        if (type == Float.class || type == float.class) {
            return 0.0f;
        }
        if (type == Boolean.class || type == boolean.class) {
            return false;
        }
        return null; // Default for String or unknown types
    }

    private String getKey(String keySuffix) {
        Preconditions.checkNotNull(keySuffix);

        return keyPrefix + (keySuffix.isEmpty() ? "" : keySuffix);
    }

    private void initializeFieldCache() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RedisCache.class)) {
                RedisCache cache = field.getAnnotation(RedisCache.class);
                field.setAccessible(true);
                fieldsCache.put(field.getName(), new EntryCache(field, getKey(cache.keySuffix()), cache.time(), 0));
            }
        }
    }

    protected static class EntryCache {
        protected volatile long   timestamp; //sufficient for read/write operations
        protected final    int    delta;
        protected final    String redisKey;
        protected final    Field  field;
        protected final    Lock   fieldLock;

        public EntryCache(Field field, String redisKey, int delta, long timestamp) {
            this.field = field;
            this.redisKey = redisKey;
            this.delta = delta;
            this.timestamp = timestamp;
            this.fieldLock = new ReentrantLock();
        }
    }
}
