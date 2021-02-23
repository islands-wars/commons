import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import fr.islandswars.commons.service.Key;
import fr.islandswars.commons.service.docker.DockerService;
import fr.islandswars.commons.threads.ThreadUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * File <b>TestDocker</b> located on PACKAGE_NAME
 * TestDocker is a part of commons.
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
 * Created the 23/02/2021 at 13:16
 * @since TODO edit
 */
public class TestDocker {

	public static void main(String[] args) {
		DockerService docker = new DockerService();
		try {
			var props = new HashMap<String, String>();
			props.put("DOCKER_USER", "user");
			props.put("DOCKER_PASS", "pass");
			props.put("DOCKER_HOST", "localhost");
			props.put("DOCKER_PORT", "2375");
			docker.load(props);
			docker.connect();
			var client    = docker.getConnection();
			var container = docker.createContainer(() -> "mongo:4.0.4");
			container.withName("mongo_test");
			container.start(docker);
			/*CreateContainerResponse container
					= client.createContainerCmd("mongo:3.6")
					.withCmd("--bind_ip_all")
					.withName("mongo_test_1")
					.withHostName("baeldung")
					.withEnv("MONGO_LATEST_VERSION=3.6")
					.withPortBindings(PortBinding.parse("9999:27017")).exec();
			client.startContainerCmd(container.getId()).exec();*/
			long count = System.currentTimeMillis();
			while (!container.isStarted()) {
			}
			docker.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ended ++");
	}

}
