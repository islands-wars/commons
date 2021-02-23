package fr.islandswars.commons.service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.SyncDockerCmd;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import fr.islandswars.commons.service.Key;
import fr.islandswars.commons.service.Service;
import fr.islandswars.commons.threads.ThreadUtils;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * File <b>DockerService</b> located on fr.islandswars.commons.service.docker
 * DockerService is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2021 Islands Wars.
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
 * @author Valentin Burgaud (Xharos), {@literal <xharos@islandswars.fr>}
 * Created the 23/02/2021 at 13:07
 * @since 0.1
 */
public class DockerService extends Service<DockerClient> {

	private          DockerClient connection;
	private volatile boolean      close;

	@Override
	public void close() throws Exception {
		close = true;
		connection.close();
	}

	@Override
	public void connect() throws Exception {
		var address = serverAddress.get(0);
		var config = DefaultDockerClientConfig.createDefaultConfigBuilder()
				.withDockerHost("tcp://" + address.getHostName() + ":" + address.getPort())
				//.withDockerTlsVerify(true)
				//.withRegistryUsername(userName)
				//.withRegistryPassword(password)
				.build();
		var httpClient = new ApacheDockerHttpClient.Builder()
				.dockerHost(config.getDockerHost())
				.sslConfig(config.getSSLConfig())
				.build();
		this.connection = DockerClientImpl.getInstance(config, httpClient);
	}

	@Override
	public DockerClient getConnection() {
		if (connection == null)
			try {
				connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return close ? null : connection;
	}

	@Override
	public boolean isClosed() {
		//connection.pingCmd().exec();
		return close;
	}

	@Override
	public void load(Map<String, String> properties) throws NullPointerException {
		var username = properties.get(Type.DOCKER_USER.getKey());
		var password = properties.get(Type.DOCKER_PASS.getKey());
		var host     = properties.get(Type.DOCKER_HOST.getKey());
		var port     = Integer.parseInt(properties.get(Type.DOCKER_PORT.getKey()));
		load(port, host, username, password);
	}

	/**
	 * Create a new container with a specific image
	 *
	 * @param key supply an image
	 * @return a container object
	 */
	public DockerContainer createContainer(Key key) {
		return isClosed() ? null : new DockerContainer(key, this);
	}

	/**
	 * Exec the given command in a concurrent pool
	 *
	 * @param cmd a sync docker cmd to execute
	 * @param <T> cmd generic type
	 * @return a future to deal with the result
	 */
	public <T> CompletableFuture<T> execSyncCmd(SyncDockerCmd<T> cmd) {
		if (!isClosed())
			return CompletableFuture.supplyAsync(cmd::exec, ThreadUtils.getExecutor());
		else throw new UnsupportedOperationException("Docker service's closed!");
	}

	private enum Type implements Key {

		DOCKER_USER(),
		DOCKER_PASS(),
		DOCKER_HOST(),
		DOCKER_PORT();

		@Override
		public String getKey() {
			return name();
		}
	}
}
