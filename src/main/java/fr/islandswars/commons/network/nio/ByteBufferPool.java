package fr.islandswars.commons.network.nio;

import fr.islandswars.commons.log.IslandsLogger;
import fr.islandswars.commons.network.NetInput;
import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.network.Pool;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * File <b>ByteBufferPool</b> located on fr.islandswars.commons.network.nio
 * ByteBufferPool is a part of commons.
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
 * Created the 18/05/2024 at 21:47
 * @since 0.1
 */
public class ByteBufferPool implements Pool {

    private final ConcurrentLinkedQueue<ByteBuffer> masterQueue;
    private final ThreadLocal<LocalCache>           bufferQueue;
    private final int                               finalSize;
    private final int                               masterSize;
    private final int                               slices;
    private final boolean                           direct;

    public ByteBufferPool(int size, boolean direct) {
        this.finalSize = Math.min(Integer.highestOneBit(size - 1), 0x40000000) << 1;
        this.masterSize = Math.min(0x100000, size);
        this.slices = size < 0x10000 ? masterSize / size : 1;
        this.direct = direct;
        this.masterQueue = new ConcurrentLinkedQueue<>();
        this.bufferQueue = new ThreadLocal<LocalCache>() {
            @Override
            protected LocalCache initialValue() {
                return new LocalCache();
            }

            @Override
            public void remove() {
                get().empty();
            }
        };
    }

    @Override
    public NetInput allocateNetInput() {
        return new InputByteBuffer(allocate());
    }

    @Override
    public NetOutput allocateNetOutput() {
        return new OutputByteBuffer(allocate());
    }

    @Override
    public void free(NetInput input) {
        if (input instanceof InputByteBuffer)
            free(((InputByteBuffer) input).getByteBuffer());
    }

    @Override
    public void free(NetOutput output) {
        if (output instanceof OutputByteBuffer)
            free(((OutputByteBuffer) output).getByteBuffer());
    }

    private ByteBuffer allocate() {
        final var localCache = bufferQueue.get();
        var       byteBuffer = localCache.queue.pollLast();
        if (byteBuffer == null) {
            byteBuffer = masterQueue.poll();
            if (byteBuffer == null) {
                var masterBuffer = direct ? ByteBuffer.allocateDirect(masterSize) : ByteBuffer.allocate(masterSize);
                if (slices == 1) {
                    byteBuffer = masterBuffer;
                } else {
                    for (int i = 0; i < slices; i++) {
                        masterBuffer.limit(masterBuffer.position() + finalSize);
                        if (i < slices - 1) {
                            fastFree(masterBuffer.slice(), localCache);
                            masterBuffer.position(masterBuffer.position() + finalSize);
                        }
                    }
                    assert masterBuffer.position() + finalSize == masterBuffer.capacity();
                    masterBuffer.limit(masterBuffer.capacity());
                    byteBuffer = masterBuffer.slice();
                }
            } else {
                localCache.outstanding++;
            }
        }
        return byteBuffer;
    }

    private void fastFree(final ByteBuffer buffer, final LocalCache localCache) {
        buffer.clear();
        if (localCache.outstanding-- >= 16 || localCache.queue.size() == 16) {
            masterQueue.add(buffer);
        } else {
            localCache.queue.add(buffer);
        }
    }

    private void free(final ByteBuffer buffer) {
        if (direct != buffer.isDirect() || getSize() != buffer.capacity()) {
            IslandsLogger.getLogger().logError(new IllegalArgumentException("Wrong buffer returned to pool"));
        }
        fastFree(buffer, bufferQueue.get());
    }

    private int getSize() {
        return finalSize;
    }

    class LocalCache {

        private final ArrayDeque<ByteBuffer> queue = new ArrayDeque<>();
        private       int                    outstanding;

        void empty() {
            while (!queue.isEmpty()) {
                masterQueue.add(queue.poll());
            }
        }
    }
}
