package org.interledger.encoding;

import java.io.EOFException;

import java.io.IOException;
import java.io.InputStream;

/**
 * OER input stream reads OER encoded data from an underlying stream
 *
 * Limitations - INTEGER types are only supported up to 3 bytes (UNSIGNED)
 *
 * @author adrianhopebailie
 *
 */
public class OerInputStream extends InputStream {

    protected final InputStream stream;

    public OerInputStream(InputStream stream) {
        this.stream = stream;
    }

    public int read8BitUInt() throws IOException {
        int value = stream.read();
        verifyNotEOF(value);
        return value;
    }

    public int read16BitUInt() throws IOException {

        int value = stream.read();
        verifyNotEOF(value);
        int next = stream.read();
        verifyNotEOF(next);

        return next + (value << 8);
    }

    public long read32BitUInt() throws IOException {
        // TODO: UnitTest read32BitUInt/write32BitUInt
        int byte4 = stream.read();
        verifyNotEOF(byte4);
        int byte3 = stream.read();
        verifyNotEOF(byte3);
        int byte2 = stream.read();
        verifyNotEOF(byte2);
        int byte1 = stream.read();
        verifyNotEOF(byte1);
        return byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24);
    }

    public int readVarUInt() throws IOException {

        // We only support a 3 byte length indicator otherwise we go beyond
        // Integer.MAX_SIZE
        int length = readLengthIndicator();
        int value = stream.read();
        verifyNotEOF(value);

        if (length == 1) {
            return value;
        } else if (length == 2) {
            int next = stream.read();
            verifyNotEOF(next);
            return value + (next << 8);
        } else if (length == 3) {
            int next = stream.read();
            verifyNotEOF(next);
            value += (next << 8);
            next = stream.read();
            verifyNotEOF(next);
            return value + (next << 16);
        } else {
            throw new IllegalArgumentException("Integers of greater than 16777215 (3 bytes) are not supported.");
        }

    }

    public byte[] readOctetString() throws IOException {
        int length = readLengthIndicator();
        if (length == 0) {
            return new byte[]{};
        }
        byte[] value = new byte[length];
        int bytesRead = 0;
        bytesRead = stream.read(value, 0, length);

        if (bytesRead < length) {
            throw new EOFException("Unexpected EOF when trying to decode OER data.");
        }
        return value;
    }

    @Override
    public int read() throws IOException {
        return this.stream.read();
    }

    protected int readLengthIndicator()
            throws IOException {
        int length = stream.read();
System.out.println("deteleme:  byte length = (byte) stream.read():"+length);

        verifyNotEOF(length);

        if (length < 128) {
            return length;
        } else if (length > 128) {
            int lengthOfLength = length - 128;
            if (lengthOfLength > 3) {
                throw new RuntimeException("This implementation only supports "
                        + "variable length fields up to 16777215 bytes.");
            }
            length = 0;
            for (int i = lengthOfLength; i > 0; i--) {
                int next = stream.read();
                verifyNotEOF(next);
                length += (next << (8 * (i - 1)));
            }
            return length;
        } else {
            throw new RuntimeException("First byte of length indicator can't be 0x80.");
        }
    }

    protected void verifyNotEOF(int data) throws EOFException {
        if (data == -1) {
            throw new EOFException("Unexpected EOF when trying to decode OER data.");
        }
    }

}
