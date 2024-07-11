package fr.islandswars.commons.service.rabbitmq.packet;

import fr.islandswars.commons.log.IslandsLogger;
import fr.islandswars.commons.network.nio.ByteBufferPool;
import fr.islandswars.commons.network.nio.InputByteBuffer;
import fr.islandswars.commons.utils.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * File <b>PacketManager</b> located on fr.islandswars.commons.service.rabbitmq.packet
 * PacketManager is a part of commons.
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
 * Created the 02/06/2024 at 17:01
 * @since 0.3
 */
public class PacketManager {

    private final ConcurrentMap<Integer, List<PacketEvent<? extends Packet>>> handlers;
    private final ByteBufferPool                                              pool;
    private final PacketType.Bound                                            bound;

    public PacketManager(PacketType.Bound bound, int size, boolean direct) {
        this.pool = new ByteBufferPool(size, direct);
        this.handlers = new ConcurrentHashMap<>();
        this.bound = bound;
    }

    public <T extends Packet> void addListener(PacketType<T> type, PacketEvent<T> event) {
        var id = type.getId();
        if (type.getBound().equals(bound)) {
            IslandsLogger.getLogger().logError(new IllegalArgumentException("Cannot listen to packet with the same Bound " + bound));
            return;
        }
        handlers.compute(id, (k, v) -> {
            if (v == null)
                v = new ArrayList<>();
            v.add(event);
            return v;
        });
    }

    public byte[] encode(Packet packet) throws Exception {
        var output = pool.allocateNetOutput();
        output.writeInt(packet.getId());
        packet.encode(output);
        var result = output.getBuffer();
        pool.free(output);
        return result;
    }

    public <T extends Packet> void decode(byte[] delivery) throws Exception {
        var input = pool.allocateNetInput();
        ((InputByteBuffer) input).getByteBuffer().put(delivery);
        ((InputByteBuffer) input).getByteBuffer().flip();
        var           packetId   = input.readInt();
        PacketType<T> packetType = PacketType.getPacketType(packetId);
        if (packetType != null) {
            T packet = ReflectionUtil.getConstructorAccessor(packetType.getPacketClass()).newInstance();
            packet.decode(input);
            pool.free(input);
            if (handlers.containsKey(packetId)) {
                handlers.get(packetId).forEach(event -> {
                    @SuppressWarnings("unchecked")
                    PacketEvent<T> typedEvent = (PacketEvent<T>) event;
                    typedEvent.receivePacket(packet);
                });
            }
        }
    }
}
