package fr.islandswars.commons.service.rabbitmq.packet;

import com.rabbitmq.client.BuiltinExchangeType;
import fr.islandswars.commons.service.rabbitmq.RabbitMQConnection;
import fr.islandswars.commons.utils.LogUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * File <b>IslandsMQ</b> located on fr.islandswars.commons.service.rabbitmq.packet
 * IslandsMQ is a part of commons.
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
 * Created the 02/06/2024 at 16:47
 * @since 0.3
 */
public class IslandsMQ {

    private final String              EXCHANGE_NAME  = "islands";
    private final String              EXCHANGE_FIRST = "server";
    private final String              EXCHANGE_ALL   = "all";
    private final BuiltinExchangeType EXCHANGE_TYPE  = BuiltinExchangeType.TOPIC;
    private final RabbitMQConnection  connection;

    public IslandsMQ(RabbitMQConnection connection) {
        this.connection = connection;
    }

    public void gameServerListen(String serverType, UUID id) {
        var channel = connection.getConnection();
        try {
            var queue = EXCHANGE_FIRST + "." + serverType + "." + id;
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            channel.queueDeclare(queue, false, true, false, null);
            channel.queueBind(queue, EXCHANGE_NAME, queue); //listen to specific message
            channel.queueBind(queue, EXCHANGE_NAME, EXCHANGE_FIRST + "." + serverType + "." + EXCHANGE_ALL); //listen to all same server's type message
            channel.queueBind(queue, EXCHANGE_NAME, EXCHANGE_FIRST + "." + EXCHANGE_ALL); //listen to all servers message

            channel.basicConsume(queue, true, (tag, delivery) -> {
                String receivedMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
            }, consumerTag -> {
            });
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }
}
