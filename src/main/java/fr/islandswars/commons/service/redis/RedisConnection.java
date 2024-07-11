package fr.islandswars.commons.service.redis;

import fr.islandswars.commons.log.IslandsLogger;
import fr.islandswars.commons.secrets.DockerSecretsLoader;
import fr.islandswars.commons.service.ServiceConnection;
import fr.islandswars.commons.service.ServiceType;
import fr.islandswars.commons.utils.Preconditions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.concurrent.ExecutionException;

/**
 * File <b>RedisConnection</b> located on fr.islandswars.commons.service.redis
 * RedisConnection is a part of commons.
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
 * Created the 17/05/2024 at 14:51
 * @since 0.1
 */
public class RedisConnection implements ServiceConnection<RedisAsyncCommands<String, String>> {

    private RedisURI                                settings;
    private StatefulRedisConnection<String, String> connection;
    private RedisAsyncCommands<String, String>      executor;

    @Override
    public void close() throws Exception {
        if (executor != null)
            executor.quit().get();
        if (connection != null)
            connection.close();
    }

    @Override
    public void connect() throws Exception {
        Preconditions.checkNotNull(settings);

        RedisClient client = RedisClient.create(settings);
        this.connection = client.connect();
        this.executor = connection.async();
    }

    @Override
    public RedisAsyncCommands<String, String> getConnection() {
        Preconditions.checkNotNull(executor, "Need to call RedisConnection#load() first.");

        return executor;
    }

    @Override
    public boolean isClosed() {
        if (executor != null) {
            try {
                return !executor.ping().get().equals("PONG");
            } catch (InterruptedException | ExecutionException e) {
                IslandsLogger.getLogger().logError(e);
                return true;
            }
        } else
            return true;
    }

    @Override
    public void load() {
        var host = DockerSecretsLoader.getValue(ServiceType.REDIS_HOSTNAME);
        var port = DockerSecretsLoader.getValue(ServiceType.REDIS_PORT);
        var user = DockerSecretsLoader.getValue(ServiceType.REDIS_USERNAME);
        var pass = DockerSecretsLoader.getValue(ServiceType.REDIS_PASSWORD);

        this.settings = RedisURI.Builder.redis(host, Integer.decode(port)).withAuthentication(user, pass.toCharArray()).build();
    }
}
