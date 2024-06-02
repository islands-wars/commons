package fr.islandswars.commons.service.rabbitmq.packet.out;

import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.service.rabbitmq.packet.Packet;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;
import fr.islandswars.commons.utils.Preconditions;

import java.util.Arrays;
import java.util.UUID;

/**
 * File <b>StatusOutPacket</b> located on fr.islandswars.commons.service.rabbitmq.packet.out
 * StatusOutPacket is a part of commons.
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
 */
public class StatusOutPacket extends Packet {

    private UUID         serverId;
    private int          onlinePlayers;
    private ServerStatus status;


    public StatusOutPacket() {
        super(PacketType.Status.STATUS_OUT.getId());
    }

    @Override
    public void decode(NetInput input) throws Exception {
        this.serverId = input.readUUID();
        this.onlinePlayers = input.readInt();
        this.status = ServerStatus.from(input.readInt());
    }

    @Override
    public NetOutput encode(NetOutput output) throws Exception {
        Preconditions.checkNotNull(serverId);
        Preconditions.checkNotNull(onlinePlayers);
        Preconditions.checkNotNull(status);

        output.writeUUID(serverId);
        output.writeInt(onlinePlayers);
        output.writeInt(status.getId());
        return output;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public UUID getServerId() {
        return serverId;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }

    public enum ServerStatus {
        STARTED(1),
        READY(2),
        ENABLE(3),
        SHUTDOWN(4),
        DISABLED(5);

        private final int id;

        ServerStatus(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ServerStatus from(int id) {
            return Arrays.stream(ServerStatus.values()).filter(s -> s.id == id).findFirst().orElse(ServerStatus.DISABLED);
        }
    }
}
