package com.clougence.clouddm.worker.component.result;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Objects;
import java.util.function.Function;

import com.clougence.utils.ArrayUtils;
import com.clougence.utils.ObjectUtils;
import com.clougence.utils.io.bytes.BytesIO;

public abstract class ResultSetInputStream extends InputStream {

    private final ByteOrder byteOrder;
    private long            lastDataHeadPos    = 0;
    private long            currentPos         = 0;
    private RowHeader       readingRow;
    private DataHeader      readingData;
    //
    private long            availableRowCount  = 0;
    private long            availableDataCount = 0;
    private long            availableDataBytes = 0;
    //
    private long            fileSize           = 0;
    private long            rowCount           = 0;

    public ResultSetInputStream(ByteOrder order){
        this.byteOrder = order == null ? ByteOrder.BIG_ENDIAN : order;
    }

    protected void initStream() throws IOException {
        byte[] rows = new byte[8];
        int r = this.getBytes(0, rows, 0, rows.length);
        if (r != 8) {
            throw new IOException("Invalid file header length: " + r);
        }
        this.rowCount = this.buildObject(rows, BytesIO::readInt64);

        this.lastDataHeadPos = 8;
        this.currentPos = 8;
        this.readingRow = null;
        this.readingData = null;
        this.availableRowCount = this.rowCount;
        this.availableDataCount = 0;
        this.availableDataBytes = 0;
        this.fileSize = this.fileLength();
    }

    protected abstract long initFilePosition() throws IOException;

    protected abstract long fileLength() throws IOException;

    protected abstract int getByte(long offset) throws IOException;

    protected abstract int getBytes(long offset, byte[] b, int off, int length) throws IOException;

    @Override
    public void close() throws IOException {
        this.endOfFile();
    }

    public boolean isEof() { return this.currentPos == this.fileSize; }

    private void endOfFile() {
        this.lastDataHeadPos = this.fileSize;
        this.currentPos = this.fileSize;
        this.readingData = null;
        this.availableRowCount = 0;
        this.availableDataCount = 0;
        this.availableDataBytes = 0;
    }

    private long getLengthData(long offset, byte lenFieldByte) throws IOException {
        switch (lenFieldByte) {
            case 0x00: {
                return 0;
            }
            case 0x01: {
                byte[] b = new byte[1];
                int r = this.getBytes(offset, b, 0, b.length);
                if (r != 1) {
                    throw new IOException("Invalid length header: " + r);
                } else {
                    return this.buildObject(b, BytesIO::readUInt8);
                }
            }
            case 0x02: {
                byte[] b = new byte[2];
                int r = this.getBytes(offset, b, 0, b.length);
                if (r != 2) {
                    throw new IOException("Invalid length header: " + r);
                } else {
                    return this.buildObject(b, BytesIO::readUInt16);
                }
            }
            case 0x03: {
                byte[] b = new byte[3];
                int r = this.getBytes(offset, b, 0, b.length);
                if (r != 3) {
                    throw new IOException("Invalid length header: " + r);
                } else {
                    return this.buildObject(b, BytesIO::readUInt24);
                }
            }
            case 0x04: {
                byte[] b = new byte[4];
                int r = this.getBytes(offset, b, 0, b.length);
                if (r != 4) {
                    throw new IOException("Invalid length header: " + r);
                } else {
                    return this.buildObject(b, BytesIO::readUInt32);
                }
            }
            case 0x08: {
                byte[] b = new byte[8];
                int r = this.getBytes(offset, b, 0, b.length);
                if (r != 8) {
                    throw new IOException("Invalid length header: " + r);
                } else {
                    return this.buildObject(b, BytesIO::readInt64);
                }
            }
            default:
                throw new IOException("Invalid length header: " + lenFieldByte);
        }
    }

    public long getRowCount() { return this.rowCount; }

    public long getDataCount() { return this.readingRow.rowDataCount; }

    //
    // for read row
    //

    public RowHeader getRowHeader() { return this.readingRow; }

    public boolean hasNextRow() {
        return this.availableRowCount > 0;
    }

    public boolean nextRow() throws IOException {
        long nextRowPos = this.readingRow == null ? this.currentPos : this.readingRow.getEndPos();
        if (nextRowPos >= this.fileSize) {
            this.endOfFile();
            return false;
        }

        RowHeader nextRow = this.getRowHeader(nextRowPos);
        this.lastDataHeadPos = nextRow.rowDataPos;
        this.currentPos = nextRow.rowDataPos;
        this.readingRow = nextRow;
        this.readingData = null;
        this.availableRowCount--;
        this.availableDataCount = nextRow.rowDataCount;
        this.availableDataBytes = 0;

        return true;
    }

