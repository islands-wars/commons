package fr.islandswars.commons.service.rabbitmq.packet;

import fr.islandswars.commons.service.rabbitmq.packet.manager.PingResponsePacket;
import fr.islandswars.commons.service.rabbitmq.packet.manager.StatusResponsePacket;
import fr.islandswars.commons.service.rabbitmq.packet.proxy.ContainerDownPacket;
import fr.islandswars.commons.service.rabbitmq.packet.proxy.ContainerUpPacket;
import fr.islandswars.commons.service.rabbitmq.packet.proxy.ProxyDownPacket;
import fr.islandswars.commons.service.rabbitmq.packet.proxy.ProxyUpPacket;
import fr.islandswars.commons.service.rabbitmq.packet.server.PingRequestPacket;
import fr.islandswars.commons.service.rabbitmq.packet.server.StatusRequestPacket;
import fr.islandswars.commons.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

import static fr.islandswars.commons.service.rabbitmq.packet.PacketType.Bound.*;

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
public class PacketType<T extends Packet> {

    protected static final Map<Integer, PacketType<? extends Packet>> packetList = new HashMap<>();

    private final int      id;
    private final Class<T> packet;
    private final Bound    bound;

    public PacketType(int id, Class<T> packet, Bound bound) {
        this.id = id;
        this.packet = packet;
        this.bound = bound;
        if (packetList.containsKey(id))
            LogUtils.error(new IllegalArgumentException("Packet id is already registered."));
        else
            packetList.put(id, this);
    }

    public Bound getBound() {
        return bound;
    }

    public int getId() {
        return id;
    }

    public Class<T> getPacketClass() {
        return packet;
    }

    //SERVER packet are sent by the game server to the manager
    //MANAGER packet are sent by the manager to the game server
    //PROXY packet are sent by one manager to another
    public enum Bound {
        SERVER,
        MANAGER,
        PROXY
    }

    @SuppressWarnings("unchecked")
    public static <T extends Packet> PacketType<T> getPacketType(int id) {
        return (PacketType<T>) packetList.get(id);
    }

    public static final class Status {
        public static final PacketType<ProxyUpPacket>       PROXY_UP_REQUEST       = new PacketType<>(1, ProxyUpPacket.class, PROXY);
        public static final PacketType<ProxyDownPacket>     PROXY_DOWN_REQUEST     = new PacketType<>(2, ProxyDownPacket.class, PROXY);
        public static final PacketType<ContainerUpPacket>   CONTAINER_UP_REQUEST   = new PacketType<>(3, ContainerUpPacket.class, PROXY);
        public static final PacketType<ContainerDownPacket> CONTAINER_DOWN_REQUEST = new PacketType<>(4, ContainerDownPacket.class, PROXY);

        public static final PacketType<PingRequestPacket>  PING_REQUEST  = new PacketType<>(10, PingRequestPacket.class, MANAGER);
        public static final PacketType<PingResponsePacket> PING_RESPONSE = new PacketType<>(11, PingResponsePacket.class, SERVER);

        public static final PacketType<StatusRequestPacket>  STATUS_REQUEST  = new PacketType<>(20, StatusRequestPacket.class, MANAGER);
        public static final PacketType<StatusResponsePacket> STATUS_RESPONSE = new PacketType<>(21, StatusResponsePacket.class, SERVER);
    }
}
