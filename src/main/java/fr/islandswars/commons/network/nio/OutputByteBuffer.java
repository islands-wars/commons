package fr.islandswars.commons.network.nio;

import fr.islandswars.commons.network.NetOutput;
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
 * Created the 22/10/17 at 15:03
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
	public void writeBigInteger(BigInteger bInt) throws IOException {
		if (bInt == null)
			throw new IllegalArgumentException("BigInteger cannot be null!");
		var b = bInt.toByteArray();
		if (b[0] == 0)
			Arrays.copyOfRange(b, 1, b.length);
		writeVarInt(b.length);
		writeBytes(b);
	}

	@Override
	public void writeBoolean(boolean b) throws IOException {
		buffer.put(b ? (byte) 1 : 0);
	}

	@Override
	public void writeByte(int b) throws IOException {
		buffer.put((byte) b);
	}

	@Override
	public void writeBytes(byte[] b) throws IOException {
		buffer.put(b);
	}

	@Override
	public void writeBytes(byte[] b, int length) throws IOException {
		buffer.put(b, 0, length);
	}

	@Override
	public void writeChar(int c) throws IOException {
		buffer.putChar((char) c);
	}

	@Override
	public void writeDouble(double d) throws IOException {
		buffer.putDouble(d);
	}

	@Override
	public void writeFloat(float f) throws IOException {
		buffer.putFloat(f);
	}

	@Override
	public void writeInt(int i) throws IOException {
		buffer.putInt(i);
	}

	@Override
	public void writeInts(int[] i) throws IOException {
		writeInts(i, i.length);
	}

	@Override
	public void writeInts(int[] i, int length) throws IOException {
		for (var index = 0; index < length; index++)
			writeInt(i[index]);
	}

	@Override
	public void writeLong(long l) throws IOException {
		buffer.putLong(l);
	}

	@Override
	public void writeLongs(long[] l) throws IOException {
		writeLongs(l, l.length);
	}

	@Override
	public void writeLongs(long[] l, int length) throws IOException {
		for (var index = 0; index < length; index++)
			writeLong(l[index]);
	}

	@Override
	public void writeShort(int s) throws IOException {
		buffer.putShort((short) s);
	}

	@Override
	public void writeShorts(short[] s) throws IOException {
		writeShorts(s, s.length);
	}

	@Override
	public void writeShorts(short[] s, int length) throws IOException {
		for (var index = 0; index < length; index++)
			writeShort(s[index]);
	}

	@Override
	public void writeString(String s) throws IOException {
		if (s == null)
			throw new IllegalArgumentException("String cannot be null!");
		var bytes = s.getBytes(StandardCharsets.UTF_8);
		if (bytes.length > 32767)
			throw new IOException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
		else {
			writeVarInt(bytes.length);
			writeBytes(bytes);
		}
	}

	@Override
	public void writeUUID(UUID uuid) throws IOException {
		writeLong(uuid.getMostSignificantBits());
		writeLong(uuid.getLeastSignificantBits());
	}

	@Override
	public void writeVarInt(int i) throws IOException {
		while ((i & ~0x7F) != 0) {
			writeByte((i & 0x7F) | 0x80);
			i >>>= 7;
		}
		writeByte(i);
	}

	@Override
	public void writeVarLong(long l) throws IOException {
		while ((l & ~0x7F) != 0) {
			writeByte((int) (l & 0x7F) | 0x80);
			l >>>= 7;
		}
		writeByte((int) l);
	}
}
