/*
 * Copyright 2008-2009 the original author or authors.
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
 */
package com.clougence.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 *
 * @version : 2012-6-21
 * @author 赵永春 (zyc@hasor.net)
 */
public class HexadecimalUtils {

    public static String bytes2hex(final byte[] b) {
        if (b == null) {
            return null;
        } else if (b.length == 0) {
            return "";
        }

        StringBuilder hs = new StringBuilder();
        for (byte element : b) {
            String stmp = Integer.toHexString(element & 0XFF).toUpperCase();
            if (stmp.length() == 1) {
                hs.append("0");
                hs.append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();
    }

    public static byte[] hex2bytes(String hexStr) {
        if (hexStr == null) {
            return null;
        } else if (StringUtils.isBlank(hexStr)) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        if (hexStr.length() % 2 == 1) {
            hexStr = "0" + hexStr;
        }

        int count = hexStr.length() / 2;
        byte[] ret = new byte[count];
        for (int i = 0; i < count; i++) {
            int index = i * 2;
            char c1 = hexStr.charAt(index);
            char c2 = hexStr.charAt(index + 1);
            ret[i] = (byte) (toByte(c1) << 4);
            ret[i] = (byte) (ret[i] | toByte(c2));
        }
        return ret;
    }

    public static byte[] bit2bytes(String bitString) {
        if (bitString == null) {
            return null;
        } else if (StringUtils.isBlank(bitString)) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        int supplement = 8 - (bitString.length() % 8);
        bitString = (supplement == 8) ? bitString : (StringUtils.repeat("0", supplement) + bitString);
        StringReader bitReader = new StringReader(bitString);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            while (true) {
                char c1 = readB(bitReader);
                char c2 = readB(bitReader);
                if (c1 == '\u0000' && c2 == '\u0000') {
                    break;
                }

                byte aByte = 0;
                aByte = (byte) (toByte(c1) << 4);
                aByte = (byte) (aByte | toByte(c2));

                out.write(aByte);
            }
        } catch (Exception e) {
            throw ExceptionUtils.toRuntime(e);
        }

        return out.toByteArray();
    }

    private static byte toByte(char src) {
        switch (Character.toUpperCase(src)) {
            case '0':
                return 0x0;
            case '1':
                return 0x1;
            case '2':
                return 0x2;
            case '3':
                return 0x3;
            case '4':
                return 0x4;
            case '5':
                return 0x5;
            case '6':
                return 0x6;
            case '7':
                return 0x7;
            case '8':
                return 0x8;
            case '9':
                return 0x9;
            case 'A':
                return 0xA;
            case 'B':
                return 0xB;
            case 'C':
                return 0xC;
            case 'D':
                return 0xD;
            case 'E':
                return 0xE;
            case 'F':
                return 0xF;
            default:
                throw new IllegalStateException("0-F");
        }
    }

    public static String bit2hex(String bitString) {
        if (bitString == null) {
            return null;
        } else if (StringUtils.isBlank(bitString)) {
            return "";
        }

        int supplement = 8 - (bitString.length() % 8);
        bitString = (supplement == 8) ? bitString : (StringUtils.repeat("0", supplement) + bitString);
        StringReader bitReader = new StringReader(bitString);
        StringBuilder bitWriter = new StringBuilder();

        try {
            while (true) {
                char c1 = readB(bitReader);
                char c2 = readB(bitReader);
                if (c1 == '\u0000' && c2 == '\u0000') {
                    break;
                }

                bitWriter.append(c1).append(c2);
            }
        } catch (Exception e) {
            throw ExceptionUtils.toRuntime(e);
        }

        return bitWriter.toString();
    }

    private static char readB(StringReader reader) throws IOException {
        char[] oct = new char[4];
        int read = -1;
        while ((read = reader.read(oct)) > 0) {
            switch (new String(oct)) {
                case "0000":
                    return '0';
                case "0001":
                    return '1';
                case "0010":
                    return '2';
                case "0011":
                    return '3';
                case "0100":
                    return '4';
                case "0101":
                    return '5';
                case "0110":
                    return '6';
                case "0111":
                    return '7';
                case "1000":
                    return '8';
                case "1001":
                    return '9';
                case "1010":
                    return 'A';
                case "1011":
                    return 'B';
                case "1100":
                    return 'C';
                case "1101":
                    return 'D';
                case "1110":
                    return 'E';
                case "1111":
                    return 'F';
            }
        }
        return '\0';
    }

    public static String bytes2bit(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else if (bytes.length == 0) {
            return "";
        } else if (bytes.length == 1 && bytes[0] == 0) {
            return "0";
        }

        StringBuilder strBuild = new StringBuilder();
        for (byte abyte : bytes) {
            byte c1 = (byte) (0x0F & abyte >> 4);
            byte c2 = (byte) (0x0F & abyte);

            if (strBuild.length() == 0) {
                String first = toBit(c1) + toBit(c2);
                for (char c : first.toCharArray()) {
                    if (strBuild.length() == 0 && c == '0') {
                        continue;
                    } else {
                        strBuild.append(c);
                    }
                }
            } else {
                strBuild.append(toBit(c1)).append(toBit(c2));
            }
        }

        return strBuild.toString();
    }

    private static String toBit(byte charB) {
        switch (charB) {
            case 0:
                return "0000";
            case 1:
                return "0001";
            case 2:
                return "0010";
            case 3:
                return "0011";
            case 4:
                return "0100";
            case 5:
                return "0101";
            case 6:
                return "0110";
            case 7:
                return "0111";
            case 8:
                return "1000";
            case 9:
                return "1001";
            case 10:
                return "1010";
            case 11:
                return "1011";
            case 12:
                return "1100";
            case 13:
                return "1101";
            case 14:
                return "1110";
            case 15:
                return "1111";
            default:
                throw new IllegalStateException("0-15");
        }
    }

    public static String hex2bit(String hexStr) {
        if (hexStr == null) {
            return null;
        } else if (StringUtils.isBlank(hexStr)) {
            return "";
        }

        StringBuilder bitWriter = new StringBuilder();
        for (char charB : hexStr.toCharArray()) {
            switch (Character.toUpperCase(charB)) {
                case '0':
                    bitWriter.append("0000");
                    break;
                case '1':
                    bitWriter.append("0001");
                    break;
                case '2':
                    bitWriter.append("0010");
                    break;
                case '3':
                    bitWriter.append("0011");
                    break;
                case '4':
                    bitWriter.append("0100");
                    break;
                case '5':
                    bitWriter.append("0101");
                    break;
                case '6':
                    bitWriter.append("0110");
                    break;
                case '7':
                    bitWriter.append("0111");
                    break;
                case '8':
                    bitWriter.append("1000");
                    break;
                case '9':
                    bitWriter.append("1001");
                    break;
                case 'A':
                    bitWriter.append("1010");
                    break;
                case 'B':
                    bitWriter.append("1011");
                    break;
                case 'C':
                    bitWriter.append("1100");
                    break;
                case 'D':
                    bitWriter.append("1101");
                    break;
                case 'E':
                    bitWriter.append("1110");
                    break;
                case 'F':
                    bitWriter.append("1111");
                    break;
                default:
                    throw new IllegalStateException("0-F");
            }
        }

        while (bitWriter.length() > 0) {
            if (bitWriter.charAt(0) == '0') {
                bitWriter.deleteCharAt(0);
            } else {
                break;
            }
        }
        return bitWriter.length() == 0 ? "0" : bitWriter.toString();
    }

    public static boolean isHexNumber(String str) {
        // fix:if str is empty, it will return true.
        if (StringUtils.isBlank(str)) {
            return false;
        }

        boolean flag = true;

        if (str.startsWith("0x") || str.startsWith("0X")) {
            str = str.substring(2);
        }

        for (int i = 0; i < str.length(); i++) {
            char cc = str.charAt(i);
            if (cc == '0' || cc == '1' || cc == '2' || cc == '3' || cc == '4' || cc == '5' || cc == '6' || cc == '7' || cc == '8' || cc == '9' || cc == 'A' || cc == 'B'
                || cc == 'C' || cc == 'D' || cc == 'E' || cc == 'F' || cc == 'a' || cc == 'b' || cc == 'c' || cc == 'd' || cc == 'e' || cc == 'f') {
                continue;
            } else {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
