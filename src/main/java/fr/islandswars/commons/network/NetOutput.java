package fr.islandswars.commons.network;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

/**
 * File <b>NetOutput</b> located on fr.islandswars.commons.network
 * NetOutput is a part of commons.
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
 * Created the 18/05/2024 at 21:40
 * @since 0.1
 */
public interface NetOutput {

    void writeBigInteger(BigInteger b) throws IOException;

    void writeBoolean(boolean b) throws IOException;

    void writeByte(int b) throws IOException;

    void writeBytes(byte[] b) throws IOException;

    void writeBytes(byte[] b, int length) throws IOException;

    void writeChar(int c) throws IOException;

    void writeDouble(double d) throws IOException;

    void writeFloat(float f) throws IOException;

    void writeInt(int i) throws IOException;

    void writeInts(int[] i) throws IOException;

    void writeInts(int[] i, int length) throws IOException;

    void writeLong(long l) throws IOException;

    void writeLongs(long[] l) throws IOException;

    void writeLongs(long[] l, int length) throws IOException;

    void writeShort(int s) throws IOException;

    void writeShorts(short[] s) throws IOException;

    void writeShorts(short[] s, int length) throws IOException;

    void writeString(String s) throws IOException;

    void writeUUID(UUID uuid) throws IOException;

    void writeVarInt(int i) throws IOException;

    void writeVarLong(long l) throws IOException;

}