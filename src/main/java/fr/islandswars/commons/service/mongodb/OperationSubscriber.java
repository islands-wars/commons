package fr.islandswars.commons.service.mongodb;

import org.reactivestreams.Subscription;

/**
 * File <b>OperationSubscriber</b> located on fr.islandswars.commons.service.mongodb
 * OperationSubscriber is a part of commons.
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
 * Created the 02/05/2024 at 17:20
 * @since 0.1
 */
public class OperationSubscriber<T> extends ObservableSubscriber<T> {

    @Override
    public void onSubscribe(Subscription s) {
        super.onSubscribe(s);
        s.request(Integer.MAX_VALUE);
    }


}
