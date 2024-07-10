package fr.islandswars.commons.service.rabbitmq.packet.manager;

import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.service.rabbitmq.packet.Packet;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;
import fr.islandswars.commons.service.rabbitmq.packet.server.StatusRequestPacket;
import fr.islandswars.commons.utils.Preconditions;

import java.util.UUID;

/**
 * File <b>StatusResponsePacket</b> located on fr.islandswars.commons.service.rabbitmq.packet.manager
 * StatusResponsePacket is a part of commons.
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
 * Created the 03/06/2024 at 00:23
 * @since 0.3
 * <p>
 * Sent from the game server to the manager in response to StatusRequestPacket.
 * Only scenario where this packet is sent without a request :
 * - Server load
 * - Server ready
 */
public class StatusResponsePacket extends Packet {

    private UUID                             serverId;
    private StatusRequestPacket.ServerStatus status;

    public StatusResponsePacket() {
        super(PacketType.Status.STATUS_RESPONSE.getId());
    }

    @Override
    public void decode(NetInput input) throws Exception {
        this.serverId = input.readUUID();
        this.status = StatusRequestPacket.ServerStatus.from(input.readInt());
    }

    @Override
    public void encode(NetOutput output) throws Exception {
        Preconditions.checkNotNull(serverId);
        Preconditions.checkNotNull(status);

        output.writeUUID(serverId);
        output.writeInt(status.getId());
    }

    public StatusRequestPacket.ServerStatus getStatus() {
        return status;
    }

    public UUID getServerId() {
        return serverId;
    }

    public StatusResponsePacket withStatus(StatusRequestPacket.ServerStatus status) {
        this.status = status;
        return this;
    }

    public StatusResponsePacket withServerId(UUID serverId) {
        this.serverId = serverId;
        return this;
    }
}
