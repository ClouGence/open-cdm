package com.clougence.clouddm.worker.component.session.storage;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.*;
import com.clougence.clouddm.sdk.execute.resultset.file.ResultSetFileCode;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.worker.component.result.ResultDataTag;
import com.clougence.clouddm.worker.component.result.ResultSetOutputStream;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.convert.ConverterUtils;

public class RowStorage implements Closeable {

    private final ResultSetOutputStream outputStream;
    private final int                   displayChars;
    private final boolean               usingCache;
    private boolean                     finished;

    public RowStorage(ResultSetOutputStream outputStream, int displayChars){
        this.outputStream = outputStream;
        this.displayChars = displayChars;
        this.usingCache = outputStream != null;
        this.finished = false;
    }

    public void initRow() throws IOException {
        if (this.usingCache) {
            this.outputStream.newRow();
        }
    }

    @Override
    public void close() throws IOException {
        this.commit();
    }

    public void commit() throws IOException {
        try {
            if (this.usingCache) {
                this.outputStream.closeRow();
            }
        } finally {
            this.finished = true;
        }
    }

    public ResultSetOutputStream getOutput() { return this.outputStream; }

    protected void checkFinished() {
        if (this.finished) {
            throw new IllegalStateException("RowStorage has been finished.");
        }
    }

    public ResultSetValue addValue(byte tag, ResultSet rs, String column, ValueFetcher fetcher, ValueFetcherContext ctx) throws SQLException {
        if (fetcher == null) {
            return this.addUnsupported(tag, column, ctx.getMeta().getMeta(), ctx);
        }

        switch (fetcher.getType()) {
            case Boolean:
                return this.addBoolean(tag, fetcher.getBoolean(column, rs, ctx), ctx);
            case Byte:
                return this.addByte(tag, fetcher.getByte(column, rs, ctx), ctx);
            case Short:
                return this.addShort(tag, fetcher.getShort(column, rs, ctx), ctx);
            case Integer:
                return this.addInteger(tag, fetcher.getInteger(column, rs, ctx), ctx);
            case Long:
                return this.addLong(tag, fetcher.getLong(column, rs, ctx), ctx);
            case BigInteger:
                return this.addBigInteger(tag, fetcher.getBigInteger(column, rs, ctx), ctx);
            case BigDecimal:
                return this.addBigDecimal(tag, fetcher.getBigDecimal(column, rs, ctx), ctx);
            case Float:
                return this.addFloat(tag, fetcher.getFloat(column, rs, ctx), ctx);
            case Double:
                return this.addDouble(tag, fetcher.getDouble(column, rs, ctx), ctx);
            case String: {
                long columnSize = fetcher.getSize(column, rs, ctx);
                if (columnSize < 0 || columnSize > 1048576) {
                    // unknown size or lage size(>1MB max 4MB using UTF_8)
                    return this.addString(tag, fetcher.getReader(column, rs, ctx), ctx);
                } else {
                    return this.addString(tag, fetcher.getString(column, rs, ctx), ctx);
                }
            }
            case Bytes: {
                long columnSize = fetcher.getSize(column, rs, ctx);
                if (columnSize < 0 || columnSize > 1048576) {
                    // unknown size or lage size(>1MB)
                    return this.addBytes(tag, fetcher.getInputStream(column, rs, ctx), ctx);
                } else {
                    return this.addBytes(tag, fetcher.getBytes(column, rs, ctx), ctx);
                }
            }
            case Date:
                return this.addLocalDate(tag, fetcher.getDate(column, rs, ctx), ctx);
            case Time:
                return this.addLocalTime(tag, fetcher.getTime(column, rs, ctx), ctx);
            case TimeZ:
                return this.addOffsetTime(tag, fetcher.getTimeZ(column, rs, ctx), ctx);
            case DateTime:
                return this.addLocalDateTime(tag, fetcher.getDateTime(column, rs, ctx), ctx);
            case DateTimeZ:
                return this.addOffsetDateTime(tag, fetcher.getDateTimeZ(column, rs, ctx), ctx);
            default:
                return this.addUnsupported(tag, column, ctx.getMeta().getMeta(), ctx);
        }
    }

