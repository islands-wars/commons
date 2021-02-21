package fr.islandswars.commons.service;

import java.util.Map;

/**
 * File <b>ServiceConnection</b> located on fr.islandswars.commons.service
 * ServiceConnection is a part of commons.
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
 * Created the 21/02/2021 at 21:32
 * @since 0.1
 */
public interface ServiceConnection<T> {

	/**
	 * Close, if open, the current service connection
	 *
	 * @throws Exception if close operation failed
	 */
	void close() throws Exception;

	/**
	 * Try to connect to the current service
	 *
	 * @throws Exception a connection exception (timeout, wrong id, etc)
	 */
	void connect() throws Exception;

	/**
	 * @return the connection object, depends on each service
	 */
	T getConnection();

	/**
	 * @return weither or not this service is open
	 */
	boolean isClosed();

	/**
	 * Load credentials from docker serets
	 *
	 * @param properties this service property
	 * @throws NullPointerException if the given map is empty
	 * @see fr.islandswars.commons.secrets.DockerSecretsLoader#load(ServiceType)
	 */
	void load(Map<String, String> properties) throws NullPointerException;
}

