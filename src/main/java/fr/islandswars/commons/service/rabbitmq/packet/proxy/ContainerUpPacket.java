package fr.islandswars.commons.service.rabbitmq.packet.proxy;

import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.service.rabbitmq.packet.Packet;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;
import fr.islandswars.commons.utils.Preconditions;

import java.util.UUID;

/**
 * File <b>ContainerUpPacket</b> located on fr.islandswars.commons.service.rabbitmq.packet.proxy
 * ContainerUpPacket is a part of commons.
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
 * Created the 07/07/2024 at 21:20
 * @since 0.3.1
 * <p>
 * Sent by a manager that has started a new container.
 * The UUID is the key to retrieve container data in redis.
 */
public class ContainerUpPacket extends Packet {

    private UUID proxyId;
    private UUID containerId;

    public ContainerUpPacket() {
        super(PacketType.Status.CONTAINER_UP_REQUEST.getId());
    }

    @Override
    public void decode(NetInput input) throws Exception {
        this.proxyId = input.readUUID();
        this.containerId = input.readUUID();
    }

    @Override
    public void encode(NetOutput output) throws Exception {
        Preconditions.checkNotNull(proxyId);
        Preconditions.checkNotNull(containerId);

        output.writeUUID(proxyId);
        output.writeUUID(containerId);
    }

    public UUID getContainerId() {
        return containerId;
    }

    public UUID getProxyId() {
        return proxyId;
    }

    public void setContainerId(UUID containerId) {
        this.containerId = containerId;
    }

    public void setProxyId(UUID proxyId) {
        this.proxyId = proxyId;
    }
}
