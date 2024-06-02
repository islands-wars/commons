package fr.islandswars.test;

import fr.islandswars.commons.service.rabbitmq.packet.PacketManager;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;
import fr.islandswars.commons.service.rabbitmq.packet.play.PingPacket;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * File <b>PacketTest</b> located on fr.islandswars.test
 * PacketTest is a part of commons.
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
 * Created the 02/06/2024 at 21:00
 * @since 0.3
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PacketTest {

    private static final PacketManager PACKET_MANAGER = new PacketManager(1024, true);
    private              byte[]        delivery;

    @BeforeEach
    public void setup() throws Exception {
        var pingPacket = new PingPacket();
        pingPacket.setTime();
        this.delivery = PACKET_MANAGER.encode(pingPacket);
    }

    @Test
    @Order(1)
    public void createPacket() {
        assertEquals(delivery.length, Integer.BYTES + Long.BYTES, "This packet should be of length 12!");
    }

    @Test
    @Order(2)
    public void retrievePacket() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        PACKET_MANAGER.addListener(PacketType.Status.PING, packet -> {
            try {
                assertEquals("PingPacket", packet.getClass().getSimpleName());
                assertTrue(packet.getTime() < System.currentTimeMillis());
            } finally {
                latch.countDown(); // Decrement the latch count to signal completion
            }
        });
        PACKET_MANAGER.decode(delivery);

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The packet was not processed in time.");

    }

}
