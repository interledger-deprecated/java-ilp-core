package org.interledger.encoding;

import java.io.IOException;

import java.io.OutputStream;

/**
 * OER input stream writes OER encoded data to an underlying stream
 *
 * Limitations - INTEGER types are only supported up to 3 bytes (UNSIGNED)
 *
 * @author adrianhopebailie
 *
 */
public class OerOutputStream extends OutputStream {

    protected final OutputStream stream;

    public OerOutputStream(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public void write(int b) throws IOException {
        stream.write(b);
    }

    public void write8BitUInt(int value) throws IOException {

        if (value > 255) {
            throw new IllegalArgumentException(Integer.toString(value) + "is greater than 8 bits.");
        }

        stream.write(value);
    }

    public void write16BitUInt(int value) throws IOException {

        if (value > 65535) {
            throw new IllegalArgumentException(Integer.toString(value) + "is greater than 16 bits.");
        }

        stream.write((value >> 8));
        stream.write(value);
    }

    public void write32BitUInt(long value) throws IOException {
        if (value > ((1<<32)-1)) {
            throw new IllegalArgumentException(Long.toString(value) + "is greater than 32 bits.");
        }
        stream.write((byte)(value>>24 & 255));
        stream.write((byte)(value>>16 & 255));
        stream.write((byte)(value>> 8 & 255));
        stream.write((byte)(value>> 0 & 255));
    }

    public void writeVarUInt(int value) throws IOException {
        // We only support a 3 byte length indicator otherwise we go beyond
        // Integer.MAX_SIZE

        // TODO add some safe checks
        if (value <= 255) {
            stream.write(1);
            stream.write(value);
        } else if (value <= 65535) {
            stream.write(2);
            stream.write((value >> 8));
            stream.write(value);
        } else if (value <= 16777215) {
            stream.write(3);
            stream.write((value >> 16));
            stream.write((value >> 8));
            stream.write(value);
        } else {
            throw new IllegalArgumentException("Integers of greater than 16777215 are not supported.");
        }

    }

    public void writeOctetString(byte[] bytes) throws IOException {
        writeLengthIndicator(bytes.length);

        stream.write(bytes);
    }

    protected void writeLengthIndicator(int length) throws IOException {

        if (length < 128) {
            stream.write(length);
        } else if (length <= 255) {
            //Write length of length byte "1000 0001"
            stream.write(128 + 1);
            stream.write(length);
        } else if (length <= 65535) {
            //Write length of length byte "1000 0010"
            stream.write(128 + 2);
            stream.write((length >> 8));
            stream.write(length);
        } else if (length <= 16777215) {
            //Write length of length byte "1000 0011"
            stream.write(128 + 3);
            stream.write((length >> 16));
            stream.write((length >> 8));
            stream.write(length);
        } else {
            throw new IllegalArgumentException("Field lengths of greater than 16777215 are not supported.");
        }
    }

    /**
     * Flushes the stream. This will write any buffered output bytes and flush
     * through to the underlying stream.
     *
     * @throws IOException If an I/O error has occurred.
     */
    public void flush() throws IOException {
        stream.flush();
    }

    /**
     * Closes the stream. This method must be called to release any resources
     * associated with the stream.
     *
     * @throws IOException If an I/O error has occurred.
     */
    public void close() {
        try {
            flush();
            stream.close();
        } catch(Exception e) {
            // TODO: Improvement. Inject Logger.
            System.out.println("WARN: Couldn't properly close the stream due to "+
                e.toString()+", Some data could have not been flushed to disk");
        }
    }

}
