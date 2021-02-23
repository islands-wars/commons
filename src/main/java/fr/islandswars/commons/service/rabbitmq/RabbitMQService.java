package fr.islandswars.commons.service.rabbitmq;

import com.mongodb.annotations.ThreadSafe;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import fr.islandswars.commons.service.Key;
import fr.islandswars.commons.service.Service;
import java.util.Map;

/**
 * File <b>RabbitMQService</b> located on fr.islandswars.commons.service.rabbitmq
 * RabbitMQService is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2021 Islands Wars.
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
 * @author Valentin Burgaud (Xharos), {@literal <xharos@islandswars.fr>}
 * Created the 23/02/2021 at 10:10
 * @since 0.1
 */
@ThreadSafe
public class RabbitMQService extends Service<Channel> {

	private volatile boolean    close;
	private          Connection connection;

	public RabbitMQService() {
		this.close = false;
	}

	@Override
	public void close() throws Exception {
		close = true;
	}

	@Override
	public void connect() throws Exception {
		var factory = new ConnectionFactory();

		factory.setHost(serverAddress.get(0).getHostName());
		factory.setPort(serverAddress.get(0).getPort());
		factory.setVirtualHost(serviceName);
		factory.setUsername(userName);
		factory.setPassword(password);

		this.connection = factory.newConnection();
	}

	@Override
	public Channel getConnection() {
		if (connection == null)
			try {
				connect();
				return close ? null : connection.createChannel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		else try {
			return close ? null : connection.createChannel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isClosed() {
		return connection.isOpen();
	}

	@Override
	public void load(Map<String, String> properties) throws NullPointerException {
		var username = properties.get(RabbitMQService.Type.RABBIT_USER.getKey());
		var password = properties.get(RabbitMQService.Type.RABBIT_PASS.getKey());
		var database = properties.get(RabbitMQService.Type.RABBIT_VH.getKey());
		var host     = properties.get(RabbitMQService.Type.RABBIT_HOST.getKey());
		var port     = Integer.parseInt(properties.get(Type.RABBIT_PORT.getKey()));
		load(port, host, username, password, database);
	}

	/**
	 * Proceed to a basic publish to a known topics.
	 *
	 * @param buffer       a message to send
	 * @param exchangeName the topic name to notify
	 * @param routingKey   a specific routing regex
	 * @throws Exception which has already been logged
	 */
	public void notifyTopic(StringBuffer buffer, String exchangeName, String routingKey) throws Exception {
		if (!close) {
			var channel = getConnection();
			try {
				channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
				channel.basicPublish(exchangeName, routingKey, null, buffer.toString().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * Proceed to a basic publish to a known channel.
	 *
	 * @param buffer    a message to send
	 * @param queueName a specific queue to notify
	 * @throws Exception which has already been logged
	 */
	public void publishToAKnownQueue(StringBuffer buffer, String queueName) throws Exception {
		if (!close) {
			var channel = getConnection();
			try {
				channel.queueDeclare(queueName, false, false, false, null);
				channel.basicPublish("", queueName, null, buffer.toString().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * Register a Consummer for an existing and open
	 * {@link Channel} with the following properties :
	 * - this queue will not survive a server restart
	 * - this queue is not restricted to this connection
	 * - this queue will be deleted when no longer in use
	 *
	 * @param handler       a basic {@link com.rabbitmq.client.Consumer} to handle message
	 * @param autoAck       if you want to acknowledge yourself
	 * @param prefetchCount maximum number of messages that the server will deliver, 0 if unlimited
	 * @throws Exception if error is thrown
	 */
	public void registerConsumer(ConsumerHandler handler, boolean autoAck, int prefetchCount) throws Exception {
		if (!close) {
			var channel = getConnection();
			try {
				handler.channel = channel;
				channel.queueDeclare(handler.id, false, false, false, null);
				channel.basicQos(prefetchCount);
				handler.tag = channel.basicConsume(handler.id, autoAck, handler);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * Register a consumer with a prefetchCount set to unlimited
	 *
	 * @param handler a basic {@link com.rabbitmq.client.Consumer} to handle message
	 * @param autoAck if you want to acknowledge yourself
	 * @throws Exception if errors is thrown
	 * @see #registerConsumer(ConsumerHandler, boolean, int)
	 */
	public void registerConsumer(ConsumerHandler handler, boolean autoAck) throws Exception {
		if (!close)
			registerConsumer(handler, autoAck, 0);
	}

	/**
	 * Register a Consummer for an existing and open
	 * {@link Channel} with the following properties :
	 * - this queue will not survive a server restart
	 * - this queue is not restricted to this connection (only one consummer)
	 * - this queue will be deleted when no longer in use
	 * - This queue name is generated by {@link com.rabbitmq.client.AMQP.Queue.DeclareOk} operation
	 * - This queue will only handle message according to this routing keys regex
	 *
	 * @param handler a basic {@link com.rabbitmq.client.Consumer} to handle message
	 * @param topics  topics to publish to
	 * @throws Exception which has already been logged
	 */
	public void registerTopic(ConsumerHandler handler, String... topics) throws Exception {
		if (!close) {
			var channel = getConnection();
			try {
				handler.channel = channel;
				var queue = channel.queueDeclare().getQueue();
				channel.exchangeDeclare(handler.id, BuiltinExchangeType.TOPIC);
				for (var topic : topics)
					channel.queueBind(queue, handler.id, topic);
				handler.tag = channel.basicConsume(queue, true, handler);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	private enum Type implements Key {

		RABBIT_USER(),
		RABBIT_PASS(),
		RABBIT_VH(),
		RABBIT_HOST(),
		RABBIT_PORT();

		@Override
		public String getKey() {
			return name();
		}
	}
}
