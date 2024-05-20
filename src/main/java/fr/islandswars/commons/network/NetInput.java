package fr.islandswars.commons.network;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

/**
 * File <b>NetInput</b> located on fr.islandswars.commons.network
 * NetInput is a part of commons.
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
 * Created the 18/05/2024 at 21:38
 * @since 0.1
 */
public interface NetInput {

    int available() throws IOException;

    BigInteger readBigInteger() throws IOException;

    boolean readBoolean() throws IOException;

    byte readByte() throws IOException;

    byte[] readBytes(int length) throws IOException;

    int readBytes(byte[] b) throws IOException;

    int readBytes(byte[] b, int offset, int length) throws IOException;

    char readChar() throws IOException;

    double readDouble() throws IOException;

    float readFloat() throws IOException;

    int readInt() throws IOException;

    int[] readInts(int length) throws IOException;

    int readInts(int[] i) throws IOException;

    int readInts(int[] i, int offset, int length) throws IOException;

    long readLong() throws IOException;

    long[] readLongs(int length) throws IOException;

    int readLongs(long[] l) throws IOException;

    int readLongs(long[] l, int offset, int length) throws IOException;

    short readShort() throws IOException;

    short[] readShorts(int length) throws IOException;

    int readShorts(short[] s) throws IOException;

    int readShorts(short[] s, int offset, int length) throws IOException;

    String readString() throws IOException;

    UUID readUUID() throws IOException;

    int readUnsignedByte() throws IOException;

    int readUnsignedShort() throws IOException;

    int readVarInt() throws IOException;

    long readVarLong() throws IOException;
}
