package com.clougence.clouddm.worker.component.result;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.function.Consumer;

import com.clougence.utils.ObjectUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.io.bytes.BytesIO;

public abstract class ResultSetOutputStream extends OutputStream {

    private final ByteOrder byteOrder;
    private long            lastRowHeaderPos    = 0;
    private long            lastDataHeadPos     = 0;
    private long            currentPos          = 0;
    //
    private long            requiredBytes       = 0;
    private boolean         writingRow          = false;
    private boolean         writingData         = false;
    private boolean         writingDataAsStream = false;
    private long            counterRows         = 0;
    private long            counterData         = 0;

    public ResultSetOutputStream(ByteOrder order){
        this.byteOrder = order == null ? ByteOrder.BIG_ENDIAN : order;
    }

    protected void initStream(boolean append) throws IOException {
        long initPos = this.initFilePosition();

        if (!append) {
            this.setLength(8);
            this.currentPos = 8;
            initPos = 8;
        }

        this.lastRowHeaderPos = initPos;
        this.lastDataHeadPos = initPos;
        this.currentPos = initPos;
    }

    protected abstract long initFilePosition() throws IOException;

    protected abstract void setBytes(long offset, byte b) throws IOException;

    protected abstract void setBytes(long offset, byte[] b, int off, int length) throws IOException;

    protected abstract void setLength(long length) throws IOException;

    @Override
    public void close() throws IOException {
        this.closeRow();
        this.flush();
    }

    @Override
    public void flush() throws IOException {
        byte[] lenBytes = buildBytes(8, bb -> {
            bb.writeInt64(this.counterRows);
        });
        this.setBytes(0, lenBytes, 0, lenBytes.length);
    }

    //
    // for write row
    //

    public void newRow() throws IOException {
        if (this.writingRow) {
            this.closeRow();
        }

        this.lastRowHeaderPos = this.currentPos;

        this.setLength(this.currentPos + 17);
        this.currentPos += 17;

        this.writingRow = true;
        this.counterRows++;
        this.counterData = 0;
    }

    public void closeRow() throws IOException {
        if (this.writingRow) {
            this.finishData();

            byte[] lenBytes = buildBytes(17, bb -> {
                bb.writeByte((byte) 0);// row tag
                bb.writeInt64(this.counterData);// row dataCount
                bb.writeInt64(this.currentPos - this.lastRowHeaderPos);
            });
            this.setBytes(this.lastRowHeaderPos, lenBytes, 0, lenBytes.length);

            this.writingRow = false;
            this.counterData = 0;
        }
    }

    public void discardRow() throws IOException {
        if (this.writingRow) {
            if (this.currentPos != this.lastRowHeaderPos) {
                this.setLength(this.lastRowHeaderPos);
            }

            this.currentPos = this.lastRowHeaderPos;
            this.requiredBytes = 0;
            this.writingRow = false;
            this.writingData = false;
            this.writingDataAsStream = false;
            this.counterRows--;
            this.counterData = 0;
        }
    }

    //
    // for write data
    //

    private void checkNextData(long length) throws IOException {
        ObjectUtils.checkPositiveOrZero(length, "length must be positive.");

        if (!this.writingRow) {
            throw new IOException("no row is being written.");
        }

        if (this.writingData) {
            throw new IOException("the previous data writing has not finish.");
        }
    }

    private byte[] buildDataHeader(byte tag, byte typeHeader, boolean isNull, long dataLength) {
        byte[] headerNoLength = new byte[] { (byte) (0xFF & (typeHeader >> 4)),//
                                             (byte) (0xFF & (typeHeader << 4)),// 
                                             tag                               //
        };

        if (isNull) {
            return headerNoLength;                                // 0 byte for length
        }

        if (dataLength <= 0xFFL) {
            headerNoLength[1] = (byte) (headerNoLength[1] | 0x01);// 1 byte for length
            return buildBytes(3 + 1, bb -> {
                bb.writeBytes(headerNoLength);
                bb.writeByte((byte) dataLength);
            });
        } else if (dataLength <= 0xFFFFL) {
            headerNoLength[1] = (byte) (headerNoLength[1] | 0x02);// 2 byte for length
            return buildBytes(3 + 2, bb -> {
                bb.writeBytes(headerNoLength);
                bb.writeInt16((short) dataLength);
            });
        } else if (dataLength <= 0xFFFFFFL) {
            headerNoLength[1] = (byte) (headerNoLength[1] | 0x03);// 3 byte for length
            return buildBytes(3 + 3, bb -> {
                bb.writeBytes(headerNoLength);
                bb.writeInt24((int) dataLength);
            });
        } else if (dataLength <= 0xFFFFFFFFL) {
            headerNoLength[1] = (byte) (headerNoLength[1] | 0x04);// 4 byte for length
            return buildBytes(3 + 4, bb -> {
                bb.writeBytes(headerNoLength);
                bb.writeUInt32(dataLength);
            });
        } else {
            headerNoLength[1] = (byte) (headerNoLength[1] | 0x08);// 8 byte for length
            return buildBytes(3 + 8, bb -> {
                bb.writeBytes(headerNoLength);
                bb.writeInt64(dataLength);
            });
        }
    }

