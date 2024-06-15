package fr.islandswars.test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import fr.islandswars.commons.service.rabbitmq.RabbitMQConnection;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * File <b>RabbitMQTest</b> located on fr.islandswars.test
 * RabbitMQTest is a part of commons.
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
 * Created the 18/05/2024 at 14:15
 * @since 0.1
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RabbitMQTest {

    private static final String              QUEUE_NAME    = "TEST";
    private static final String              EXCHANGE_NAME = "TEST_EXCHANGE";
    private static final BuiltinExchangeType EXCHANGE_TYPE = BuiltinExchangeType.FANOUT;
    private static       RabbitMQConnection  rabbitClient;
    private static       Channel             channel;

    @BeforeAll
    public static void setupConnection() throws Exception {
        rabbitClient = new RabbitMQConnection();
        rabbitClient.load();
        rabbitClient.connect();
        channel = rabbitClient.getConnection();
    }

    @AfterAll
    public static void closeConnection() throws Exception {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        rabbitClient.close();
    }

    @BeforeEach
    public void setup() throws Exception {
        // Ensure the queue is declared before each test
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clean up the queue after each test
        if (channel != null && channel.isOpen()) {
            channel.queueDelete(QUEUE_NAME);
            channel.exchangeDelete(EXCHANGE_NAME);
        }
    }

    @Test
    @Order(1)
    public void testConnection() {
        assertNotNull(channel, "Broker connection should not be null");
        assertTrue(channel.isOpen(), "The channel should be open");
    }

    @Test
    @Order(2)
    public void registerConsumer() throws Exception {
        assertNotNull(channel, "Broker connection should not be null");

        final String message = "Hello RabbitMQ!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        var response = channel.basicGet(QUEUE_NAME, true);

        assertEquals(new String(response.getBody(), StandardCharsets.UTF_8), message, "The message received should be the same as the message sent");
    }

    @Test
    @Order(3)
    public void testMultipleConsumers() throws Exception {
        assertNotNull(channel, "Broker connection should not be null");

        final String         message           = "Hello RabbitMQ!";
        final int            numberOfConsumers = 3;
        final CountDownLatch latch             = new CountDownLatch(numberOfConsumers);

        // Create a consumer that counts down the latch
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String receivedMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(receivedMessage);
            assertEquals(message, receivedMessage, "The message received should be the same as the message sent");
            latch.countDown();
        };

        // Register multiple consumers
        for (int i = 0; i < numberOfConsumers; i++) {
            String uniqueQueueName = QUEUE_NAME+"_" + i;
            channel.queueDeclare(uniqueQueueName, false, false, false, null);
            channel.queueBind(uniqueQueueName, EXCHANGE_NAME, "");
            channel.basicConsume(uniqueQueueName, true, deliverCallback, consumerTag -> {});
        }

        // Publish a message to the queue
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));

        // Wait for the latch to count down
        boolean allMessagesReceived = latch.await(5, TimeUnit.SECONDS);
        assertTrue(allMessagesReceived, "All consumers should receive the message");
    }
}
