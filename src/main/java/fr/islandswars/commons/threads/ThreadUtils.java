package fr.islandswars.commons.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * File <b>ThreadUtils</b> located on fr.islandswars.commons.threads
 * ThreadUtils is a part of commons.
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
 * Created the 04/04/2024 at 16:18
 * @since 0.1
 */
public class ThreadUtils {

    private static final ExecutorService executor;

    static {
        executor = Executors.newCachedThreadPool(new IslandsThreadFactory());
    }

    private ThreadUtils() {
    }

    /**
     * @return a custom cached thread pool
     */
    public static ExecutorService getExecutor() {
        return executor;
    }

    private static class IslandsThreadFactory implements ThreadFactory {

        private final AtomicInteger count;

        IslandsThreadFactory() {
            this.count = new AtomicInteger();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            var thread = new Thread(runnable);
            thread.setName("is-pool-" + count.getAndIncrement());
            return thread;
        }
    }
}

