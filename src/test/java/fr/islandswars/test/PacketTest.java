package fr.islandswars.test;

import fr.islandswars.commons.service.rabbitmq.packet.PacketManager;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;
import fr.islandswars.commons.service.rabbitmq.packet.server.PingRequestPacket;
import fr.islandswars.commons.utils.LogUtils;
import org.junit.jupiter.api.*;

import java.util.UUID;
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

    private static final PacketManager PACKET_MANAGER = new PacketManager(PacketType.Bound.SERVER, 1024, true);
    private              byte[]        delivery;
    private final        UUID          serverId       = UUID.randomUUID();
    private final        int           code           = 17;


    @BeforeEach
    public void setup() throws Exception {
        var pingPacket = new PingRequestPacket();
        pingPacket.setCode(code);
        pingPacket.setManagerId(serverId);
        this.delivery = PACKET_MANAGER.encode(pingPacket);

        LogUtils.setErrorConsummer(System.err::print);
    }

    @Test
    @Order(1)
    public void createPacket() {
        assertEquals(delivery.length, 2 * Integer.BYTES + 2 * Long.BYTES, "This packet should be of length 24!");
    }

    @Test
    @Order(2)
    public void retrievePacket() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        PACKET_MANAGER.addListener(PacketType.Status.PING_REQUEST, packet -> {
            try {
                assertEquals("PingRequestPacket", packet.getClass().getSimpleName());
                assertEquals(code, packet.getCode());
                assertEquals(serverId, packet.getManagerId());
            } finally {
                latch.countDown(); // Decrement the latch count to signal completion
            }
        });
        PACKET_MANAGER.decode(delivery);

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The packet was not processed in time.");
    }

}
