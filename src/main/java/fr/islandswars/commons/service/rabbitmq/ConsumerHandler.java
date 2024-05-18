package fr.islandswars.commons.service.rabbitmq;

import com.rabbitmq.client.*;
import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.nio.InputByteBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * File <b>ConsumerHandler</b> located on fr.islandswars.commons.service.rabbitmq
 * ConsumerHandler is a part of commons.
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
 * Created the 18/05/2024 at 21:50
 * @since 0.1
 */
public abstract class ConsumerHandler implements Consumer {

    protected final String  id;
    protected       String  tag;
    protected       Channel channel;

    public ConsumerHandler(String queueName) {
        this.id = queueName;
    }

    @Override
    public void handleConsumeOk(String consumerTag) {

    }

    @Override
    public void handleCancelOk(String consumerTag) {

    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {

    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        var buffer = new InputByteBuffer(ByteBuffer.wrap(body));
        handleDelivery(envelope, buffer);
    }

    public abstract void handleDelivery(Envelope envelope, NetInput output);

}
