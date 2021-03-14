package fr.islandswars.commons.service.docker;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.LogConfig;
import com.mongodb.annotations.ThreadSafe;
import fr.islandswars.commons.service.Key;
import fr.islandswars.commons.threads.ThreadUtils;
import fr.islandswars.commons.utils.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * File <b>DockerContainer</b> located on fr.islandswars.commons.service.docker
 * DockerContainer is a part of commons.
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
 * Created the 23/02/2021 at 13:59
 * @since TODO edit
 */
@ThreadSafe
public class DockerContainer {

	private static final Map<String, String> LOG_CONFIG;
	private final        CreateContainerCmd  createCmd;
	private volatile     String              container_id;

	static {
		LOG_CONFIG = Collections.singletonMap("gelf-address", "udp://0.0.0.0:12201");
	}

	DockerContainer(Key key, DockerService service) {
		Preconditions.checkNotNull(key);
		Preconditions.checkState(key, k -> k.getKey() != null && !k.getKey().isEmpty());

		this.createCmd = service.getConnection().createContainerCmd(key.getKey());
	}

	/**
	 * @return this container's id if launched
	 */
	public String getContainerId() {
		return container_id;
	}

	/**
	 * @return if {@link CreateContainerCmd} has been executed
	 */
	public boolean isStarted() {
		return getContainerId() != null;
	}

	/**
	 * Start this container
	 *
	 * @param service the service
	 * @return an async code to execute
	 */
	public CompletableFuture<Void> start(DockerService service) {
		Preconditions.checkNotNull(service);

		return service.execSyncCmd(createCmd).thenApplyAsync((response) -> {
			var id = response.getId();
			service.getConnection().startContainerCmd(id).exec();
			return id;
		}, ThreadUtils.getExecutor()).thenAcceptAsync(id -> this.container_id = id);
	}

	/**
	 * Stop, then remove this container from the docker host
	 *
	 * @param service the service
	 * @param timeout a timeout before kill (default 10 seconds)
	 * @return an async code to execute
	 */
	public CompletableFuture<Void> stop(DockerService service, int timeout) {
		Preconditions.checkNotNull(service);
		Preconditions.checkState(timeout, ref -> ref >= 0);

		return service.execSyncCmd(service.getConnection().stopContainerCmd(getContainerId()).withTimeout(timeout == 0 ? 10 : timeout))
				.thenRunAsync(() -> {
					service.getConnection().removeContainerCmd(getContainerId());
					container_id = null;
				}, ThreadUtils.getExecutor());
	}

	/**
	 * Attach env variables to this container
	 *
	 * @param env variables to add to this container
	 */
	public void withEnv(String... env) {
		Preconditions.checkNotNull(env);
		createCmd.withEnv(env);
	}

	/**
	 * Specify ports that needs to be exposed
	 *
	 * @param tcpPorts a list of internal ports
	 */
	public void withExposedTcpPorts(int... tcpPorts) {
		List<ExposedPort> ports = new ArrayList<>();
		for (var tcpPort : tcpPorts) {
			ports.add(ExposedPort.tcp(tcpPort));
		}
		createCmd.withExposedPorts(ports);
		//TODO test if we need to bind ports, or maybe supply option to bind exposed ports
	}

	/**
	 * Redirect log to our graylog service through GELF protocol
	 */
	public void withGelfLog() {
		var config = new LogConfig(LogConfig.LoggingType.GELF);
		config.setConfig(LOG_CONFIG);
		createCmd.getHostConfig().withLogConfig(config);
	}

	/**
	 * Attach a name to this container
	 *
	 * @param name a container's name
	 */
	public void withName(String name) {
		Preconditions.checkNotNull(name);
		Preconditions.checkState(name, ref -> !ref.isEmpty());

		createCmd.withName(name);
	}

}

