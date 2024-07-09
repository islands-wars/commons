package fr.islandswars.commons.service;

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

    RMQ_PASSWORD("rmq_password"),
    RMQ_HOSTNAME("rmq_host"),
    RMQ_USERNAME("rmq_username"),
    RMQ_PORT("rmq_port"),
    REDIS_PASSWORD("redis_password"),
    REDIS_HOSTNAME("redis_host"),
    REDIS_USERNAME("redis_username"),
    REDIS_PORT("redis_port"),
    MONGO_PASSWORD("mongodb_password"),
    MONGO_HOSTNAME("mongodb_host"),
    MONGO_USERNAME("mongodb_username"),
    MONGO_PORT("mongodb_port"),
    DOCKER_HOST("docker_host");


    private static final String path = "/run/secrets/";
    private final        String secretFileName;

    ServiceType(String secretFileName) {
        this.secretFileName = secretFileName;
    }

    public String getSecretFileName() {
        return secretFileName;
    }

    public String getSecretPath() {
        return path + secretFileName;
    }
}