    protected void writeDataHeaderWithNull(byte tag, byte typeHeader) throws IOException {
        this.checkNextData(0);

        this.lastDataHeadPos = this.currentPos;

        byte[] header = this.buildDataHeader(tag, typeHeader, true, 0);
        this.setBytes(this.currentPos, header, 0, header.length);
        this.currentPos += header.length;

        this.requiredBytes = 0;
        this.writingData = true;
        this.writingDataAsStream = false;
        this.counterData++;
    }

    protected void writeDataHeaderWithSize(byte tag, byte typeHeader, long dataLength) throws IOException {
        this.checkNextData(dataLength);

        this.lastDataHeadPos = this.currentPos;

        byte[] header = this.buildDataHeader(tag, typeHeader, false, dataLength);
        this.setBytes(this.currentPos, header, 0, header.length);
        this.currentPos += header.length;

        this.requiredBytes = dataLength;
        this.writingData = true;
        this.writingDataAsStream = false;
        this.counterData++;
    }

    protected void writeDataHeaderWithStream(byte tag, byte typeHeader) throws IOException {
        this.checkNextData(0);

        this.lastDataHeadPos = this.currentPos;

        byte[] header = this.buildDataHeader(tag, typeHeader, false, Long.MAX_VALUE);
        this.setBytes(this.currentPos, header, 0, header.length);
        this.currentPos += header.length;

        this.requiredBytes = -1;
        this.writingData = true;
        this.writingDataAsStream = true;
        this.counterData++;
    }

    private byte[] buildBytes(int size, Consumer<BytesIO> v) {
        byte[] bytes = new byte[size];
        BytesIO bb = BytesIO.wrap(bytes, true);
        bb = bb.order(this.byteOrder);
        v.accept(bb);
        return bytes;
    }

    //
    // for stream
    //

    private void requiredWritingData() throws IOException {
        if (!this.writingData) {
            throw new IOException("no data in written.");
        }
    }

    /** append data to file */
    public void write(byte b) throws IOException {
        this.requiredWritingData();

        if (!this.writingDataAsStream) {
            if (this.requiredBytes == 0) {
                throw new IOException("data length exceeds required bytes: 1 > " + this.requiredBytes);
            }
        }

        this.setBytes(this.currentPos, b);
        this.currentPos += 1;
        this.requiredBytes -= 1;
    }

    /** append data to file */
    @Override
    public void write(int b) throws IOException {
        this.write((byte) b);
    }

    /** append data to file */
    @Override
    public void write(byte[] b) throws IOException {
        this.requiredWritingData();

        if (!this.writingDataAsStream) {
            if (b.length > this.requiredBytes) {
                throw new IOException("data length exceeds required bytes: " + b.length + " > " + this.requiredBytes);
            }
        }

        this.setBytes(this.currentPos, b, 0, b.length);
        this.currentPos += b.length;
        this.requiredBytes -= b.length;
    }

    /** append data to file */
    @Override
    public void write(byte[] b, int offset, int length) throws IOException {
        this.requiredWritingData();

        if (!this.writingDataAsStream) {
            if (length > (b.length - offset)) {
                throw new IOException("there is not enough data in the parameters.");
            }
            if (length > this.requiredBytes) {
                throw new IOException("data length exceeds required bytes: " + length + " > " + this.requiredBytes);
            }
        }

        this.setBytes(this.currentPos, b, offset, length);
        this.currentPos += length;
        this.requiredBytes -= length;
    }

