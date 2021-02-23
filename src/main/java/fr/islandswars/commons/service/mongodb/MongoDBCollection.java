package fr.islandswars.commons.service.mongodb;

import com.google.gson.Gson;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fr.islandswars.commons.service.collection.Collection;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * File <b>MongoDBCollection</b> located on fr.islandswars.commons.service.mongodb
 * MongoDBCollection is a part of commons.
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
 * @author Sceat and David <a href = "https://github.com/HydreIO/AresCommons/tree/master/src/main/java/fr/aresrpg/commons/domain/database">AresCommons</a>
 * @author Valentin Burgaud (Xharos), {@literal <xharos@islandswars.fr>}
 * Created the 22/02/2021 at 18:55
 * @since 0.1
 */
public class MongoDBCollection<T> implements Collection<T> {

	private static final UpdateOptions             UPSERT;
	private final        MongoCollection<Document> collection;
	private final        Gson                      gson;
	private final        Class<T>                  clazz;

	static {
		UPSERT = new UpdateOptions().upsert(true);
	}

	MongoDBCollection(MongoCollection<Document> collection, Class<T> clazz) {
		this.collection = collection;
		this.gson = new Gson();
		this.clazz = clazz;
	}

	@Override
	public void count(SingleResultCallback<Long> resultCallback) {
		collection.count(resultCallback);
	}

	@Override
	public void drop(SingleResultCallback<Void> resultCallback) {
		collection.drop(resultCallback);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void find(Bson filter, int max, SingleResultCallback<List<T>> resultCallback) {
		List<T>                found = new ArrayList<>();
		FindIterable<Document> cursor;
		try {
			if (filter == null)
				cursor = collection.find().limit(max);
			else
				cursor = collection.find(filter).limit(max);
			cursor.forEach((doc) -> found.add(gson.fromJson(doc.toJson(), getClazz())), (viod, throwable) -> resultCallback.onResult(found, throwable));
		} catch (Exception e) {
			System.err.println("Could'not deserialize");
		}
	}

	@Override
	public void put(T value, SingleResultCallback<Void> resultCallback) {
		var document = Document.parse(gson.toJson(value));
		System.out.println("doc -> " + document);
		collection.insertOne(document, /*new Document("$set", document),*/ resultCallback);
	}

	@Override
	public void putOrUpdate(Bson filter, T value, SingleResultCallback<UpdateResult> resultCallback) {
		var document = Document.parse(gson.toJson(value));
		collection.updateOne(filter, new Document("$set", document), UPSERT, resultCallback);
	}

	@Override
	public void putOrUpdateAll(Bson filter, T value, SingleResultCallback<UpdateResult> resultCallback) {
		var document = Document.parse(gson.toJson(value));
		collection.updateMany(filter, new Document("$set", document), resultCallback);
	}

	@Override
	public void remove(Bson filter, SingleResultCallback<DeleteResult> resultCallback) {
		collection.deleteMany(filter, resultCallback);
	}

	@Override
	public void sorted(String fieldname, int max, SingleResultCallback<List<T>> resultCallback) {
		List<T> found = new ArrayList<>();
		if (max == 0)
			return;
		try {
			FindIterable<Document> cursor = collection.find().sort(Filters.exists(fieldname));
			cursor.forEach(doc -> {
				found.add(gson.fromJson(doc.toJson(), getClazz()));
			}, (viod, throwable) -> resultCallback.onResult(found, throwable));
		} catch (Exception e) {
			System.err.println("Could'not deserialize");
		}
	}

	@Override
	public void update(Bson filter, T value, SingleResultCallback<UpdateResult> resultCallback) {
		var document = Document.parse(gson.toJson(value));
		collection.updateOne(filter, new Document("$set", document), resultCallback);
	}

	@Override
	public void updateAll(Bson filter, T value, SingleResultCallback<UpdateResult> resultCallback) {
		var document = Document.parse(gson.toJson(value));
		collection.updateMany(filter, new Document("$set", document), resultCallback);
	}

	@Override
	public String getIdentifier() {
		return collection.getNamespace().getCollectionName();
	}

	private Class<T> getClazz() {
		return clazz;
	}
}
