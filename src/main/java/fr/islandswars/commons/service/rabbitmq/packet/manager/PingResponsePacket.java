package fr.islandswars.commons.service.rabbitmq.packet.manager;

import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.service.rabbitmq.packet.Packet;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;
import fr.islandswars.commons.utils.Preconditions;

import java.util.UUID;

/**
 * File <b>PingResponsePacket</b> located on fr.islandswars.commons.service.rabbitmq.packet.manager
 * PingResponsePacket is a part of commons.
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
 * Created the 15/06/2024 at 19:06
 * @since 0.3
 * <p>
 * Sent from the game server to the manager in response to PingRequestPacket.
 * The code should be identical to the request.
 */
public class PingResponsePacket extends Packet {

    private UUID serverId;
    private int  code;

    public PingResponsePacket() {
        super(PacketType.Status.PING_RESPONSE.getId());
    }

    @Override
    public void decode(NetInput input) throws Exception {
        this.serverId = input.readUUID();
        this.code = input.readInt();
    }

    @Override
    public void encode(NetOutput output) throws Exception {
        Preconditions.checkNotNull(serverId);
        Preconditions.checkNotNull(code);

        output.writeUUID(serverId);
        output.writeInt(code);
    }

    public UUID getServerId() {
        return serverId;
    }

    public int getCode() {
        return code;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