    //
    // for write objects
    //

    public void finishData() throws IOException {
        if (this.writingData) {
            if (this.writingDataAsStream) {
                long dataLen = this.currentPos - this.lastDataHeadPos + 10; // 10 bytes for data header(2 headers + 8 bytes for length )
                byte[] lenBytes = buildBytes(8, bb -> bb.writeInt64(dataLen));

                long dataLenHeader = this.currentPos - this.lastDataHeadPos + 2;// 2 bytes for data header
                this.setBytes(dataLenHeader, lenBytes, 0, lenBytes.length);
            } else {
                this.currentPos += this.requiredBytes;
                this.setLength(this.currentPos);
            }

            this.requiredBytes = 0;
            this.writingData = false;
            this.writingDataAsStream = false;
        }
    }

    public void discardData() throws IOException {
        if (this.writingData) {
            if (this.currentPos != this.lastDataHeadPos) {
                this.setLength(this.lastDataHeadPos);

            }

            this.currentPos = this.lastDataHeadPos;
            this.requiredBytes = 0;
            this.writingData = false;
            this.writingDataAsStream = false;
            this.counterData--;
        }
    }

    private byte[] lengthBytes(long dataLength) {
        ObjectUtils.checkPositiveOrZero(dataLength, "dataLength must be positive.");
        if (dataLength <= 0xFFL) {
            return buildBytes(2, bb -> {
                bb.writeByte((byte) 0x01);      // 1 byte for length
                bb.writeByte((byte) dataLength);
            });
        } else if (dataLength <= 0xFFFFL) {
            return buildBytes(3, bb -> {
                bb.writeByte((byte) 0x02);      // 2 byte for length
                bb.writeInt16((short) dataLength);
            });
        } else if (dataLength <= 0xFFFFFFL) {
            return buildBytes(4, bb -> {
                bb.writeByte((byte) 0x03);      // 3 byte for length
                bb.writeInt24((int) dataLength);
            });
        } else if (dataLength <= 0xFFFFFFFFL) {
            return buildBytes(5, bb -> {
                bb.writeByte((byte) 0x04);      // 4 byte for length
                bb.writeUInt32(dataLength);
            });
        } else {
            return buildBytes(9, bb -> {
                bb.writeByte((byte) 0x08);      // 8 byte for length
                bb.writeInt64(dataLength);
            });
        }
    }

