package fr.islandswars.commons.service.mongodb;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.SocketSettings;
import fr.islandswars.commons.service.Key;
import fr.islandswars.commons.service.Service;
import fr.islandswars.commons.service.collection.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File <b>MongoDBService</b> located on fr.islandswars.commons.service.mongodb
 * MongoDBService is a part of commons.
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
 * Created the 22/02/2021 at 18:55
 * @since 0.1
 */
@ThreadSafe
public class MongoDBService extends Service<MongoDatabase> {

	private final    ConcurrentMap<String, MongoDBCollection> collections;
	private volatile boolean                                  close;
	private          MongoDatabase                            database;

	public MongoDBService() {
		this.collections = new ConcurrentHashMap<>();
		this.close = false;
	}

	@Override
	public void close() throws Exception {
		close = true;
	}

	@Override
	public void connect() throws Exception {
		var mongoAddress    = new ServerAddress(serverAddress.get(0).getHostName(), serverAddress.get(0).getPort());
		var credential      = MongoCredential.createCredential(userName, serviceName, password.toCharArray());
		var socketSettings  = SocketSettings.builder().connectTimeout(10, TimeUnit.SECONDS).build();
		var clusterSettings = ClusterSettings.builder().hosts(Collections.singletonList(mongoAddress)).build();
		var settings        = MongoClientSettings.builder().clusterSettings(clusterSettings).credentialList(Collections.singletonList(credential)).socketSettings(socketSettings).build();
		MongoClient client  = MongoClients.create(settings);
		this.database = client.getDatabase(serviceName);
	}

	@Override
	public MongoDatabase getConnection() {
		if (database == null)
			try {
				connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return close ? null : database;
	}

	@Override
	public boolean isClosed() {
		return close;
	}

	@Override
	public void load(Map<String, String> properties) throws NullPointerException {
		var username = properties.get(Type.MONGO_USER.getKey());
		var password = properties.get(Type.MONGO_PASS.getKey());
		var database = properties.get(Type.MONGO_DB.getKey());
		var host     = properties.get(Type.MONGO_HOST.getKey());
		var port     = Integer.parseInt(properties.get(Type.MONGO_PORT.getKey()));
		Logger.getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.protocol.insert").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.protocol.query").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.protocol.update").setLevel(Level.OFF);
		load(port, host, username, password, database);
	}


	@SuppressWarnings("unchecked")
	public <T> Collection<T> get(String id, Class<T> type) throws IllegalStateException {
		if (database == null)
			throw new IllegalStateException("Unable to get the collection ! The database is not connected.");
		MongoDBCollection<T> collection = collections.get(id);
		if (collection != null)
			return close ? null : collection;
		collection = new MongoDBCollection<>(database.getCollection(id), type);
		collections.put(id, collection);
		return close ? null : collection;
	}

	public Collection<?>[] getCollections() {
		return close ? null : collections.values().toArray((Collection<?>[]) new Object[collections.size()]);
	}

	private enum Type implements Key {

		MONGO_USER(),
		MONGO_PASS(),
		MONGO_DB(),
		MONGO_HOST(),
		MONGO_PORT();

		@Override
		public String getKey() {
			return name();
		}
	}
}
