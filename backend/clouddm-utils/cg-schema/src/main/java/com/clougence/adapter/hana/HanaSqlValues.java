/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.adapter.hana;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** SQL literals shared by the HANA data editor and SQL export. */
public final class HanaSqlValues {
    private HanaSqlValues(){
    }

    public static String literal(HanaTypes type, String value) {
        if (value == null) {
            return "NULL";
        }
        switch (type) {
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
                return new BigInteger(value).toString();
            case DECIMAL:
            case SMALLDECIMAL:
            case REAL:
            case FLOAT:
            case DOUBLE:
                return new BigDecimal(value).toString();
            case BOOLEAN:
                if ("true".equalsIgnoreCase(value) || "1".equals(value)) {
                    return "TRUE";
                }
                if ("false".equalsIgnoreCase(value) || "0".equals(value)) {
                    return "FALSE";
                }
                throw new IllegalArgumentException("Invalid HANA boolean value");
            case BINARY:
            case VARBINARY:
            case BLOB:
                String hex = value.replaceFirst("^0[xX]", "");
                if (hex.length() % 2 != 0 || !hex.matches("[0-9a-fA-F]*")) {
                    throw new IllegalArgumentException("HANA binary values require hexadecimal byte pairs");
                }
                return "HEXTOBIN('" + hex + "')";
            case DATE:
                return "TO_DATE('" + LocalDate.parse(value) + "', 'YYYY-MM-DD')";
            case TIME:
                LocalTime time = LocalTime.parse(value);
                if (time.getNano() != 0) {
                    throw new IllegalArgumentException("HANA TIME does not store fractional seconds");
                }
                return "TO_TIME('" + time.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "', 'HH24:MI:SS')";
            case SECONDDATE:
            case TIMESTAMP:
                LocalDateTime timestamp = LocalDateTime.parse(value.replace(' ', 'T'));
                if (timestamp.getNano() % 100 != 0 || type == HanaTypes.SECONDDATE && timestamp.getNano() != 0) {
                    throw new IllegalArgumentException("Value exceeds HANA timestamp precision");
                }
                return "TO_TIMESTAMP('" + timestamp.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSSSSSS")) + "', 'YYYY-MM-DD HH24:MI:SS.FF7')";
            case CHAR:
            case NCHAR:
            case VARCHAR:
            case NVARCHAR:
            case ALPHANUM:
            case SHORTTEXT:
            case CLOB:
            case NCLOB:
            case TEXT:
                return "N'" + value.replace("'", "''") + "'";
            default:
                throw new IllegalArgumentException("Unsupported HANA editable type: " + type);
        }
    }
}
