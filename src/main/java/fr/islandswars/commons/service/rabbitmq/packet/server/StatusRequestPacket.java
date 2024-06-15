package fr.islandswars.commons.service.rabbitmq.packet.server;

import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.service.rabbitmq.packet.Packet;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;
import fr.islandswars.commons.utils.Preconditions;

import java.util.Arrays;
import java.util.UUID;

/**
 * File <b>StatusRequestPacket</b> located on fr.islandswars.commons.service.rabbitmq.packet.server
 * StatusRequestPacket is a part of commons.
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
 * Created the 03/06/2024 at 00:20
 * @since 0.3
 * <p>
 * Sent from the manager to the game server to request a StatusResponsePacket.
 * If the status is different from the current server status, updates it.
 */
public class StatusRequestPacket extends Packet {

    private UUID                             serverId;
    private StatusRequestPacket.ServerStatus status;

    public StatusRequestPacket() {
        super(PacketType.Status.STATUS_REQUEST.getId());
    }

    @Override
    public void decode(NetInput input) throws Exception {
        this.serverId = input.readUUID();
        this.status = ServerStatus.from(input.readInt());
    }

    @Override
    public void encode(NetOutput output) throws Exception {
        Preconditions.checkNotNull(serverId);
        Preconditions.checkNotNull(status);

        output.writeUUID(serverId);
        output.writeInt(status.getId());
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public UUID getServerId() {
        return serverId;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public enum ServerStatus {
        STARTED(1), //sent in onLoad method
        READY(2), //sent in onEnable method
        ENABLE(3), //ready to accept players
        DISABLE(4), //will no longer accept new players
        SHUTDOWN(5); //server is stopping

        private final int id;

        ServerStatus(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ServerStatus from(int id) {
            return Arrays.stream(ServerStatus.values()).filter(s -> s.id == id).findFirst().orElse(ServerStatus.SHUTDOWN);
        }
    }
}
