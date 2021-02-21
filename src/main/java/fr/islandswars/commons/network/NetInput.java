package fr.islandswars.commons.network;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

/**
 * File <b>NetInput</b> located on fr.islandswars.commons.network
 * NetInput is a part of commons.
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
 * Created the 22/10/17 at 14:58
 * @since 0.1
 */
public interface NetInput {

	/**
	 * Gets the number of available bytes.
	 *
	 * @return The number of available bytes.
	 * @throws IOException If an I/O error occurs.
	 */
	int available() throws IOException;

	/**
	 * Reads the next BigInteger.
	 *
	 * @return The next BigInteger.
	 * @throws IOException If an I/O error occurs.
	 */
	BigInteger readBigInteger() throws IOException;

	/**
	 * Reads the next boolean.
	 *
	 * @return The next boolean.
	 * @throws IOException If an I/O error occurs.
	 */
	boolean readBoolean() throws IOException;

	/**
	 * Reads the next byte.
	 *
	 * @return The next byte.
	 * @throws IOException If an I/O error occurs.
	 */
	byte readByte() throws IOException;

	/**
	 * Reads the next byte array.
	 *
	 * @param length The length of the byte array.
	 * @return The next byte array.
	 * @throws IOException If an I/O error occurs.
	 */
	byte[] readBytes(int length) throws IOException;

	/**
	 * Reads as much data as possible into the given byte array.
	 *
	 * @param b Byte array to read to.
	 * @return The amount of bytes read, or -1 if no bytes could be read.
	 * @throws IOException If an I/O error occurs.
	 */
	int readBytes(byte b[]) throws IOException;

	/**
	 * Reads the given amount of bytes into the given array at the given offset.
	 *
	 * @param b      Byte array to read to.
	 * @param offset Offset of the array to read to.
	 * @param length Length of bytes to read.
	 * @return The amount of bytes read, or -1 if no bytes could be read.
	 * @throws IOException If an I/O error occurs.
	 */
	int readBytes(byte b[], int offset, int length) throws IOException;

	/**
	 * Reads the next char.
	 *
	 * @return The next char.
	 * @throws IOException If an I/O error occurs.
	 */
	char readChar() throws IOException;

	/**
	 * Reads the next double.
	 *
	 * @return The next double.
	 * @throws IOException If an I/O error occurs.
	 */
	double readDouble() throws IOException;

	/**
	 * Reads the next float.
	 *
	 * @return The next float.
	 * @throws IOException If an I/O error occurs.
	 */
	float readFloat() throws IOException;

	/**
	 * Reads the next integer.
	 *
	 * @return The next integer.
	 * @throws IOException If an I/O error occurs.
	 */
	int readInt() throws IOException;

	/**
	 * Reads the next int array.
	 *
	 * @param length The length of the int array.
	 * @return The next int array.
	 * @throws IOException If an I/O error occurs.
	 */
	int[] readInts(int length) throws IOException;

	/**
	 * Reads as much data as possible into the given int array.
	 *
	 * @param i Int array to read to.
	 * @return The amount of ints read, or -1 if no ints could be read.
	 * @throws IOException If an I/O error occurs.
	 */
	int readInts(int i[]) throws IOException;

	/**
	 * Reads the given amount of ints into the given array at the given offset.
	 *
	 * @param i      Int array to read to.
	 * @param offset Offset of the array to read to.
	 * @param length Length of bytes to read.
	 * @return The amount of ints read, or -1 if no ints could be read.
	 * @throws IOException If an I/O error occurs.
	 */
	int readInts(int i[], int offset, int length) throws IOException;

	/**
	 * Reads the next long.
	 *
	 * @return The next long.
	 * @throws IOException If an I/O error occurs.
	 */
	long readLong() throws IOException;

	/**
	 * Reads the next long array.
	 *
	 * @param length The length of the long array.
	 * @return The next long array.
	 * @throws IOException If an I/O error occurs.
	 */
	long[] readLongs(int length) throws IOException;

	/**
	 * Reads as much data as possible into the given long array.
	 *
	 * @param l Long array to read to.
	 * @return The amount of longs read, or -1 if no longs could be read.
	 * @throws IOException If an I/O error occurs.
	 */
	int readLongs(long l[]) throws IOException;

	/**
	 * Reads the given amount of longs into the given array at the given offset.
	 *
	 * @param l      Long array to read to.
	 * @param offset Offset of the array to read to.
	 * @param length Length of bytes to read.
	 * @return The amount of longs read, or -1 if no longs could be read.
	 * @throws IOException If an I/O error occurs.
	 */
	int readLongs(long l[], int offset, int length) throws IOException;

	/**
	 * Reads the next short.
	 *
	 * @return The next short.
	 * @throws IOException If an I/O error occurs.
	 */
	short readShort() throws IOException;

	/**
	 * Reads the next short array.
	 *
	 * @param length The length of the short array.
	 * @return The next short array.
	 * @throws IOException If an I/O error occurs.
	 */
	short[] readShorts(int length) throws IOException;

	/**
	 * Reads as much data as possible into the given short array.
	 *
	 * @param s Short array to read to.
	 * @return The amount of shorts read, or -1 if no shorts could be read.
	 * @throws IOException If an I/O error occurs.
	 */
	int readShorts(short s[]) throws IOException;

	/**
	 * Reads the given amount of shorts into the given array at the given offset.
	 *
	 * @param s      Short array to read to.
	 * @param offset Offset of the array to read to.
	 * @param length Length of bytes to read.
	 * @return The amount of shorts read, or -1 if no shorts could be read.
	 * @throws IOException If an I/O error occurs.
	 */
	int readShorts(short s[], int offset, int length) throws IOException;

	/**
	 * Reads the next string.
	 *
	 * @return The next string.
	 * @throws IOException If an I/O error occurs.
	 */
	String readString() throws IOException;

	/**
	 * Reads the next UUID.
	 *
	 * @return The next UUID.
	 * @throws IOException If an I/O error occurs.
	 */
	UUID readUUID() throws IOException;

	/**
	 * Reads the next unsigned byte.
	 *
	 * @return The next unsigned byte.
	 * @throws IOException If an I/O error occurs.
	 */
	int readUnsignedByte() throws IOException;

	/**
	 * Reads the next unsigned short.
	 *
	 * @return The next unsigned short.
	 * @throws IOException If an I/O error occurs.
	 */
	int readUnsignedShort() throws IOException;

	/**
	 * Reads the next varint. A varint is a form of integer where only necessary bytes are written. This is done to save bandwidth.
	 *
	 * @return The next varint.
	 * @throws IOException If an I/O error occurs.
	 */
	int readVarInt() throws IOException;

	/**
	 * Reads the next varlong. A varlong is a form of long where only necessary bytes are written. This is done to save bandwidth.
	 *
	 * @return The next varlong.
	 * @throws IOException If an I/O error occurs.
	 */
	long readVarLong() throws IOException;
}