    public ResultSetValue addValue(byte tag, Map<String, Object> rs, String column, ValueType fetcher, ValueFetcherContext ctx) {
        if (fetcher == null) {
            return this.addUnsupported(tag, column, ctx.getMeta().getMeta(), ctx);
        }

        switch (fetcher) {
            case Boolean:
                return this.addBoolean(tag, (Boolean) ConverterUtils.convert(Boolean.class, rs.get(column)), ctx);
            case Byte:
                return this.addByte(tag, (Byte) ConverterUtils.convert(Byte.class, rs.get(column)), ctx);
            case Short:
                return this.addShort(tag, (Short) ConverterUtils.convert(Short.class, rs.get(column)), ctx);
            case Integer:
                return this.addInteger(tag, (Integer) ConverterUtils.convert(Integer.class, rs.get(column)), ctx);
            case Long:
                return this.addLong(tag, (Long) ConverterUtils.convert(Long.class, rs.get(column)), ctx);
            case BigInteger:
                return this.addBigInteger(tag, (BigInteger) ConverterUtils.convert(BigInteger.class, rs.get(column)), ctx);
            case BigDecimal:
                return this.addBigDecimal(tag, (BigDecimal) ConverterUtils.convert(BigDecimal.class, rs.get(column)), ctx);
            case Float:
                return this.addFloat(tag, (Float) ConverterUtils.convert(Float.class, rs.get(column)), ctx);
            case Double:
                return this.addDouble(tag, (Double) ConverterUtils.convert(Double.class, rs.get(column)), ctx);
            case String:
                return this.addString(tag, String.valueOf(rs.get(column)), ctx);
            case Date:
            case Time:
            case TimeZ:
            case DateTime:
            case DateTimeZ:
            case Bytes:
            default:
                return this.addUnsupported(tag, column, ctx.getMeta().getMeta(), ctx);
        }
    }

    protected ResultSetValue addBoolean(byte tag, Boolean v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeBoolean(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayBoolean(this.displayChars, v);
            return ResultSetValue.of(complete, mask, value, 0, 1);
        }
    }

    protected ResultSetValue addByte(byte tag, Byte v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeByte(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayByte(this.displayChars, v);
            return ResultSetValue.of(complete, mask, value, 0, 1);
        }
    }

    protected ResultSetValue addShort(byte tag, Short v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeShort(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayShort(this.displayChars, v);
            return ResultSetValue.of(complete, mask, value, 0, 2);
        }
    }

    protected ResultSetValue addInteger(byte tag, Integer v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeInteger(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayInteger(this.displayChars, v);
            return ResultSetValue.of(complete, mask, value, 0, 4);
        }
    }

    protected ResultSetValue addLong(byte tag, Long v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeLong(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayLong(this.displayChars, v);
            return ResultSetValue.of(complete, mask, value, 0, 8);
        }
    }

    protected ResultSetValue addBigInteger(byte tag, BigInteger v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeBigInteger(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayBigInteger(this.displayChars, v);
            return ResultSetValue.of(complete, mask, value, 0, v.toByteArray().length);
        }
    }

    protected ResultSetValue addBigDecimal(byte tag, BigDecimal v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeBigDecimal(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayBigDecimal(this.displayChars, v);
            byte[] decData = v.unscaledValue().toByteArray();
            return ResultSetValue.of(complete, mask, value, 0, 4 + decData.length);
        }
    }

    protected ResultSetValue addFloat(byte tag, Float v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeFloat(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayFloat(this.displayChars, v);
            return ResultSetValue.of(complete, mask, value, 0, 4);
        }
    }

    protected ResultSetValue addDouble(byte tag, Double v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeDouble(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayDouble(this.displayChars, v);
            return ResultSetValue.of(complete, mask, value, 0, 8);
        }
    }

    protected ResultSetValue addBytes(byte tag, byte[] v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeBytes(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            ValueFetcherContextBytesStreamInfo ctx = (ValueFetcherContextBytesStreamInfo) fvc.getContext();
            if (ctx != null) {
                complete = complete && ctx.isComplete();
                long fullSize = ctx.fullSize();

                try {
                    byte[] sample = ctx.sampleData(this.displayChars);
                    String displayed = HexadecimalUtils.bytes2hex(sample);
                    long moreSize = fullSize - sample.length;
                    return ResultSetValue.of(complete, mask, displayed, moreSize, fullSize);
                } catch (IOException e) {
                    return ResultSetValue.ofError(complete, mask, e.getMessage());
                }
            } else {
                byte[] sample = v;
                if (v.length > this.displayChars) {
                    sample = new byte[this.displayChars];
                    System.arraycopy(v, 0, sample, 0, this.displayChars);
                }

                long fullSize = v.length;
                String displayed = HexadecimalUtils.bytes2hex(sample);
                long moreSize = fullSize - sample.length;
                return ResultSetValue.of(complete, mask, displayed, moreSize, fullSize);
            }
        }
    }