    public void writeBoolean(byte tag, Boolean v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Boolean);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Boolean, 1);
                this.write((byte) (v ? 1 : 0));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeByte(byte tag, Byte v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Byte);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Byte, 1);
                this.write(v);
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeShort(byte tag, Short v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Short);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Short, 2);
                this.write(buildBytes(2, bb -> bb.writeInt16(v)));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeInteger(byte tag, Integer v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Integer);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Integer, 4);
                this.write(buildBytes(4, bb -> bb.writeInt32(v)));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeLong(byte tag, Long v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Long);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Long, 8);
                this.write(buildBytes(8, bb -> bb.writeInt64(v)));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeBigInteger(byte tag, BigInteger v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.BigInteger);
            } else {
                byte[] dataBytes = v.toByteArray();
                this.writeDataHeaderWithSize(tag, EntityType.BigInteger, dataBytes.length);
                this.write(dataBytes);
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeBigDecimal(byte tag, BigDecimal v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.BigDecimal);
            } else {
                byte[] decData = v.unscaledValue().toByteArray();
                byte[] dataBytes = buildBytes(4 + decData.length, bb -> {
                    bb.writeInt32(v.scale());
                    bb.writeBytes(decData);
                });
                this.writeDataHeaderWithSize(tag, EntityType.BigDecimal, dataBytes.length);
                this.write(dataBytes);
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeFloat(byte tag, Float v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Float);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Float, 4);
                this.write(buildBytes(4, bb -> bb.writeFloat32(v)));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeDouble(byte tag, Double v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Double);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Double, 8);
                this.write(buildBytes(8, bb -> bb.writeFloat64(v)));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeBytes(byte tag, byte[] v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Bytes);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Bytes, v.length);
                this.write(v);
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeBytes(byte tag, byte[] v, int offset, int length) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Bytes);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Bytes, length);
                this.write(v, offset, length);
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeString(byte tag, String v, Charset charset) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.String);
            } else {
                charset = (Charset) ObjectUtils.defaultIfNull(charset, Charset.defaultCharset());
                byte[] nameLenBytes;
                byte[] nameBytes;
                byte[] dataLenBytes;
                byte[] dataBytes;

                nameBytes = charset.name().getBytes(StandardCharsets.US_ASCII);
                nameLenBytes = lengthBytes(nameBytes.length);
                dataBytes = v.getBytes(charset);
                dataLenBytes = lengthBytes(dataBytes.length);

                long length = nameLenBytes.length + nameBytes.length + dataLenBytes.length + dataBytes.length;
                this.writeDataHeaderWithSize(tag, EntityType.String, length);

                this.write(nameLenBytes);
                this.write(nameBytes);
                this.write(dataLenBytes);
                this.write(dataBytes);
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeLocalDate(byte tag, LocalDate v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Date);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Date, 8);
                this.write(buildBytes(8, bb -> bb.writeInt64(v.toEpochDay())));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeLocalTime(byte tag, LocalTime v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Time);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.Time, 8);
                this.write(buildBytes(8, bb -> bb.writeInt64(v.toNanoOfDay())));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeOffsetTime(byte tag, OffsetTime v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.TimeZ);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.TimeZ, 12);
                this.write(buildBytes(12, bb -> {
                    bb.writeInt64(v.toLocalTime().toNanoOfDay());
                    bb.writeInt32(v.getOffset().getTotalSeconds());
                }));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeLocalDateTime(byte tag, LocalDateTime v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.DateTime);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.DateTime, 16);
                this.write(buildBytes(16, bb -> {
                    bb.writeInt64(v.toLocalDate().toEpochDay());
                    bb.writeInt64(v.toLocalTime().toNanoOfDay());
                }));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeOffsetDateTime(byte tag, OffsetDateTime v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.DateTimeZ);
            } else {
                this.writeDataHeaderWithSize(tag, EntityType.DateTimeZ, 20);
                this.write(buildBytes(20, bb -> {
                    bb.writeInt64(v.toLocalDate().toEpochDay());
                    bb.writeInt64(v.toLocalTime().toNanoOfDay());
                    bb.writeInt32(v.getOffset().getTotalSeconds());
                }));
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeCodeStatus(byte tag, int code) throws IOException {
        try {
            this.writeDataHeaderWithSize(tag, EntityType.Code, 4);
            this.write(buildBytes(4, bb -> bb.writeInt32(code)));

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeString(byte tag, Reader v, Charset charset) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.String);
            } else {
                charset = (Charset) ObjectUtils.defaultIfNull(charset, Charset.defaultCharset());
                byte[] nameBytes = charset.name().getBytes(StandardCharsets.US_ASCII);
                byte[] nameLenBytes = lengthBytes(nameBytes.length);

                this.writeDataHeaderWithStream(tag, EntityType.String);
                this.write(nameLenBytes);
                this.write(nameBytes);

                long dataLengthPos = this.currentPos;
                this.write(new byte[9]); // reserve 9 bytes for data length.
                try (Writer out = new OutputStreamWriter(this, charset)) {
                    IOUtils.copy(v, out);
                    out.flush();
                }

                long dataLength = this.currentPos - dataLengthPos - 9;
                byte[] dataLenBytes = lengthBytes(dataLength);
                this.setBytes(dataLengthPos, dataLenBytes, 0, dataLenBytes.length);
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }

    public void writeBytes(byte tag, InputStream v) throws IOException {
        try {
            if (v == null) {
                this.writeDataHeaderWithNull(tag, EntityType.Bytes);
            } else {
                this.writeDataHeaderWithStream(tag, EntityType.Bytes);

                long dataLengthPos = this.currentPos;
                this.write(new byte[9]); // reserve 9 bytes for data length.
                IOUtils.copy(v, this);

                long dataLength = this.currentPos - dataLengthPos - 9;
                byte[] dataLenBytes = buildBytes(9, bb -> {
                    bb.writeByte((byte) 0x08); // 8 byte for length
                    bb.writeInt64(dataLength);
                });
                this.setBytes(dataLengthPos, dataLenBytes, 0, dataLenBytes.length);
            }

            this.finishData();
        } catch (IOException e) {
            this.discardData();
            throw e;
        }
    }
}
