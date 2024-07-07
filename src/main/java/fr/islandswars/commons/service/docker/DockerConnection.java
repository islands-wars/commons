package fr.islandswars.commons.service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import fr.islandswars.commons.secrets.DockerSecretsLoader;
import fr.islandswars.commons.service.ServiceConnection;
import fr.islandswars.commons.service.ServiceType;
import fr.islandswars.commons.utils.LogUtils;
import fr.islandswars.commons.utils.Preconditions;

import java.time.Duration;

/**
 * File <b>DockerConnection</b> located on fr.islandswars.commons.service.docker
 * DockerConnection is a part of commons.
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
 * Created the 07/07/2024 at 19:20
 * @since 0.3.1
 */
public class DockerConnection implements ServiceConnection<DockerClient> {

    private DockerClientConfig config;
    private DockerHttpClient   httpClient;
    private DockerClient       client;

    @Override
    public void close() throws Exception {
        if (client != null)
            client.close();
    }

    @Override
    public void connect() throws Exception {
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(httpClient);

        this.client = DockerClientImpl.getInstance(config, httpClient);
    }

    @Override
    public DockerClient getConnection() {
        Preconditions.checkNotNull(client, "Need to call DockerConnection#load() first.");

        return client;
    }

    @Override
    public boolean isClosed() {
        if (client == null)
            return true;
        else
            try {
                client.pingCmd().exec();
                return false;
            } catch (Exception e) {
                LogUtils.error(e);
                return true;
            }
    }

    @Override
    public void load() {
        var host = DockerSecretsLoader.getValue(ServiceType.DOCKER_HOST);

        this.config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(host).build();
        this.httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(10))
                .responseTimeout(Duration.ofSeconds(10))
                .build();
    }
}
