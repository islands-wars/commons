package fr.islandswars.commons.service.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import fr.islandswars.commons.log.IslandsLogger;
import fr.islandswars.commons.secrets.DockerSecretsLoader;
import fr.islandswars.commons.service.ServiceConnection;
import fr.islandswars.commons.service.ServiceType;
import fr.islandswars.commons.utils.Preconditions;

import java.io.IOException;

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

    private ConnectionFactory factory;
    private Connection        connection;

    @Override
    public void close() throws Exception {
        if (connection != null)
            connection.close();
    }

    @Override
    public void connect() throws Exception {
        Preconditions.checkNotNull(factory);

        this.connection = factory.newConnection();
    }

    @Override
    public Channel getConnection() {
        Preconditions.checkNotNull(connection, "Need to call RabbitMQConnection#load() first.");

        try {
            return connection.createChannel();
        } catch (IOException e) {
            IslandsLogger.getLogger().logError(e);
        }
        return null;
    }

    @Override
    public boolean isClosed() {
        if (connection != null)
            return !connection.isOpen();
        else return true;
    }

    @Override
    public void load() {
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
