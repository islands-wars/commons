package fr.islandswars.commons.secrets;

import fr.islandswars.commons.log.IslandsLogger;
import fr.islandswars.commons.service.ServiceType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * File <b>DockerSecretsLoader</b> located on fr.islandswars.commons.secrets
 * DockerSecretsLoader is a part of commons.
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
public class DockerSecretsLoader {

    private static final Boolean COMPOSE = Boolean.parseBoolean(System.getenv("COMPOSE"));

    public static String getValue(ServiceType type) {
        if (COMPOSE)
            return System.getenv(type.getSecretFileName());
        else
            try {
                return new String(Files.readAllBytes(Paths.get(type.getSecretPath())));
            } catch (IOException e) {
                IslandsLogger.getLogger().logError(e);
            }
        return type.getSecretFileName();
    }
}
