package fr.islandswars.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.islandswars.commons.service.collection.Collection;
import fr.islandswars.commons.service.mongodb.MongoDBConnection;
import fr.islandswars.commons.service.mongodb.ObservableSubscriber;
import org.bson.Document;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * File <b>MongoDBTest</b> located on fr.islandswars.test
 * MongoDBTest is a part of commons.
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
 * Created the 01/05/2024 at 16:17
 * @since 0.1
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoDBTest {

    private final static Gson              gson    = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private final static TestDoc           testDoc = new TestDoc(1, 2, "myman", "s1", "s2");
    private static       MongoDBConnection mongoClient;
    private static       MongoDatabase     database;

    @BeforeAll
    public static void setup() throws Exception {
        mongoClient = new MongoDBConnection();
        mongoClient.load();
        mongoClient.connect();
        database = mongoClient.getConnection();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        mongoClient.close();
    }

    @Test
    @Order(1)
    public void testConnection() {
        assertNotNull(database, "Database connection should not be null");

        assertDoesNotThrow(() -> {
            var subscriber = new ObservableSubscriber<>();
            database.listCollectionNames().subscribe(subscriber);
            var result = subscriber.get();
        }, "No exception should be thrown during MongoDB connection test");
    }

    @Test
    @Order(2)
    public void testRemoveCollection() {
        assertDoesNotThrow(() -> {
            var subscriber = new ObservableSubscriber<>();
            database.getCollection("test").drop().subscribe(subscriber);
            subscriber.get();
        }, "No exception should be thrown during collection drop");
    }

    @Test
    @Order(3)
    public void testCreateCollection() {
        assertDoesNotThrow(() -> {
            var subscriber = new ObservableSubscriber<>();
            database.createCollection("test").subscribe(subscriber);
            subscriber.get();
        }, "No exception should be thrown during new collection creation");
    }

    @Test
    @Order(4)
    public void testSerialize() {
        var doc = Document.parse(gson.toJson(testDoc));
        assertEquals(doc.get("id", Integer.class), 1);
        assertEquals(doc.get("strings", List.class).size(), 2);
        assertEquals(doc.get("name", String.class).length(), 5);
        assertNull(doc.get("j", Long.class));
    }

    @Test
    @Order(5)
    public void testWriteInCollection() {
        Collection<TestDoc> testDocCollection = mongoClient.getCollection("test", TestDoc.class);
        var                 subscriber        = testDocCollection.insert(testDoc);
        var                 result            = subscriber.first();
        assertTrue(result.wasAcknowledged(), "The collection test should exists");
    }

    @Test
    @Order(6)
    public void testRetrieveDocument() throws ExecutionException, InterruptedException {
        Collection<TestDoc> testDocCollection = mongoClient.getCollection("test", TestDoc.class);
        var                 publisher         = testDocCollection.findOne(Filters.eq("id", 1));
        TestDoc             result            = publisher.get();
        assertNotNull(result, "The find operation should return a result");
    }

    @Test
    @Order(7)
    public void testUpdateDocument() {
        Collection<TestDoc> testDocCollection = mongoClient.getCollection("test", TestDoc.class);
        var                 subscriber        = testDocCollection.update(Updates.set("name", "sdf"), Filters.eq("name", "myman"));
        var                 result            = subscriber.first();
        assertEquals(result.getModifiedCount(), 1, "The update operation should be on one element");
    }

    private static class TestDoc {

        @Expose
        @SerializedName("id")
        private int          i;
        @Expose
        private List<String> strings;
        @Expose
        private String       name;
        private long         j;

        public TestDoc(int i, long j, String name, String... values) {
            this.i = i;
            this.name = name;
            this.strings = Arrays.asList(values);
            this.j = j;
        }

        @Override
        public String toString() {
            return gson.toJson(this);
        }
    }
}
