package com.clougence.utils.io.result;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class ResultSetTest {

    @Test
    public void test_1() throws IOException {
        try (FileResultSetInputStream in = new FileResultSetInputStream(
            new File("src/test/resources/console_result_49a0e370d7fc47e3bfab78f0cffe0706_qa372495ccc2140cbaacc59aac0996a71_2.dat"))) {
            // row 1
            for (int i = 0; i < in.getRowCount(); i++) {
                System.out.println("----------------------------------------");
                in.nextRow();
                for (int j = 0; j < in.getDataCount(); j++) {
                    ResultSetInputStream.DataHeader dataHeader = in.nextDataHeader();
                    if (dataHeader.getType() == EntityType.String) {
                        System.out.println("\t" + in.readString());
                    } else if (dataHeader.getType() == EntityType.Date) {
                        System.out.println("\t" + in.readLocalDate());
                    } else {
                        System.out.println("\tNone");
                    }
                }
            }
        }
    }

    @Test
    public void allTypeTest_1() throws IOException {
        try (FileResultSetOutputStream out = new FileResultSetOutputStream(new File("test.dat"), false)) {
            out.newRow();
            out.writeCodeStatus(1234);

            {
                out.writeBoolean(true);
                out.writeBoolean(false);
                out.writeBoolean(null);

                out.writeByte((byte) 123);
                out.writeByte((byte) -123);
                out.writeByte(null);

                out.writeShort((short) 123);
                out.writeShort((short) -123);
                out.writeShort(null);

                out.writeInteger(123);
                out.writeInteger(-123);
                out.writeInteger(null);

                out.writeLong(123L);
                out.writeLong(-123L);
                out.writeLong(null);

                out.writeFloat(123.123f);
                out.writeFloat(-123.123f);
                out.writeFloat(null);

                out.writeDouble(123.123d);
                out.writeDouble(-123.123d);
                out.writeDouble(null);
            }

            {
                out.writeLocalDate(LocalDate.of(2025, 2, 23));
                out.writeLocalDate(LocalDate.of(0, 1, 1));
                out.writeLocalDate(null);

                out.writeLocalTime(LocalTime.of(12, 32, 45));
                out.writeLocalTime(LocalTime.of(0, 0, 0));
                out.writeLocalTime(null);

                out.writeLocalDateTime(LocalDateTime.of(LocalDate.of(2025, 2, 23), LocalTime.of(12, 32, 45)));
                out.writeLocalDateTime(LocalDateTime.of(LocalDate.of(0, 1, 1), LocalTime.of(0, 0, 0)));
                out.writeLocalDateTime(null);

                out.writeOffsetTime(OffsetTime.of(LocalTime.of(12, 32, 45), ZoneOffset.ofHours(8)));
                out.writeOffsetTime(OffsetTime.of(LocalTime.of(0, 0, 0), ZoneOffset.ofHours(8)));
                out.writeOffsetTime(null);

                out.writeOffsetDateTime(OffsetDateTime.of(LocalDate.of(2025, 2, 23), LocalTime.of(12, 32, 45), ZoneOffset.ofHours(8)));
                out.writeOffsetDateTime(OffsetDateTime.of(LocalDate.of(0, 1, 1), LocalTime.of(0, 0, 0), ZoneOffset.ofHours(8)));
                out.writeOffsetDateTime(null);
            }

            {
                out.writeBigInteger(BigInteger.valueOf(123456));
                out.writeBigInteger(BigInteger.valueOf(-123456));
                out.writeBigInteger(null);

                out.writeBigDecimal(new BigDecimal("1234.567890"));
                out.writeBigDecimal(new BigDecimal("-1234.567890"));
                out.writeBigDecimal(null);

                out.writeBytes(new byte[0]);
                out.writeBytes(new byte[] { 1, 2, 3 });
                out.writeBytes((byte[]) null);

                out.writeString("中文测试1", StandardCharsets.UTF_8);
                out.writeString("中文测试2", StandardCharsets.UTF_16BE);
                out.writeString((String) null, StandardCharsets.UTF_8);
            }
        }

        try (FileResultSetInputStream in = new FileResultSetInputStream(new File("test.dat"))) {
            in.nextRow();
            assert in.readCodeStatus() == 1234;

            {
                assert in.readBoolean();
                assert !in.readBoolean();
                assert in.readBoolean() == null;

                assert in.readByte() == 123;
                assert in.readByte() == -123;
                assert in.readByte() == null;

                assert in.readShort() == 123;
                assert in.readShort() == -123;
                assert in.readShort() == null;

                assert in.readInteger() == 123;
                assert in.readInteger() == -123;
                assert in.readInteger() == null;

                assert in.readLong() == 123;
                assert in.readLong() == -123;
                assert in.readLong() == null;

                assert in.readFloat() == 123.123f;
                assert in.readFloat() == -123.123f;
                assert in.readFloat() == null;

                assert in.readDouble() == 123.123d;
                assert in.readDouble() == -123.123d;
                assert in.readDouble() == null;
            }

            {
                assert in.readLocalDate().equals(LocalDate.of(2025, 2, 23));
                assert in.readLocalDate().equals(LocalDate.of(0, 1, 1));
                assert in.readLocalDate() == null;

                assert in.readLocalTime().equals(LocalTime.of(12, 32, 45));
                assert in.readLocalTime().equals(LocalTime.of(0, 0, 0));
                assert in.readLocalTime() == null;

                assert in.readLocalDateTime().equals(LocalDateTime.of(LocalDate.of(2025, 2, 23), LocalTime.of(12, 32, 45)));
                assert in.readLocalDateTime().equals(LocalDateTime.of(LocalDate.of(0, 1, 1), LocalTime.of(0, 0, 0)));
                assert in.readLocalDateTime() == null;

                assert in.readOffsetTime().equals(OffsetTime.of(LocalTime.of(12, 32, 45), ZoneOffset.ofHours(8)));
                assert in.readOffsetTime().equals(OffsetTime.of(LocalTime.of(0, 0, 0), ZoneOffset.ofHours(8)));
                assert in.readOffsetTime() == null;

                assert in.readOffsetDateTime().equals(OffsetDateTime.of(LocalDate.of(2025, 2, 23), LocalTime.of(12, 32, 45), ZoneOffset.ofHours(8)));
                assert in.readOffsetDateTime().equals(OffsetDateTime.of(LocalDate.of(0, 1, 1), LocalTime.of(0, 0, 0), ZoneOffset.ofHours(8)));
                assert in.readOffsetDateTime() == null;
            }

            {
                assert in.readBigInteger().longValue() == 123456L;
                assert in.readBigInteger().longValue() == -123456L;
                assert in.readBigInteger() == null;

                assert in.readBigDecimal().equals(new BigDecimal("1234.567890"));
                assert in.readBigDecimal().equals(new BigDecimal("-1234.567890"));
                assert in.readBigDecimal() == null;

                assert in.readBytes().length == 0;
                assert Arrays.equals(in.readBytes(), new byte[] { 1, 2, 3 });
                assert in.readBytes() == null;

                assert in.readString().equals("中文测试1");
                assert in.readString().equals("中文测试2");
                assert in.readString() == null;
            }
        }
    }

    @Test
    public void rowTest_1() throws IOException {
        try (FileResultSetOutputStream out = new FileResultSetOutputStream(new File("test.dat"), false)) {
            // row 1
            out.newRow();
            out.writeInteger(1);
            out.writeBoolean(false);
            out.writeString("hello word", StandardCharsets.UTF_8);

            // row 2
            out.newRow();
            out.writeInteger(2);
            out.writeBoolean(false);

            // row 3
            out.newRow();
            out.writeInteger(3);
            out.writeBoolean(false);
        }

        try (FileResultSetInputStream in = new FileResultSetInputStream(new File("test.dat"))) {
            assert in.getRowCount() == 3;

            // row 1
            in.nextRow();
            assert in.getRowHeader().getDataCount() == 3;
            assert in.readInteger() == 1;
            assert !in.readBoolean();
            assert in.readString().equals("hello word");

            // row 2
            in.nextRow();
            assert in.getRowHeader().getDataCount() == 2;
            assert in.readInteger() == 2;
            assert !in.readBoolean();

            // row 3
            in.nextRow();
            assert in.getRowHeader().getDataCount() == 2;
            assert in.readInteger() == 3;
            assert !in.readBoolean();
        }
    }

    @Test
    public void rowTest_2() throws IOException {
        try (FileResultSetOutputStream out = new FileResultSetOutputStream(new File("test.dat"), false)) {
            // row 1
            out.newRow();
            out.writeInteger(null);
            out.writeBoolean(null);
            out.writeString((String) null, StandardCharsets.UTF_8);
        }

        try (FileResultSetInputStream in = new FileResultSetInputStream(new File("test.dat"))) {
            assert in.getRowCount() == 1;

            in.nextRow();
            assert in.getRowHeader().getDataCount() == 3;
            assert in.readInteger() == null;
            assert in.readBoolean() == null;
            assert in.readString() == null;
        }
    }

    @Test
    public void rowTest_3() throws IOException {
        try (FileResultSetOutputStream out = new FileResultSetOutputStream(new File("test.dat"), false)) {
            // row 1
            out.newRow();
            out.writeInteger(null);
            out.writeBoolean(null);
            out.writeString((String) null, StandardCharsets.UTF_8);
        }

        try (FileResultSetInputStream in = new FileResultSetInputStream(new File("test.dat"))) {
            assert in.getRowCount() == 1;

            in.nextRow();
            assert in.getRowHeader().getDataCount() == 3;
            assert in.readInteger() == null;
            assert in.readBoolean() == null;
            assert in.readString() == null;
        }
    }

    @Test
    public void rowTest_4() throws IOException {
        try (FileResultSetOutputStream out = new FileResultSetOutputStream(new File("test.dat"), false)) {
            // row 1
            out.newRow();
            out.writeString("select * from abc", StandardCharsets.UTF_8);

            // row 2
            out.newRow();
            out.writeString("{A_123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890}", StandardCharsets.UTF_8);
            out.writeString("{B_123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890}", StandardCharsets.UTF_8);
            out.writeString("{C_123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890}", StandardCharsets.UTF_8);

            // row 3
            out.newRow();
            out.writeString("user", StandardCharsets.UTF_8);
            out.writeBoolean(false);
            out.writeBoolean(false);
        }

        try (FileResultSetInputStream in = new FileResultSetInputStream(new File("test.dat"))) {
            assert in.getRowCount() == 3;

            // row 1
            in.nextRow();
            assert in.getRowHeader().getDataCount() == 1;
            assert in.readString().equals("select * from abc");

            // row 2
            in.nextRow();
            assert in.getRowHeader().getDataCount() == 3;
            assert in.readString()
                .equals("{A_123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890}");
            assert in.readString()
                .equals("{B_123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890}");
            assert in.readString()
                .equals("{C_123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890}");

            // row 3
            in.nextRow();
            assert in.getRowHeader().getDataCount() == 3;
            assert in.readString().equals("user");
            assert !in.readBoolean();
            assert !in.readBoolean();
        }
    }
}
