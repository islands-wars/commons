package fr.islandswars.commons.service.collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import fr.islandswars.commons.service.mongodb.FutureSubscriber;
import fr.islandswars.commons.service.mongodb.OperationSubscriber;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.concurrent.CompletableFuture;

/**
 * File <b>Collection</b> located on fr.islandswars.commons.service.collection
 * Collection is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2024 Islands Wars.
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
 * @author Jangliu, {@literal <jangliu@islandswars.fr>}
 * Created the 02/05/2024 at 16:56
 * @since 0.1
 */
public class Collection<T> {

    private final Gson                      gson;
    private final MongoCollection<Document> mongoCollection;
    private final UpdateOptions             updateOptions;
    private final ReplaceOptions            replaceOptions;
    private final Class<T>                  clazz;

    public Collection(MongoCollection<Document> mongoCollection, Class<T> clazz) {
        this.mongoCollection = mongoCollection;
        this.gson = new GsonBuilder().create();
        this.updateOptions = new UpdateOptions().upsert(true);
        this.replaceOptions = new ReplaceOptions().upsert(true);
        this.clazz = clazz;
    }

    public CompletableFuture<T> findOne(Bson filter) {
        var subscriber = new FutureSubscriber<Document>();
        mongoCollection.find(filter).first().subscribe(subscriber);
        return subscriber.getFuture().thenApplyAsync(rec -> {
            var first = rec.isEmpty() ? null : rec.get(0);
            if (first != null)
                return deserialize(first);
            else return null;
        });
    }

    public OperationSubscriber<InsertOneResult> insert(T doc) {
        var subscriber = new OperationSubscriber<InsertOneResult>();
        mongoCollection.insertOne(serialize(doc)).subscribe(subscriber);
        return subscriber;
    }

    public OperationSubscriber<UpdateResult> replace(T doc, Bson filter) {
        var subscriber = new OperationSubscriber<UpdateResult>();
        mongoCollection.replaceOne(filter, serialize(doc), replaceOptions).subscribe(subscriber);
        return subscriber;
    }

    public OperationSubscriber<UpdateResult> update(Bson doc, Bson filter) {
        var subscriber = new OperationSubscriber<UpdateResult>();
        mongoCollection.updateOne(filter, doc, updateOptions).subscribe(subscriber);
        return subscriber;
    }

    public OperationSubscriber<DeleteResult> remove(Bson filter) {
        var subscriber = new OperationSubscriber<DeleteResult>();
        mongoCollection.deleteOne(filter).subscribe(subscriber);
        return subscriber;
    }

    public Document serialize(T object) {
        return Document.parse(gson.toJson(object));
    }

    public T deserialize(Document doc) {
        return gson.fromJson(doc.toJson(), clazz);
    }
}
