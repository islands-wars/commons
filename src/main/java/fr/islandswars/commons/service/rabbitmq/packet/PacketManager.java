package fr.islandswars.commons.service.rabbitmq.packet;

import fr.islandswars.commons.network.nio.ByteBufferPool;
import fr.islandswars.commons.network.nio.InputByteBuffer;
import fr.islandswars.commons.utils.ReflectionUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private final Map<Integer, List<PacketEvent<? extends Packet>>> handlers;
    private final ByteBufferPool                                    pool;

    public PacketManager(int size, boolean direct) {
        this.pool = new ByteBufferPool(size, direct);
        this.handlers = new ConcurrentHashMap<>();
    }

    public <T extends Packet> void addListener(PacketType type, PacketEvent<T> event) {
        var id = type.getId();
        handlers.compute(id, (k, v) -> {
            if (v == null)
                v = new CopyOnWriteArrayList<>();
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

    public void decode(byte[] delivery) throws Exception {
        var input = pool.allocateNetInput();
        ((InputByteBuffer) input).getByteBuffer().put(delivery);
        ((InputByteBuffer) input).getByteBuffer().flip();
        var packetId = input.readInt();
        if (PacketType.packetList.containsKey(packetId)) {
            Class<? extends Packet> packetClass = PacketType.packetList.get(packetId);
            Packet                  packet      = ReflectionUtil.getConstructorAccessor(packetClass).newInstance();
            packet.decode(input);
            pool.free(input);
            if (handlers.containsKey(packetId)) {
                handlers.get(packetId).forEach(event -> {
                    @SuppressWarnings("unchecked")
                    PacketEvent<Packet> typedEvent = (PacketEvent<Packet>) event;
                    typedEvent.receivePacket(packet);
                });
            }
        }
    }
}
