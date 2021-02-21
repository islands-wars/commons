package fr.islandswars.commons.service.redis;

import fr.islandswars.commons.service.Key;
import fr.islandswars.commons.service.Service;
import java.util.Map;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * File <b>RedisService</b> located on fr.islandswars.commons.service.redis
 * RedisService is a part of commons.
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
 * Created the 21/02/2021 at 21:42
 * @since 0.1
 */
public class RedisService extends Service<Jedis> {

	private          JedisPool pool;
	private volatile boolean   close;

	public RedisService() {
		this.close = false;
	}

	@Override
	public void close() throws Exception {
		close = true;
	}

	@Override
	public boolean isClosed() {
		return close;
	}

	@Override
	public void connect() throws Exception {
		var serverSocketAddress = serverAddress.get(0);
		var   config              = new JedisPoolConfig();
		//read https://gist.github.com/JonCole/925630df72be1351b21440625ff2671f#file-redis-bestpractices-java-jedis-md
		config.setMaxTotal(8);
		config.setMinIdle(2);
		//config.setMaxWaitMillis(1000); && blockWhenExhausted(true); by default will throw exception
		this.pool = new JedisPool(config, serverSocketAddress.getHostString(), serverSocketAddress.getPort(), 2000, password);
	}

	@Override
	public Jedis getConnection() {
		if (pool == null)
			try {
				connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return close ? null : pool.getResource();
	}

	@Override
	public void load(Map<String, String> properties) throws NullPointerException {
		var password = properties.get(RedisService.Type.REDIS_PASS.getKey());
		var host     = properties.get(RedisService.Type.REDIS_HOST.getKey());
		var port     = Integer.parseInt(properties.get(Type.REDIS_PORT.getKey()));

		load(port, host, "", password);
	}

	private enum Type implements Key {

		REDIS_PASS(),
		REDIS_HOST(),
		REDIS_PORT();

		@Override
		public String getKey() {
			return name();
		}
	}
}
