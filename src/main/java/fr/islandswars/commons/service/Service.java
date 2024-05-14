package fr.islandswars.commons.service;

import fr.islandswars.commons.utils.Preconditions;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

/**
 * File <b>Service</b> located on fr.islandswars.commons.service
 * Service is a part of commons.
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
 * Created the 29/04/2024 at 20:07
 * @since 0.1
 */
public abstract class Service<T> implements ServiceConnection<T> {

    protected String userName, password, serviceName;
    protected List<InetSocketAddress> serverAddress;

    public void dispose() throws Exception {
        close();
    }

    public void load(InetSocketAddress address, String userName, String password) {
        Preconditions.checkNotNull(address);
        load(Collections.singletonList(address), userName, password, "");
    }

    public void load(int port, String host, String userName, String password, String serviceName) {
        Preconditions.checkNotNull(host);
        load(Collections.singletonList(InetSocketAddress.createUnresolved(host, port)), userName, password, serviceName);
    }

    public void load(InetSocketAddress address, String userName, String password, String serviceName) {
        Preconditions.checkNotNull(address);
        load(Collections.singletonList(address), userName, password, serviceName);
    }

    public void load(List<InetSocketAddress> addressList, String userName, String password, String serviceName) {
        Preconditions.checkNotNull(userName);
        Preconditions.checkNotNull(password);
        Preconditions.checkNotNull(serviceName);

        this.serverAddress = addressList;
        this.userName = userName;
        this.password = password;
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(serverAddress.get(0).toString());
            builder.append(" using user = ").append(userName).append(" and ");
        builder.append("password = ").append(password.replaceAll("(?s).", "*"));
        if (!serviceName.equals(""))
            builder.append(" with service ").append(serviceName).append("!");
        return builder.toString();
    }

}

