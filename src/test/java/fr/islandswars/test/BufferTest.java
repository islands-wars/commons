package fr.islandswars.test;

import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.network.nio.ByteBufferPool;
import fr.islandswars.commons.network.nio.InputByteBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * File <b>BufferTest</b> located on fr.islandswars.test
 * BufferTest is a part of commons.
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
 * Created the 15/06/2024 at 19:30
 * @since 0.3
 */
public class BufferTest {

    private       ByteBufferPool POOL = new ByteBufferPool(1024, true);
    private       NetOutput      output;
    private final int            i    = 55;
    private final double         d    = 55.2d;
    private final float          f    = 1.3f;
    private final long           l    = 8978;
    private final short          s    = 1;
    private final byte           b    = 1;
    private final String         st   = "coucou";
    private final char           c    = 'e';
    private final boolean        bo   = true;
    private final UUID           u    = UUID.randomUUID();
    private final BigInteger     bi   = new BigInteger("12345678998654578563245");

    @BeforeEach
    public void setup() throws IOException {
        this.output = POOL.allocateNetOutput();
        output.writeInt(i);
        output.writeDouble(d);
        output.writeFloat(f);
        output.writeLong(l);
        output.writeShort(s);
        output.writeByte(b);
        output.writeString(st);
        output.writeChar(c);
        output.writeBoolean(bo);
        output.writeUUID(u);
        output.writeBigInteger(bi);
    }

    @Test
    public void writeData() {
        assertEquals(64, output.getBuffer().length);
    }

    @Test
    public void readData() throws IOException {
        var buffer = output.getBuffer();
        var input  = (InputByteBuffer) POOL.allocateNetInput();
        input.getByteBuffer().put(buffer);
        input.getByteBuffer().flip();

        assertEquals(i, input.readInt());
        assertEquals(d, input.readDouble());
        assertEquals(f, input.readFloat());
        assertEquals(l, input.readLong());
        assertEquals(s, input.readShort());
        assertEquals(b, input.readByte());
        assertEquals(st, input.readString());
        assertEquals(c, input.readChar());
        assertEquals(bo, input.readBoolean());
        assertEquals(u, input.readUUID());
        assertEquals(bi, input.readBigInteger());
        assertEquals(input.getByteBuffer().limit(), input.getByteBuffer().position());
    }
}
