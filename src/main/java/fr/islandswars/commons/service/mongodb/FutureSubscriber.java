package fr.islandswars.commons.service.mongodb;

import fr.islandswars.commons.utils.LogUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * File <b>FutureSubscriber</b> located on fr.islandswars.commons.service.mongodb
 * FutureSubscriber is a part of commons.
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
 * Created the 14/05/2024 at 17:46
 * @since 0.1
 */
public class FutureSubscriber<T> implements Subscriber<T> {

    private final CompletableFuture<List<T>> future   = new CompletableFuture<>();
    private final List<T>                    received = new ArrayList<>();

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE); // Request all items
    }

    @Override
    public void onNext(T item) {
        received.add(item);
    }

    @Override
    public void onError(Throwable throwable) {
        future.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        future.complete(received);
    }

    public CompletableFuture<List<T>> getFuture() {
        return future;
    }

    public List<T> get() {
        return getFuture().join();
    }

    public List<T> get(long timeout, TimeUnit unit) throws TimeoutException {
        try {
            return getFuture().get(timeout, unit);
        } catch (Exception e) {
            LogUtils.error(e);
            return null;
        }
    }

    public T first() {
        List<T> received = get();
        return received.isEmpty() ? null : received.get(0);
    }
}