    private RowHeader getRowHeader(long offset) throws IOException {
        byte[] b = new byte[17];
        int r = this.getBytes(offset, b, 0, b.length);
        if (r != 17) {
            throw new IOException("Invalid row header length: " + r);
        }

        return this.buildObject(b, bb -> {
            byte rowTag = bb.readByte();
            long rowDataCount = bb.readInt64();
            long rowDataLength = bb.readInt64();

            return new RowHeader(offset, rowTag, 17, rowDataLength, offset + 17, rowDataCount);
        });
    }

    public void discardReadRow() {
        this.lastDataHeadPos = this.readingRow.rowDataPos;
        this.currentPos = this.readingRow.rowDataPos;
        this.readingData = null;
        this.availableRowCount++;
        this.availableDataCount = this.readingRow.rowDataCount;
        this.availableDataBytes = 0;
    }

    //
    // for read data
    //

    public DataHeader getDataHeader() { return this.readingData; }

    public boolean hasNextData() throws IOException {
        return this.availableDataCount > 0;
    }

    public DataHeader nextDataHeader() throws IOException {
        if (this.availableDataCount == 0) {
            return null;
        }

        return this.getDataHeader(this.currentPos);
    }

    /**
     * Skip the current data and move to the next data.
     */
    public boolean nextData() throws IOException {
        if (this.readingData != null) {
            this.finishReadData();
        }

        if (this.availableDataCount == 0) {
            return false;
        }

        // enter body
        boolean hasData = this.enterBody();
        if (hasData) {
            // skip body
            this.finishReadData();
            return true;
        }
        return false;
    }

    protected boolean enterBody() throws IOException {
        if (this.availableDataCount == 0) {
            return false;
        }

        DataHeader header = this.getDataHeader(this.currentPos);
        this.lastDataHeadPos = this.currentPos;
        this.currentPos = header.dataBodyPos;
        this.readingData = header;
        this.availableDataBytes = header.dataBodyLength;
        this.availableDataCount--;

        return true;
    }

    private DataHeader getDataHeader(long offset) throws IOException {
        byte[] b = new byte[3];
        int r = this.getBytes(offset, b, 0, b.length);
        if (r != 3) {
            throw new IOException("Invalid data header length: " + r);
        }

        // type header
        byte typeCode = (byte) ((b[0] << 4) | ((b[1] >>> 4) & 0x0F));
        byte lenFieldBytes = (byte) (b[1] & 0x0F);
        byte dataTag = b[2];
        if (lenFieldBytes == 0) {
            return new DataHeader(typeCode, dataTag, offset, 3, 0, offset + 3, true);
        }

        // dataLen header
        long dataLen = this.getLengthData(offset + 3, lenFieldBytes);
        long dataPos = offset + 3 + lenFieldBytes;
        long dataHeaderLen = dataPos - offset;
        String charsetName = null;

        if (EntityType.isTypeOf(typeCode, EntityType.String)) {
            byte nameDataLenFieldByte = (byte) this.getByte(dataPos);
            long nameDataLen = this.getLengthData(dataPos + 1, nameDataLenFieldByte);
            long nameDataPos = dataPos + 1 + nameDataLenFieldByte;
            if (nameDataLen < 0 || nameDataLen > Integer.MAX_VALUE) {
                throw new IOException("Invalid charsetName header: " + r);
            }
            byte[] nameBytes = new byte[(int) nameDataLen];
            r = this.getBytes(nameDataPos, nameBytes, 0, (int) nameDataLen);
            if (r != nameDataLen) {
                throw new IOException("Invalid charsetName data: " + r);
            }

            // dataLen and check
            byte strDataLenFieldByte = (byte) this.getByte(nameDataPos + nameDataLen);
            long strDataLen = this.getLengthData(nameDataPos + nameDataLen + 1, strDataLenFieldByte);
            long strDataPos = nameDataPos + nameDataLen + 1 + strDataLenFieldByte;

            // check data length
            long nameDataFullLen = 1 + nameDataLenFieldByte + nameDataLen;
            long strDataFullLen = 1 + strDataLenFieldByte + strDataLen;
            if (dataLen != (nameDataFullLen + strDataFullLen)) {
                throw new IOException("Invalid data length for String: " + dataLen + ", expected: " + (nameDataLen + strDataLen + 2));
            }

            // header data
            dataHeaderLen = strDataPos - offset;
            dataLen = strDataLen;
            dataPos = strDataPos;
            charsetName = new String(nameBytes, StandardCharsets.US_ASCII);
        }

        return new DataHeader(typeCode, dataTag, offset, dataHeaderLen, dataLen, dataPos, false, charsetName);
    }

