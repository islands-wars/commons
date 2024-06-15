package fr.islandswars.commons.network.nio;

import fr.islandswars.commons.network.NetOutput;
import fr.islandswars.commons.utils.LogUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

/**
 * File <b>OutputByteBuffer</b> located on fr.islandswars.commons.network.nio
 * OutputByteBuffer is a part of commons.
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
 * Created the 18/05/2024 at 21:44
 * @since 0.1
 */
public class OutputByteBuffer implements NetOutput {

    private final ByteBuffer buffer;

    public OutputByteBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer getByteBuffer() {
        return buffer;
    }

    @Override
    public byte[] getBuffer() {
        byte[] usedBuffer = new byte[buffer.position()];
        buffer.flip(); // Prepare the buffer for reading
        buffer.get(usedBuffer);
        return usedBuffer;
    }

    @Override
    public void writeBigInteger(BigInteger bInt) {
        if (bInt == null)
            LogUtils.error(new IllegalArgumentException("BigInteger cannot be null!"));
        var b = bInt.toByteArray();
        if (b[0] == 0)
            Arrays.copyOfRange(b, 1, b.length);
        writeVarInt(b.length);
        writeBytes(b);
    }

    @Override
    public void writeBoolean(boolean b) {
        buffer.put(b ? (byte) 1 : 0);
    }

    @Override
    public void writeByte(int b) {
        buffer.put((byte) b);
    }

    @Override
    public void writeBytes(byte[] b) {
        buffer.put(b);
    }

    @Override
    public void writeBytes(byte[] b, int length) {
        buffer.put(b, 0, length);
    }

    @Override
    public void writeChar(int c) {
        buffer.putChar((char) c);
    }

    @Override
    public void writeDouble(double d) {
        buffer.putDouble(d);
    }

    @Override
    public void writeFloat(float f) {
        buffer.putFloat(f);
    }

    @Override
    public void writeInt(int i) {
        buffer.putInt(i);
    }

    @Override
    public void writeInts(int[] i) {
        writeInts(i, i.length);
    }

    @Override
    public void writeInts(int[] i, int length) {
        for (var index = 0; index < length; index++)
            writeInt(i[index]);
    }

    @Override
    public void writeLong(long l) {
        buffer.putLong(l);
    }

    @Override
    public void writeLongs(long[] l) {
        writeLongs(l, l.length);
    }

    @Override
    public void writeLongs(long[] l, int length) {
        for (var index = 0; index < length; index++)
            writeLong(l[index]);
    }

    @Override
    public void writeShort(int s) {
        buffer.putShort((short) s);
    }

    @Override
    public void writeShorts(short[] s) {
        writeShorts(s, s.length);
    }

    @Override
    public void writeShorts(short[] s, int length) {
        for (var index = 0; index < length; index++)
            writeShort(s[index]);
    }

    @Override
    public void writeString(String s) throws IOException {
        if (s == null)
            LogUtils.error(new IllegalArgumentException("String cannot be null!"));
        var bytes = s.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 32767)
            LogUtils.error(new IOException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")"));
        else {
            writeVarInt(bytes.length);
            writeBytes(bytes);
        }
    }

    @Override
    public void writeUUID(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    @Override
    public void writeVarInt(int i) {
        while ((i & ~0x7F) != 0) {
            writeByte((i & 0x7F) | 0x80);
            i >>>= 7;
        }
        writeByte(i);
    }

    @Override
    public void writeVarLong(long l) {
        while ((l & ~0x7F) != 0) {
            writeByte((int) (l & 0x7F) | 0x80);
            l >>>= 7;
        }
        writeByte((int) l);
    }
}