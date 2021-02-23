package fr.islandswars.commons.service.collection;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fr.islandswars.commons.functional.Identifier;
import java.util.List;

/**
 * File <b>Collection</b> located on fr.islandswars.commons.service.collection
 * Collection is a part of commons.
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
 * Created the 22/02/2021 at 18:56
 * @since TODO edit
 */
public interface Collection<T> extends Identifier<String> {

	/**
	 * Count the number of document inside this collection
	 *
	 * @param resultCallback a callback performed in async by mongo at the end
	 */
	void count(SingleResultCallback<Long> resultCallback);

	/**
	 * Drop this collection
	 *
	 * @param resultCallback a callback performed in async by mongo at the end
	 * @see com.mongodb.async.client.MongoCollection#drop(SingleResultCallback)
	 */
	void drop(SingleResultCallback<Void> resultCallback);

	/**
	 * Find the values in the collection
	 *
	 * @param filter         the filter to use
	 * @param max            the maximum of value to getLocal
	 * @param resultCallback a callback performed in async by mongo at the end
	 */
	void find(Filter filter, int max, SingleResultCallback<List<T>> resultCallback);

	/**
	 * Put the value in the collection
	 *
	 * @param value          the value
	 * @param resultCallback a callback performed in async by mongo at the end
	 */
	void put(T value, SingleResultCallback<Void> resultCallback);

	/**
	 * Put or update the value in the collection
	 *
	 * @param filter         the filter to find the value
	 * @param value          the new value
	 * @param resultCallback a callback performed in async by mongo at the end
	 */
	void putOrUpdate(Filter filter, T value, SingleResultCallback<UpdateResult> resultCallback);

	/**
	 * Put or update all the value in the collection
	 *
	 * @param filter         the filter to find the values
	 * @param value          the new value
	 * @param resultCallback a callback performed in async by mongo at the end
	 */
	void putOrUpdateAll(Filter filter, T value, SingleResultCallback<UpdateResult> resultCallback);

	/**
	 * Remove the values matching the filter
	 *
	 * @param filter         the filter to use
	 * @param resultCallback a callback performed in async by mongo at the end
	 */
	void remove(Filter filter, SingleResultCallback<DeleteResult> resultCallback);

	/**
	 * Find the values in the collection and sort them
	 *
	 * @param fieldname      the field name
	 * @param max            the maximum values to getLocal
	 * @param resultCallback a callback performed in async by mongo at the end
	 */
	void sorted(String fieldname, int max, SingleResultCallback<List<T>> resultCallback);

	/**
	 * Update the value in the collection
	 *
	 * @param filter         the filter to find the value
	 * @param value          the new value
	 * @param resultCallback a callback performed in async by mongo at the end
	 */
	void update(Filter filter, T value, SingleResultCallback<UpdateResult> resultCallback);

	/**
	 * Update all the value in the collection
	 *
	 * @param filter         the filter to find the values
	 * @param value          the new value
	 * @param resultCallback a callback performed in async by mongo at the end
	 */
	void updateAll(Filter filter, T value, SingleResultCallback<UpdateResult> resultCallback);

}