    public void finishReadData() throws IOException {
        if (this.readingData == null) {
            throw new IOException("not read any data.");
        }

        if (this.availableDataCount == 0) {
            return;
        }

        long nextDataPos = this.lastDataHeadPos + this.readingData.headerLength + this.readingData.dataBodyLength;
        if (nextDataPos >= this.fileSize) {
            this.endOfFile();
            return;
        }

        this.lastDataHeadPos = nextDataPos;
        this.currentPos = nextDataPos;
        this.readingData = null;
        this.availableDataBytes = 0;
    }

    public void discardReadData() {
        if (this.readingData == null) {
            return;
        }

        this.currentPos = this.lastDataHeadPos;
        this.readingData = null;
        this.availableDataBytes = 0;
        this.availableDataCount++;
    }

    //
    // for stream
    //

    private void requiredReadingData() throws IOException {
        if (this.readingData == null) {
            throw new IOException("no data in reading.");
        }
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        int i = this.read(b, 0, b.length);
        if (i == -1) {
            return -1;  // EOF
        } else {
            return b[0] & 0xFF;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        this.requiredReadingData();

        if (this.availableDataBytes == 0) {
            return -1;
        }
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }

        int readLen = (int) Math.min(len, this.availableDataBytes);
        int r = this.getBytes(this.currentPos, b, off, readLen);
        if (r > 0) {
            this.currentPos += r;
            this.availableDataBytes -= r;
            return r;
        } else {
            return r;
        }
    }

    private void readSkip(int size) throws IOException {
        this.requiredReadingData();

        if (size > this.availableDataBytes) {
            throw new IOException("There are not enough bytes that can be skipped, exceeding " + (size - this.availableDataBytes) + " bytes。");
        }

        this.currentPos += size;
        this.availableDataBytes -= size;
    }

