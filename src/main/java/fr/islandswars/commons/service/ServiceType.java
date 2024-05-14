package fr.islandswars.commons.service;

import java.io.File;

/**
 * File <b>ServiceType</b> located on fr.islandswars.commons.service
 * ServiceType is a part of commons.
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
 * Created the 29/04/2024 at 18:18
 * @since 0.1
 */
public enum ServiceType {

    REDIS("redis_password"),
    MONGO_PASSWORD("mongodb_password"),
    MONGO_HOST("mongodb_host"),
    MONGO_PORT("mongodb_port"),
    MONGO_USERNAME("mongodb_username");

    private static final String path = "/run/secrets/";
    private final        String secretFileName;

    ServiceType(String secretFileName) {
        this.secretFileName = secretFileName;
    }

    public String getSecret() {
        return path + secretFileName;
    }
}
