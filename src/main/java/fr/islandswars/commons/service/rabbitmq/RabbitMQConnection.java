package fr.islandswars.commons.service.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import fr.islandswars.commons.secrets.DockerSecretsLoader;
import fr.islandswars.commons.service.ServiceConnection;
import fr.islandswars.commons.service.ServiceType;
import fr.islandswars.commons.utils.LogUtils;
import fr.islandswars.commons.utils.Preconditions;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * File <b>RabbitMQConnection</b> located on fr.islandswars.commons.service.rabbitmq
 * RabbitMQConnection is a part of commons.
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
 * Created the 18/05/2024 at 14:06
 * @since 0.1
 */
public class RabbitMQConnection implements ServiceConnection<Channel> {

    private final AtomicBoolean     status;
    private       ConnectionFactory factory;
    private       Connection        connection;

    public RabbitMQConnection() {
        this.status = new AtomicBoolean(false);
    }

    @Override
    public void close() throws Exception {
        connection.close();
        status.set(false);
    }

    @Override
    public void connect() throws IOException, TimeoutException {
        Preconditions.checkNotNull(factory);

        this.connection = factory.newConnection();
        status.set(true);
    }

    @Override
    public Channel getConnection() {
        if (connection == null) {
            try {
                connect();
                return connection.createChannel();
            } catch (IOException | TimeoutException e) {
                LogUtils.error(new RuntimeException(e));
            }
        } else {
            try {
                return connection.createChannel();
            } catch (IOException e) {
                LogUtils.error(new RuntimeException(e));
            }
        }
        return null;
    }

    @Override
    public boolean isClosed() {
        return status.get();
    }

    @Override
    public void load() throws NullPointerException {
        var host = DockerSecretsLoader.getValue(ServiceType.RMQ_HOSTNAME);
        var port = DockerSecretsLoader.getValue(ServiceType.RMQ_PORT);
        var user = DockerSecretsLoader.getValue(ServiceType.RMQ_USERNAME);
        var pass = DockerSecretsLoader.getValue(ServiceType.RMQ_PASSWORD);

        this.factory = new ConnectionFactory();
        factory.setUsername(user);
        factory.setPassword(pass);
        factory.setHost(host);
        factory.setPort(Integer.decode(port));
    }
}
