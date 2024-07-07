package fr.islandswars.commons.service.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.islandswars.commons.secrets.DockerSecretsLoader;
import fr.islandswars.commons.service.ServiceConnection;
import fr.islandswars.commons.service.ServiceType;
import fr.islandswars.commons.service.collection.Collection;
import fr.islandswars.commons.utils.Preconditions;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * File <b>MongoDBConnection</b> located on fr.islandswars.commons.service.mongodb
 * MongoDBConnection is a part of commons.
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
 * Created the 30/04/2024 at 18:14
 * @since 0.1
 */
public class MongoDBConnection implements ServiceConnection<MongoDatabase> {

    private static final String DATABASE = "iswars";

    private final AtomicBoolean       status;
    private       MongoClient         client;
    private       MongoClientSettings settings;
    private       MongoDatabase       base;
    private       CodecRegistry       codecRegistry;

    public MongoDBConnection() {
        this.status = new AtomicBoolean(true);
    }

    @Override
    public void close() throws Exception {
        if (client != null)
            client.close();
        status.set(true);
    }

    @Override
    public void connect() throws Exception {
        Preconditions.checkNotNull(settings);

        this.client = MongoClients.create(settings);
        this.base = client.getDatabase(DATABASE);
        status.set(false);
    }

    @Override
    public MongoDatabase getConnection() {
        Preconditions.checkNotNull(base, "Need to call MongoDBConnection#load() first.");

        return base;
    }

    @Override
    public boolean isClosed() {
        return status.get();
    }

    @Override
    public void load() {
        var pass = DockerSecretsLoader.getValue(ServiceType.MONGO_PASSWORD);
        var user = DockerSecretsLoader.getValue(ServiceType.MONGO_USERNAME);
        var host = DockerSecretsLoader.getValue(ServiceType.MONGO_HOSTNAME);
        var port = DockerSecretsLoader.getValue(ServiceType.MONGO_PORT);

        MongoCredential credential = MongoCredential.createCredential(user, DATABASE, pass.toCharArray());

        this.settings = MongoClientSettings.builder().credential(credential)
                .applyToClusterSettings(builder -> builder.hosts(List.of(new ServerAddress(host, Integer.decode(port)))))
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY).build();
    }

    public Collection<Document> getCollection(String name) {
        return getCollection(name, Document.class);
    }

    public <T> Collection<T> getCollection(String name, Class<T> clazz) {
        return new Collection<>(getConnection().getCollection(name), clazz);
    }

}
