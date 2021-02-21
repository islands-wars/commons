package fr.islandswars.commons.network;

/**
 * File <b>Pool</b> located on fr.islandswars.commons.network
 * Pool is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2021 Islands Wars.
 * <p>
 * Islands Wars - Commons is free software: you can redistribute it and/or modify
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
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">GNU GPL license</a>.
 * <p>
 *
 * @author Valentin Burgaud (Xharos), {@literal <xharos@islandswars.fr>}
 * Created the 22/10/17 at 14:59
 * @since 0.1
 */
public interface Pool {

	/**
	 * Allocate a new NetInput with 1024 bytes (default pool impl)
	 *
	 * @return a new NetInput
	 */
	NetInput allocateNetInput();

	/**
	 * Allocate a new NetOutput with 1024 bytes (default pool impl)
	 *
	 * @return a new NetOuput
	 */
	NetOutput allocateNetOutput();

	/**
	 * Send back this buffer to the pool
	 *
	 * @param input a buffer to free
	 */
	void free(NetInput input);

	/**
	 * Send back this buffer to the pool
	 *
	 * @param output a buffer to free
	 */
	void free(NetOutput output);
}
