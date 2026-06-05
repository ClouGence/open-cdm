/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package com.clougence.clouddm.worker.services;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.clouddm.sdk.execute.session.result.ReaderOptions;
import com.clougence.clouddm.sdk.execute.resultset.file.ResultReader;
import com.clougence.clouddm.sdk.execute.resultset.file.ResultSetFileCode;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.worker.component.result.EntityType;
import com.clougence.clouddm.worker.component.result.FileResultSetInputStream;
import com.clougence.clouddm.worker.component.result.ResultDataTag;
import com.clougence.clouddm.worker.component.result.ResultSetInputStream.DataHeader;
import com.clougence.clouddm.worker.component.result.ResultSetInputStream.RowHeader;
import com.clougence.clouddm.worker.component.session.storage.RowDataHelper;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.io.IOUtils;

class SidecarResultReaderImpl implements ResultReader {

    private final FileResultSetInputStream resultInput;
    private final QueryRequest             queryRequest;
    private final ColMetaData[]            colMetaData;
    private final ReaderOptions            options;

    public SidecarResultReaderImpl(File resultFile) throws IOException{
        this.resultInput = new FileResultSetInputStream(resultFile);

        this.resultInput.nextRow();
        this.queryRequest = JsonUtils.toObj(this.resultInput.readString(), QueryRequest.class);

        this.resultInput.nextRow();
        this.colMetaData = new ColMetaData[Math.toIntExact(this.resultInput.getDataCount())];
        for (int i = 0; i < this.resultInput.getDataCount(); i++) {
            this.colMetaData[i] = JsonUtils.toObj(this.resultInput.readString(), ColMetaData.class);
        }

        this.options = new ReaderOptions();
        this.options.setColumnBytesLimit(this.queryRequest.getResultConf().getFetchColumnBytesLimit());
        this.options.setElementBytesLimit(this.queryRequest.getResultConf().getFetchElementBytesLimit());
        this.options.setTempPath(GlobalConfUtils.getTempDataHome());
        this.options.setDataFormat(this.queryRequest.getResultConf().getDataFormat());
        this.options.setTimeFormat(this.queryRequest.getResultConf().getTimeFormat());
        this.options.setDataTimeFormat(this.queryRequest.getResultConf().getDataTimeFormat());
        this.options.setTimeWithZoneFormat(this.queryRequest.getResultConf().getTimeWithZoneFormat());
        this.options.setDataTimeWithZoneFormat(this.queryRequest.getResultConf().getDataTimeWithZoneFormat());
    }

    @Override
    public QueryRequest getQueryInfo() { return this.queryRequest; }

    @Override
    public ColMetaData[] getMetadataInfo() { return this.colMetaData; }

    @Override
    public void close() throws IOException {
        this.resultInput.close();
    }

    @Override
    public long getFileSize() { return this.resultInput.getFileName().length(); }

    @Override
    public long getRowCount() { return this.resultInput.getRowCount() - 2; }

    @Override
    public boolean isFileEof() { return this.resultInput.isEof(); }

    //

    @Override
    public byte getRowTag() {
        RowHeader header = this.resultInput.getRowHeader();
        return header == null ? (byte) 0 : header.getTag();
    }

    @Override
    public long getRowSize() {
        RowHeader header = this.resultInput.getRowHeader();
        return header == null ? -1 : header.getLength();
    }

    @Override
    public long getDataCount() {
        RowHeader header = this.resultInput.getRowHeader();
        return header == null ? -1 : header.getDataCount();
    }

    @Override
    public boolean nextRow() throws IOException {
        return this.resultInput.nextRow();
    }

    @Override
    public long nextRow(long skipNumber) throws IOException {
        long actualSkip = 0;
        for (long i = 0; i < skipNumber; i++) {
            if (!this.resultInput.nextRow()) {
                break;
            } else {
                actualSkip++;
            }
        }
        return actualSkip;
    }

    @Override
    public boolean hasNextRow() {
        return this.resultInput.hasNextRow();
    }

    @Override
    public boolean isLastRow() { return this.resultInput.isEof(); }

    //

    @Override
    public byte nextDataTag() throws IOException {
        DataHeader header = this.resultInput.nextDataHeader();
        return header == null ? (byte) 0 : header.getTag();
    }

    @Override
    public byte nextDataType() throws IOException {
        DataHeader header = this.resultInput.nextDataHeader();
        return header == null ? 0 : header.getType();
    }

    @Override
    public long nextDataSize() throws IOException {
        DataHeader header = this.resultInput.nextDataHeader();
        return header == null ? -1 : header.getLength();
    }

    @Override
    public boolean nextData() throws IOException {
        return this.resultInput.nextData();
    }

    @Override
    public long nextData(long skipData) throws IOException {
        long actualSkip = 0;
        for (long i = 0; i < skipData; i++) {
            if (!this.resultInput.nextData()) {
                break;
            } else {
                actualSkip++;
            }
        }
        return actualSkip;
    }

    @Override
    public boolean hasNextData() throws IOException {
        return this.resultInput.hasNextData();
    }

    @Override
    public boolean isLastData() throws IOException { return !this.resultInput.hasNextData(); }

    //

    public ResultSetValue readAsString(ColMetaData meta, QueryResultConf conf) throws IOException {
        DataHeader header = this.resultInput.nextDataHeader();
        return readAsString(meta, conf, 0, header.getLength());
    }

