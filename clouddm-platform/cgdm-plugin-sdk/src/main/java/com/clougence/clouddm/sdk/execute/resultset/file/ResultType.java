package com.clougence.clouddm.sdk.execute.resultset.file;

public interface ResultType {

    byte Code       = (byte) 0x00;
    byte Boolean    = (byte) 0x01;
    byte Byte       = (byte) 0x02;
    byte Short      = (byte) 0x03;
    byte Integer    = (byte) 0x04;
    byte Long       = (byte) 0x05;
    byte BigInteger = (byte) 0x06;
    byte Float      = (byte) 0x07;
    byte Double     = (byte) 0x08;
    byte String     = (byte) 0x09;
    byte Bytes      = (byte) 0x0A;
    byte Date       = (byte) 0x0B;
    byte Time       = (byte) 0x0C;
    byte TimeZ      = (byte) 0x0D;
    byte DateTime   = (byte) 0x0E;
    byte DateTimeZ  = (byte) 0x0F;
    byte BigDecimal = (byte) 0x10;
}
