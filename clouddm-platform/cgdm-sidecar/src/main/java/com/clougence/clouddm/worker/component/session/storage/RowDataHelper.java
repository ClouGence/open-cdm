package com.clougence.clouddm.worker.component.session.storage;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;

import com.clougence.clouddm.sdk.execute.session.result.ReaderOptions;
import com.clougence.utils.StringUtils;

public class RowDataHelper {

    public static String displayString(int displayChars, String str) {
        if (str == null) {
            return null;
        }

        if (str.length() > displayChars) {
            return str.substring(0, displayChars);
        } else {
            return str;
        }
    }

    public static String displayBoolean(int displayChars, Boolean v) {
        if (v == null) {
            return null;
        } else if (displayChars >= 5) {
            return v ? "True" : "False";
        } else if (displayChars >= 3) {
            return v ? "Yes" : "No";
        } else {
            return v ? "Y" : "N";
        }
    }

    public static String displayByte(int displayChars, Byte v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, v.toString());
        }
    }

    public static String displayShort(int displayChars, Short v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, v.toString());
        }
    }

    public static String displayInteger(int displayChars, Integer v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, v.toString());
        }
    }

    public static String displayLong(int displayChars, Long v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, v.toString());
        }
    }

    public static String displayBigInteger(int displayChars, BigInteger v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, v.toString(10));
        }
    }

    public static String displayBigDecimal(int displayChars, BigDecimal v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, v.toPlainString());
        }
    }

    public static String displayFloat(int displayChars, Float v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, v.toString());
        }
    }

    public static String displayDouble(int displayChars, Double v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, v.toString());
        }
    }

    public static String displayLocalDate(int displayChars, ReaderOptions options, LocalDate v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, options.getDataFormat().format(v));
        }
    }

    public static String displayLocalTime(int displayChars, ReaderOptions options, LocalTime v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, options.getTimeFormat().format(v));
        }
    }

    public static String displayOffsetTime(int displayChars, ReaderOptions options, OffsetTime v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, options.getTimeWithZoneFormat().format(v));
        }
    }

    public static String displayLocalDateTime(int displayChars, ReaderOptions options, LocalDateTime v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, options.getDataTimeFormat().format(v));
        }
    }

    public static String displayOffsetDateTime(int displayChars, ReaderOptions options, OffsetDateTime v) {
        if (v == null) {
            return null;
        } else {
            return displayString(displayChars, options.getDataTimeWithZoneFormat().format(v));
        }
    }

    public static String displayUnsupported(int displayChars, String columnType) {
        if (displayChars <= 3) {
            return "N/A";
        } else if (displayChars <= 6) {
            return "Unsup.";
        } else if (displayChars <= 12 || StringUtils.isBlank(columnType)) {
            return "Unsupported.";
        } else {
            return "Unsupported " + columnType + " type.";
        }
    }
}