    @Override
    public ResultSetValue readAsString(ColMetaData meta, QueryResultConf conf, long offset, long length) throws IOException {
        DataHeader header = this.resultInput.nextDataHeader();
        byte dataTag = header.getTag();
        boolean complete = !(dataTag == (ResultDataTag.DATA_CROPPED_TAG | dataTag));
        boolean mask = dataTag == (ResultDataTag.DATA_MASK_TAG | dataTag);
        boolean error = false;
        long moreSize = 0;
        long totalSize = header.getLength();
        String value = null;

        switch (header.getType()) {
            case EntityType.Boolean: {
                Boolean v = this.resultInput.readBoolean();
                value = RowDataHelper.displayBoolean(conf.getDisplayChars(), v);
                break;
            }
            case EntityType.Byte: {
                Byte v = this.resultInput.readByte();
                value = RowDataHelper.displayByte(conf.getDisplayChars(), v);
                break;
            }
            case EntityType.Short: {
                Short v = this.resultInput.readShort();
                value = RowDataHelper.displayShort(conf.getDisplayChars(), v);
                break;
            }
            case EntityType.Integer: {
                Integer v = this.resultInput.readInteger();
                value = RowDataHelper.displayInteger(conf.getDisplayChars(), v);
                break;
            }
            case EntityType.Long: {
                Long v = this.resultInput.readLong();
                value = RowDataHelper.displayLong(conf.getDisplayChars(), v);
                break;
            }
            case EntityType.BigInteger: {
                BigInteger v = this.resultInput.readBigInteger();
                value = RowDataHelper.displayBigInteger(conf.getDisplayChars(), v);
                break;
            }
            case EntityType.BigDecimal: {
                BigDecimal v = this.resultInput.readBigDecimal();
                value = RowDataHelper.displayBigDecimal(conf.getDisplayChars(), v);
                break;
            }
            case EntityType.Float: {
                Float v = this.resultInput.readFloat();
                value = RowDataHelper.displayFloat(conf.getDisplayChars(), v);
                break;
            }
            case EntityType.Double: {
                Double v = this.resultInput.readDouble();
                value = RowDataHelper.displayDouble(conf.getDisplayChars(), v);
                break;
            }
            case EntityType.String: {
                try {
                    this.resultInput.readStream(header);
                    if (!header.isNull()) {
                        InputStreamReader r = new InputStreamReader(this.resultInput, header.getCharset());
                        long skipped = 0;
                        while (skipped < offset) {
                            long s = r.skip(offset - skipped);
                            if (s <= 0) {
                                break;
                            }
                            skipped += s;
                        }

                        StringWriter w = new StringWriter();
                        char[] buffer = new char[1024]; // 缓冲区
                        long charsToRead = length;
                        while (charsToRead > 0) {
                            int len = (int) Math.min(buffer.length, charsToRead);
                            int read = r.read(buffer, 0, len);
                            if (read == -1) {
                                break;
                            }
                            w.write(buffer, 0, read);
                            charsToRead -= read;
                        }
                        value = w.toString();

                        while (true) {
                            int read = r.read(buffer); // 复用缓冲区
                            if (read == -1) {
                                break;
                            }
                            moreSize += read;
                        }
                    }
                } finally {
                    this.resultInput.finishReadData();
                }
                break;
            }
            case EntityType.Bytes: {
                try {
                    this.resultInput.readStream(header);
                    ByteArrayOutputStream tmp = new ByteArrayOutputStream();
                    IOUtils.copyLarge(this.resultInput, tmp, offset, length);
                    byte[] tmpBytes = tmp.toByteArray();
                    value = HexadecimalUtils.bytes2hex(tmpBytes);
                    moreSize = this.resultInput.available();
                } finally {
                    this.resultInput.finishReadData();
                }
                break;
            }
            case EntityType.Date: {
                LocalDate v = this.resultInput.readLocalDate();
                value = RowDataHelper.displayLocalDate(conf.getDisplayChars(), this.options, v);
                break;
            }
            case EntityType.Time: {
                LocalTime v = this.resultInput.readLocalTime();
                value = RowDataHelper.displayLocalTime(conf.getDisplayChars(), this.options, v);
                break;
            }
            case EntityType.TimeZ: {
                OffsetTime v = this.resultInput.readOffsetTime();
                value = RowDataHelper.displayOffsetTime(conf.getDisplayChars(), this.options, v);
                break;
            }
            case EntityType.DateTime: {
                LocalDateTime v = this.resultInput.readLocalDateTime();
                value = RowDataHelper.displayLocalDateTime(conf.getDisplayChars(), this.options, v);
                break;
            }
            case EntityType.DateTimeZ: {
                OffsetDateTime v = this.resultInput.readOffsetDateTime();
                value = RowDataHelper.displayOffsetDateTime(conf.getDisplayChars(), this.options, v);
                break;
            }
            case EntityType.Code: {
                error = true;
                ResultSetFileCode code = ResultSetFileCode.of(this.resultInput.readCodeStatus());
                switch (code) {
                    case Unsupported: {
                        String colType = meta != null ? meta.getColumnType() : "";
                        value = RowDataHelper.displayUnsupported(conf.getDisplayChars(), colType);
                        break;
                    }
                    default: {
                        value = "UnknownCode:" + code.name();
                        break;
                    }
                }
                break;
            }
            default: {
                value = "UnknownType: " + header.getType();
                error = true;
                break;
            }
        }

        if (error) {
            return ResultSetValue.ofError(complete, mask, value);
        } else {
            return ResultSetValue.of(complete, mask, value, moreSize, totalSize);
        }
    }
}
