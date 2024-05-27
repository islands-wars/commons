package fr.islandswars.commons.service.mongodb;

import fr.islandswars.commons.utils.LogUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * File <b>ObservableSubscriber</b> located on fr.islandswars.commons.service.mongodb
 * ObservableSubscriber is a part of commons.
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
 * Created the 01/05/2024 at 16:37
 * @since 0.1
 */
public class ObservableSubscriber<T> implements Subscriber<T> {

    private final    List<T>        received;
    private final    CountDownLatch latch;
    private volatile Subscription   subscription;
    private volatile boolean        completed;

    public ObservableSubscriber() {
        this.received = new ArrayList<>();
        this.latch = new CountDownLatch(1);
    }

    @Override
    public void onSubscribe(final Subscription s) {
        subscription = s;
    }

    @Override
    public void onNext(final T t) {
        received.add(t);
    }

    @Override
    public void onError(final Throwable t) {
        LogUtils.error(t.getMessage(), t);
        onComplete();
    }

    @Override
    public void onComplete() {
        completed = true;
        latch.countDown();
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public List<T> getReceived() {
        return received;
    }

    public List<T> get() {
        return await().getReceived();
    }

    public List<T> get(final long timeout, final TimeUnit unit) {
        return await(timeout, unit).getReceived();
    }


    public T first() {
        List<T> received = await().getReceived();
        return !received.isEmpty() ? received.get(0) : null;
    }

    public ObservableSubscriber<T> await() {
        return await(60, TimeUnit.SECONDS);
    }

    public ObservableSubscriber<T> await(final long timeout, final TimeUnit unit) {
        subscription.request(Integer.MAX_VALUE);
        try {
            if (!latch.await(timeout, unit)) {
                LogUtils.error( new TimeoutException("Publisher onComplete timed out"));
            }
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return this;
    }
}