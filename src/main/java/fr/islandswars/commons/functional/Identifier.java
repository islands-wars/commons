package fr.islandswars.commons.functional;

/**
 * File <b>Identifier</b> located on fr.islandswars.commons.functional
 * Identifier is a part of commons.
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
 * Created the 27/06/2018 at 14:56
 * @since 0.1
 */
public interface Identifier<T> {

	/**
	 * @return this object id
	 */
	T getIdentifier();
}
