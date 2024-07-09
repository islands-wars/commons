package fr.islandswars.commons.service.rabbitmq.packet.proxy;

import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.service.rabbitmq.packet.Packet;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;
import fr.islandswars.commons.utils.Preconditions;

import java.util.UUID;

/**
 * File <b>ProxyUpPacccket</b> located on fr.islandswars.commons.service.rabbitmq.packet.proxy
 * ProxyUpPacccket is a part of commons.
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
 * Created the 07/07/2024 at 21:15
 * @since 0.3.1
 * <p>
 * When a new proxy start, it will notify the other using this packet.
 * The UUID is passed and will be also available in redis
 */
public class ProxyUpPacket extends Packet {

    private UUID proxyId;

    public ProxyUpPacket() {
        super(PacketType.Status.PROXY_UP_REQUEST.getId());
    }

    @Override
    public void decode(NetInput input) throws Exception {
        this.proxyId = input.readUUID();
    }

    @Override
    public void encode(NetOutput output) throws Exception {
        Preconditions.checkNotNull(proxyId);

        output.writeUUID(proxyId);
    }

    public UUID getProxyId() {
        return proxyId;
    }

    public void setProxyId(UUID proxyId) {
        this.proxyId = proxyId;
    }
}
