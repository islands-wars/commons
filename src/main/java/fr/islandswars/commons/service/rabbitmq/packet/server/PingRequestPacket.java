package fr.islandswars.commons.service.rabbitmq.packet.server;

import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.service.rabbitmq.packet.Packet;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;
import fr.islandswars.commons.utils.Preconditions;

import java.util.UUID;

/**
 * File <b>PingRequestPacket</b> located on fr.islandswars.commons.service.rabbitmq.packet.server
 * PingRequestPacket is a part of commons.
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
 * Created the 15/06/2024 at 19:00
 * @since 0.3
 * <p>
 * Sent from the manager to the game server to request a PingResponsePacket.
 * The code should be sent back by the response.
 */
public class PingRequestPacket extends Packet {

    private UUID managerId;
    private int  code;

    public PingRequestPacket() {
        super(PacketType.Status.PING_REQUEST.getId());
    }

    @Override
    public void decode(NetInput input) throws Exception {
        this.managerId = input.readUUID();
        this.code = input.readInt();
    }

    @Override
    public void encode(NetOutput output) throws Exception {
        Preconditions.checkNotNull(managerId);
        Preconditions.checkNotNull(code);

        output.writeUUID(managerId);
        output.writeInt(code);
    }

    public UUID getManagerId() {
        return managerId;
    }

    public int getCode() {
        return code;
    }

    public void setManagerId(UUID managerId) {
        this.managerId = managerId;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
