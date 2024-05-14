package fr.islandswars.commons;

import fr.islandswars.commons.service.mongodb.MongoDBConnection;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

/**
 * File <b>Commons</b> located on fr.islandswars.commons
 * Commons is a part of commons.
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
 * MERCHANTABILITY or rFITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">GNU license</a>.
 * <p>
 *
 * @author Jangliu, {@literal <jangliu@islandswars.fr>}
 * Created the 04/04/2024 at 15:52
 * @since 0.1
 */
public class Commons {

    public static final String NAME = "COMMONS";

    public static void main(String[] args) throws IOException {
        MongoDBConnection mongo = new MongoDBConnection();
        mongo.load();
        System.out.println("call load");
        mongo.connect();
        System.out.println("call connect");
        mongo.getConnection().getCollection("test").find()
                .first()
                .subscribe(new Subscriber<>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Document document) {
                        System.out.println("next el");
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("error");
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("done");
                    }
                });
        while (true) {

        }
    }
}
