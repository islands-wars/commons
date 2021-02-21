package fr.islandswars.commons.network.nio;

import fr.islandswars.commons.network.NetInput;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * File <b>InputByteBuffer</b> located on fr.islandswars.commons.network.nio
 * InputByteBuffer is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2021 Islands Wars.
 * <p>
 * Islands Wars - Commons is free software: you can redistribute it and/or modify
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
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">GNU GPL license</a>.
 * <p>
 *
 * @author Valentin Burgaud (Xharos), {@literal <xharos@islandswars.fr>}
 * Created the 22/10/17 at 15:02
 * @since 0.1
 */
public class InputByteBuffer implements NetInput {

	private final ByteBuffer buffer;

	public InputByteBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public int available() throws IOException {
		return buffer.remaining();
	}

	@Override
	public BigInteger readBigInteger() throws IOException {
		var b = readBytes(readVarInt());
		return new BigInteger(1, b);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return buffer.get() == 1;
	}

	@Override
	public byte readByte() throws IOException {
		return buffer.get();
	}

	@Override
	public byte[] readBytes(int length) throws IOException {
		if (length < 0)
			throw new IllegalArgumentException("Array cannot have length less than 0.");
		var b = new byte[length];
		buffer.get(b);
		return b;
	}

	@Override
	public int readBytes(byte[] b) throws IOException {
		return readBytes(b, 0, b.length);
	}

	@Override
	public int readBytes(byte[] b, int offset, int length) throws IOException {
		int readable = buffer.remaining();
		if (readable <= 0)
			return -1;
		if (readable < length)
			length = readable;
		buffer.get(b, offset, length);
		return length;
	}

	@Override
	public char readChar() throws IOException {
		return buffer.getChar();
	}

	@Override
	public double readDouble() throws IOException {
		return buffer.getDouble();
	}

	@Override
	public float readFloat() throws IOException {
		return buffer.getFloat();
	}

	@Override
	public int readInt() throws IOException {
		return buffer.getInt();
	}

	@Override
	public int[] readInts(int length) throws IOException {
		if (length < 0)
			throw new IllegalArgumentException("Array cannot have length less than 0.");
		var i = new int[length];
		for (var index = 0; index < length; index++)
			i[index] = readInt();
		return i;
	}

	@Override
	public int readInts(int[] i) throws IOException {
		return readInts(i, 0, i.length);
	}

	@Override
	public int readInts(int[] i, int offset, int length) throws IOException {
		var readable = buffer.remaining();
		if (readable <= 0)
			return -1;
		if (readable < length * 4)
			length = readable / 4;
		for (var index = offset; index < offset + length; index++)
			i[index] = readInt();
		return length;
	}

	@Override
	public long readLong() throws IOException {
		return buffer.getLong();
	}

	@Override
	public long[] readLongs(int length) throws IOException {
		if (length < 0)
			throw new IllegalArgumentException("Array cannot have length less than 0.");
		var l = new long[length];
		for (var index = 0; index < length; index++)
			l[index] = readLong();
		return l;
	}

	@Override
	public int readLongs(long[] l) throws IOException {
		return readLongs(l, 0, l.length);
	}

	@Override
	public int readLongs(long[] l, int offset, int length) throws IOException {
		var readable = buffer.remaining();
		if (readable <= 0)
			return -1;
		if (readable < length * 2)
			length = readable / 2;
		for (var index = offset; index < offset + length; index++)
			l[index] = readLong();
		return length;
	}

	@Override
	public short readShort() throws IOException {
		return buffer.getShort();
	}

	@Override
	public short[] readShorts(int length) throws IOException {
		if (length < 0)
			throw new IllegalArgumentException("Array cannot have length less than 0.");
		var s = new short[length];
		for (var index = 0; index < length; index++)
			s[index] = readShort();
		return s;
	}

	@Override
	public int readShorts(short[] s) throws IOException {
		return readShorts(s, 0, s.length);
	}

	@Override
	public int readShorts(short[] s, int offset, int length) throws IOException {
		var readable = buffer.remaining();
		if (readable <= 0)
			return -1;
		if (readable < length * 2)
			length = readable / 2;
		for (var index = offset; index < offset + length; index++)
			s[index] = readShort();
		return length;
	}

	@Override
	public String readString() throws IOException {
		var length = readVarInt();
		var bytes  = readBytes(length);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	@Override
	public UUID readUUID() throws IOException {
		return new UUID(readLong(), readLong());
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return buffer.get() & 0xFF;
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return buffer.getShort() & 0xFFFF;
	}

	@Override
	public int readVarInt() throws IOException {
		var value = 0;
		var size  = 0;
		int b;
		while (((b = readByte()) & 0x80) == 0x80) {
			value |= (b & 0x7F) << (size++ * 7);
			if (size > 5)
				throw new IOException("VarInt too long (length must be <= 5)");
		}
		return value | ((b & 0x7F) << (size * 7));
	}

	@Override
	public long readVarLong() throws IOException {
		var value = 0;
		var size  = 0;
		int b;
		while (((b = readByte()) & 0x80) == 0x80) {
			value |= (long) (b & 0x7F) << (size++ * 7);
			if (size > 10)
				throw new IOException("VarLong too long (length must be <= 10)");
		}
		return value | ((long) (b & 0x7F) << (size * 7));
	}

	public ByteBuffer getByteBuffer() {
		return buffer;
	}
}

