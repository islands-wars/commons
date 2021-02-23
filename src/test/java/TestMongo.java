import com.mongodb.async.client.MongoCollection;
import fr.islandswars.commons.service.collection.Filter;
import fr.islandswars.commons.service.mongodb.MongoDBService;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bson.Document;

/**
 * File <b>TestMongo</b> located on PACKAGE_NAME
 * TestMongo is a part of commons.
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
 * Created the 22/02/2021 at 19:20
 * @since TODO edit
 */
public class TestMongo {

	public static void main(String[] args) {
		var           mongo   = new MongoDBService();
		AtomicBoolean success = new AtomicBoolean(true);
		try {
			mongo.load(null);
			mongo.connect();
			var collec = mongo.get("test", Test.class);
			//collec.put(new Test("xharos", UUID.randomUUID(), 16, 58L), (r, t) -> success.set(false));
			collec.update(Filter.eq("name", "xharos"), new Test("xharos", UUID.randomUUID(), 160, 58L), (r, t)-> success.set(false));
			while (success.get()) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
