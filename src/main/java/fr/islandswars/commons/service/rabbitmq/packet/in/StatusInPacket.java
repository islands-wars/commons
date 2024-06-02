package fr.islandswars.commons.service.rabbitmq.packet.in;

import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.service.rabbitmq.packet.Packet;
import fr.islandswars.commons.service.rabbitmq.packet.PacketType;

/**
 * File <b>StatusInPacket</b> located on fr.islandswars.commons.service.rabbitmq.packet.in
 * StatusInPacket is a part of commons.
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
 */
public class StatusInPacket extends Packet {

    public StatusInPacket() {
        super(PacketType.Status.STATUS_IN.getId());
    }

    @Override
    public void decode(NetInput input) {

    }

    @Override
    public NetOutput encode(NetOutput output) {
        return output;
    }
}
