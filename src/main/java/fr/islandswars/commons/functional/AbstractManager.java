package fr.islandswars.commons.functional;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * File <b>AbstractManager</b> located on fr.islandswars.commons.functional
 * AbstractManager is a part of commons.
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
 * Created the 27/06/2018 at 14:57
 * @since 0.1
 */
public interface AbstractManager<K, V, T> extends ResourceDisposable<K>, Identifier<T> {

	/**
	 * Get the resource associated to this key, or else null
	 *
	 * @param key a generic key
	 * @return a generic var if exist
	 */
	V get(K key);

	/**
	 * Get all registered resource {@link AbstractManager#register(Object, Object)}
	 *
	 * @return a {@link Stream} of registered resources
	 */
	Stream<V> getAssociatedResource();

	/**
	 * Get the resource associated to this key, or else null
	 *
	 * @param key          a generic key
	 * @param defaultValue a generic key if the first one doesn't match anything
	 * @return a generic var if exist
	 * @see java.util.HashMap#getOrDefault(Object, Object)
	 */
	default V getOrElse(K key, V defaultValue) {
		V value = get(key);
		return value == null ? defaultValue : value;
	}

	/**
	 * Get the resource associated to this key, or else null
	 *
	 * @param key               a generic key
	 * @param exceptionSupplier an exception to throw if the key doesn't match anything
	 * @param <X>               a super class of Throwable
	 * @return a generic var if exist
	 * @throws X if the value is nul
	 * @see java.util.Optional#orElseThrow(Supplier)
	 */
	default <X extends Throwable> V getOrElseThrow(K key, Supplier<? extends X> exceptionSupplier) throws X {
		V value = get(key);
		if (value == null)
			throw exceptionSupplier.get();
		else
			return value;
	}

	/**
	 * Register the given generic parameter
	 *
	 * @param key   a generic var
	 * @param value a generic var
	 */
	void register(K key, V value);

}