    protected ResultSetValue addBytes(byte tag, InputStream v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeBytes(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            ValueFetcherContextBytesStreamInfo ctx = (ValueFetcherContextBytesStreamInfo) fvc.getContext();
            complete = complete && ctx.isComplete();
            long fullSize = ctx.fullSize();

            try {
                byte[] sample = ctx.sampleData(this.displayChars);
                String displayed = HexadecimalUtils.bytes2hex(sample);
                long moreSize = fullSize - sample.length;
                return ResultSetValue.of(complete, mask, displayed, moreSize, fullSize);
            } catch (IOException e) {
                return ResultSetValue.ofError(complete, mask, e.getMessage());
            }
        }
    }

    protected ResultSetValue addString(byte tag, Reader v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeString(tag, v, StandardCharsets.UTF_8);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            ValueFetcherContextCharStreamInfo ctx = (ValueFetcherContextCharStreamInfo) fvc.getContext();
            complete = complete && ctx.isComplete();
            long fullSize = ctx.fullSize();

            try {
                String displayed = ctx.sampleData(this.displayChars);
                long moreSize = fullSize - displayed.length();
                return ResultSetValue.of(complete, mask, displayed, moreSize, fullSize);
            } catch (IOException e) {
                return ResultSetValue.ofError(complete, mask, e.getMessage());
            }
        }
    }

    protected ResultSetValue addString(byte tag, String v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeString(tag, v, StandardCharsets.UTF_8);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            ValueFetcherContextCharStreamInfo ctx = (ValueFetcherContextCharStreamInfo) fvc.getContext();
            if (ctx != null) {
                complete = complete && ctx.isComplete();
                long fullSize = ctx.fullSize();

                try {
                    String displayed = ctx.sampleData(this.displayChars);
                    long moreSize = fullSize - displayed.length();
                    return ResultSetValue.of(complete, mask, displayed, moreSize, fullSize);
                } catch (IOException e) {
                    return ResultSetValue.ofError(complete, mask, e.getMessage());
                }
            } else {
                int fullSize = v.length();
                String displayed = (fullSize > this.displayChars) ? v.substring(0, this.displayChars) : v;
                return ResultSetValue.of(true, mask, displayed, fullSize - displayed.length(), fullSize);
            }
        }
    }

    protected ResultSetValue addLocalDate(byte tag, LocalDate v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeLocalDate(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayLocalDate(this.displayChars, fvc.getOptions(), v);
            return ResultSetValue.of(complete, mask, value, 0, 8);
        }
    }

    protected ResultSetValue addLocalTime(byte tag, LocalTime v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeLocalTime(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayLocalTime(this.displayChars, fvc.getOptions(), v);
            return ResultSetValue.of(complete, mask, value, 0, 8);
        }
    }

    protected ResultSetValue addOffsetTime(byte tag, OffsetTime v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeOffsetTime(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayOffsetTime(this.displayChars, fvc.getOptions(), v);
            return ResultSetValue.of(complete, mask, value, 0, 12);
        }
    }

    protected ResultSetValue addLocalDateTime(byte tag, LocalDateTime v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeLocalDateTime(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayLocalDateTime(this.displayChars, fvc.getOptions(), v);
            return ResultSetValue.of(complete, mask, value, 0, 16);
        }
    }

    protected ResultSetValue addOffsetDateTime(byte tag, OffsetDateTime v, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeOffsetDateTime(tag, v);
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        if (v == null) {
            return ResultSetValue.of(complete, mask, null, 0, 0);
        } else {
            String value = RowDataHelper.displayOffsetDateTime(this.displayChars, fvc.getOptions(), v);
            return ResultSetValue.of(complete, mask, value, 0, 20);
        }
    }

    protected ResultSetValue addUnsupported(byte tag, String column, ColMetaData ctx, ValueFetcherContext fvc) {
        this.checkFinished();

        if (this.usingCache) {
            try {
                this.outputStream.writeCodeStatus(tag, ResultSetFileCode.Unsupported.getCode());
            } catch (IOException e) {
                fvc.markError(e);
            }
        }

        boolean complete = !(tag == (tag | ResultDataTag.DATA_CROPPED_TAG));
        boolean mask = tag == (tag | ResultDataTag.DATA_MASK_TAG);
        String columnType = ctx.getColumnType();
        return ResultSetValue.ofError(complete, mask, RowDataHelper.displayUnsupported(this.displayChars, columnType));
    }
}
