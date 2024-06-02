package fr.islandswars.commons.service.rabbitmq.packet;

import fr.islandswars.commons.service.rabbitmq.packet.in.StatusInPacket;
import fr.islandswars.commons.service.rabbitmq.packet.out.StatusOutPacket;
import fr.islandswars.commons.service.rabbitmq.packet.play.PingPacket;
import fr.islandswars.commons.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

import static fr.islandswars.commons.service.rabbitmq.packet.PacketType.Bound.IN;
import static fr.islandswars.commons.service.rabbitmq.packet.PacketType.Bound.OUT;

/**
 * File <b>PacketType</b> located on fr.islandswars.commons.service.rabbitmq.packet
 * PacketType is a part of commons.
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
 * Created the 02/06/2024 at 16:46
 * @since 0.3
 */
public class PacketType {

    protected static final Map<Integer, Class<? extends Packet>> packetList = new HashMap<>();

    private final int                     id;
    private final Class<? extends Packet> packet;
    private final Bound                   bound;

    public PacketType(int id, Class<? extends Packet> packet, Bound bound) {
        this.id = id;
        this.packet = packet;
        this.bound = bound;
        if (packetList.containsKey(id))
            LogUtils.error(new IllegalArgumentException("Packet id is already registered."));
        else
            packetList.put(id, packet);
    }

    public Bound getBound() {
        return bound;
    }

    public int getId() {
        return id;
    }

    public Class<? extends Packet> getPacketClass() {
        return packet;
    }

    public enum Bound {
        IN,
        OUT;
    }

    public static final class Status {
        public static final PacketType PING       = new PacketType(1, PingPacket.class, IN);
        public static final PacketType STATUS_IN  = new PacketType(2, StatusInPacket.class, IN);
        public static final PacketType STATUS_OUT = new PacketType(3, StatusOutPacket.class, OUT);
    }
}