    @Override
    public int available() {
        if (this.currentPos == this.fileSize) {
            return -1;
        } else {
            return this.availableDataBytes > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) this.availableDataBytes;
        }
    }

    @Override
    public synchronized void mark(int readLimit) {
        // TODO 实现 mark 方法，用于标记当前读取位置。
    }

    @Override
    public synchronized void reset() throws IOException {
        // TODO 实现 reset 方法，用于将读取位置重置到 mark 标记的位置。
        throw new IOException("mark/reset not supported");
    }

    @Override
    public boolean markSupported() {
        // TODO 实现 markSupported 方法，返回是否支持 mark 和 reset。
        return false;
    }

    //
    // for read objects
    //

    private void checkTypeCode(DataHeader header, byte checkType) throws IOException {
        Objects.requireNonNull(header, "DataHeader is null.");
        if (!header.typeOf(checkType)) {
            throw new IOException("Invalid data type: " + header.typeCode + ", expected: " + checkType);
        }
    }

    private <T> T buildObject(byte[] b, Function<BytesIO, T> v) {
        BytesIO bb = BytesIO.wrap(b);
        bb = bb.order(this.byteOrder);
        return v.apply(bb);
    }

    private byte[] readComplete(int size) throws IOException {
        if (size == 0) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        byte[] b = new byte[size];
        int i = this.read(b, 0, b.length);
        if (i != size) {
            throw new IOException("data corruption.");
        } else {
            return b;
        }
    }

    private DataHeader nextDataAndGet() throws IOException {
        if (!this.enterBody()) {
            throw new IOException("No more data available.");
        }
        return this.readingData;
    }

    public void readStream(DataHeader header) throws IOException {
        if (header.typeOf(EntityType.Bytes) || header.typeOf(EntityType.String)) {
            DataHeader nextData = this.nextDataHeader();
            if (nextData.position != header.position) {
                throw new IOException("Data header position mismatch: expected " + header.position + ", but got " + nextData.position);
            }
            this.nextDataAndGet();
        } else {
            throw new IOException("Invalid data header, only Bytes/String support Stream.");
        }
    }

    public Boolean readBoolean() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Boolean);

            Boolean result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 1) {
                    byte[] bytes = this.readComplete(1);
                    result = bytes[0] != 0;
                } else {
                    throw new IOException("Invalid data length for Boolean: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public Byte readByte() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Byte);

            Byte result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 1) {
                    byte[] bytes = this.readComplete(1);
                    result = bytes[0];
                } else {
                    throw new IOException("Invalid data length for Byte: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public Short readShort() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Short);

            Short result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 2) {
                    byte[] bytes = this.readComplete(2);
                    result = this.buildObject(bytes, BytesIO::readInt16);
                } else {
                    throw new IOException("Invalid data length for Short: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public Integer readInteger() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Integer);

            Integer result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 4) {
                    byte[] bytes = this.readComplete(4);
                    result = this.buildObject(bytes, BytesIO::readInt32);
                } else {
                    throw new IOException("Invalid data length for Integer: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public Long readLong() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Long);

            Long result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 8) {
                    byte[] bytes = this.readComplete(8);
                    result = this.buildObject(bytes, BytesIO::readInt64);
                } else {
                    throw new IOException("Invalid data length for Long: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public BigInteger readBigInteger() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.BigInteger);

            BigInteger result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                byte[] bytes = this.readComplete((int) dataLen);
                result = new BigInteger(bytes);
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public BigDecimal readBigDecimal() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.BigDecimal);

            BigDecimal result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                byte[] bytes = this.readComplete((int) dataLen);
                result = this.buildObject(bytes, io -> {
                    int scale = io.readInt32();
                    byte[] unscaledVal = new byte[io.readableBytes()]; // last bytes
                    io.readBytes(unscaledVal);
                    return new BigDecimal(new BigInteger(unscaledVal), scale);
                });
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public Float readFloat() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Float);

            Float result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 4) {
                    byte[] bytes = this.readComplete(4);
                    result = this.buildObject(bytes, BytesIO::readFloat32);
                } else {
                    throw new IOException("Invalid data length for Float: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public Double readDouble() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Double);

            Double result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 8) {
                    byte[] bytes = this.readComplete(8);
                    result = this.buildObject(bytes, BytesIO::readFloat64);
                } else {
                    throw new IOException("Invalid data length for Double: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public byte[] readBytes() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Bytes);

            byte[] result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen > Integer.MAX_VALUE) {
                    throw new IOException("data length exceeds maximum size: " + dataLen + " > " + Integer.MAX_VALUE);
                } else {
                    result = this.readComplete((int) dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public byte[] readBytes(int skip, long length) throws IOException {
        ObjectUtils.checkPositiveOrZero(skip, "skip must be positive.");
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Bytes);

            byte[] result;
            if (header.isNull()) {
                result = null;
            } else if (length == 0) {
                result = ArrayUtils.EMPTY_BYTE_ARRAY;
            } else {
                long dataLen = header.getLength();
                if (length < 0) {
                    length = dataLen;
                }

                if ((skip + length) > dataLen) {
                    throw new IOException("read length exceeds data length: " + (skip + length) + " > " + dataLen);
                }
                if (length > Integer.MAX_VALUE) {
                    throw new IOException("data length exceeds maximum size: " + length + " > " + Integer.MAX_VALUE);
                }

                this.readSkip(skip);
                result = this.readComplete((int) length);
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public String readString() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.String);

            String result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen > Integer.MAX_VALUE) {
                    throw new IOException("data length exceeds maximum size: " + dataLen + " > " + Integer.MAX_VALUE);
                } else {
                    byte[] strBytes = this.readComplete((int) dataLen);
                    result = new String(strBytes, header.getCharset());
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public LocalDate readLocalDate() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Date);

            LocalDate result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 8) {
                    byte[] bytes = this.readComplete(8);
                    return this.buildObject(bytes, bb -> {
                        long epochDay = bb.readInt64();
                        return LocalDate.ofEpochDay(epochDay);
                    });
                } else {
                    throw new IOException("Invalid data length for LocalDate: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public LocalTime readLocalTime() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Time);

            LocalTime result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 8) {
                    byte[] bytes = this.readComplete(8);
                    result = this.buildObject(bytes, bb -> {
                        return LocalTime.ofNanoOfDay(bb.readInt64());
                    });
                } else {
                    throw new IOException("Invalid data length for LocalTime: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public OffsetTime readOffsetTime() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.TimeZ);

            OffsetTime result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 12) {
                    byte[] bytes = this.readComplete(12);
                    result = this.buildObject(bytes, bb -> {
                        LocalTime localTime = LocalTime.ofNanoOfDay(bb.readInt64());
                        ZoneOffset zone = ZoneOffset.ofTotalSeconds(bb.readInt32());
                        return OffsetTime.of(localTime, zone);
                    });
                } else {
                    throw new IOException("Invalid data length for OffsetTime: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public LocalDateTime readLocalDateTime() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.DateTime);

            LocalDateTime result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 16) {
                    byte[] bytes = this.readComplete(16);
                    result = this.buildObject(bytes, bb -> {
                        LocalDate localDate = LocalDate.ofEpochDay(bb.readInt64());
                        LocalTime localTime = LocalTime.ofNanoOfDay(bb.readInt64());
                        return LocalDateTime.of(localDate, localTime);
                    });
                } else {
                    throw new IOException("Invalid data length for LocalDateTime: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public OffsetDateTime readOffsetDateTime() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.DateTimeZ);

            OffsetDateTime result;
            if (header.isNull()) {
                result = null;
            } else {
                long dataLen = header.getLength();
                if (dataLen == 20) {
                    byte[] bytes = this.readComplete(20);
                    result = this.buildObject(bytes, bb -> {
                        LocalDate localDate = LocalDate.ofEpochDay(bb.readInt64());
                        LocalTime localTime = LocalTime.ofNanoOfDay(bb.readInt64());
                        ZoneOffset zone = ZoneOffset.ofTotalSeconds(bb.readInt32());
                        return OffsetDateTime.of(localDate, localTime, zone);
                    });
                } else {
                    throw new IOException("Invalid data length for OffsetDateTime: " + dataLen);
                }
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public int readCodeStatus() throws IOException {
        try {
            DataHeader header = this.nextDataAndGet();
            this.checkTypeCode(header, EntityType.Code);

            int result;
            long dataLen = header.getLength();
            if (dataLen == 4) {
                byte[] bytes = this.readComplete(4);
                result = this.buildObject(bytes, BytesIO::readInt32);
            } else {
                throw new IOException("Invalid data length for OffsetDateTime: " + dataLen);
            }

            this.finishReadData();
            return result;
        } catch (IOException e) {
            this.discardReadData();
            throw e;
        }
    }

    public static class DataHeader {

        private final byte    typeCode;
        private final byte    dataTag;
        private final long    headerLength;
        private final long    position;
        //
        private final long    dataBodyLength;
        private final long    dataBodyPos;
        private final boolean dataBodyIsNull;
        private final String  charsetName;

        DataHeader(byte typeCode, byte dataTag, long position, long headerLength, long dataBodyLength, long dataBodyPos, boolean dataBodyIsNull){
            this(typeCode, dataTag, position, headerLength, dataBodyLength, dataBodyPos, dataBodyIsNull, null);
        }

        DataHeader(byte typeCode, byte dataTag, long position, long headerLength, long dataBodyLength, long dataBodyPos, boolean dataBodyIsNull, String charsetName){
            this.typeCode = typeCode;
            this.dataTag = dataTag;
            this.headerLength = headerLength;
            this.position = position;
            this.dataBodyLength = dataBodyLength;
            this.dataBodyPos = dataBodyPos;
            this.dataBodyIsNull = dataBodyIsNull;
            this.charsetName = charsetName;
        }

        public byte getType() { return this.typeCode; }

        public byte getTag() { return this.dataTag; }

        public long getLength() { return this.dataBodyLength; }

        public boolean isNull() { return this.dataBodyIsNull; }

        public String getCharsetName() { return this.charsetName; }

        public Charset getCharset() { return this.charsetName == null ? null : Charset.forName(this.charsetName); }

        public boolean typeOf(byte dataType) {
            return EntityType.isTypeOf(this.typeCode, dataType);
        }
    }

    public static class RowHeader {

        private final byte rowTag;
        private final long headerLength;
        private final long position;
        //
        private final long rowDataLength;
        private final long rowDataPos;
        private final long rowDataCount;

        RowHeader(long position, byte rowTag, long headerLength, long rowDataLength, long rowDataPos, long rowDataCount){
            this.rowTag = rowTag;
            this.position = position;
            this.headerLength = headerLength;
            this.rowDataLength = rowDataLength;
            this.rowDataPos = rowDataPos;
            this.rowDataCount = rowDataCount;
        }

        public byte getTag() { return this.rowTag; }

        public long getLength() { return this.rowDataLength; }

        public long getDataCount() { return this.rowDataCount; }

        private long getEndPos() { return this.position + this.rowDataLength; }
    }
}
