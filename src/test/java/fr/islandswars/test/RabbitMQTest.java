package fr.islandswars.test;

import com.rabbitmq.client.Channel;
import fr.islandswars.commons.service.rabbitmq.RabbitMQConnection;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private static RabbitMQConnection rabbitClient;
    private static Channel            connection;

    @BeforeAll
    public static void setup() throws IOException, TimeoutException {
        rabbitClient = new RabbitMQConnection();
        rabbitClient.load();
        rabbitClient.connect();
        connection = rabbitClient.getConnection();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        rabbitClient.close();
    }

    @Test
    @Order(1)
    public void testConnection() {
        assertNotNull(connection, "Broker connection should not be null");

        var result = connection.isOpen();
        assertTrue(result);
    }
}
