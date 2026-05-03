package com.clougence.detectrule.runtime.v1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

import com.clougence.detectrule.lang.LangObject;
import com.clougence.detectrule.lang.ValueObject;
import com.clougence.detectrule.lang.reflect.TypeType;
import com.clougence.detectrule.parser.ast.primary.XxType;
import com.clougence.detectrule.parser.ast.token.CastTypeDtFmtToken;
import com.clougence.detectrule.parser.ast.token.CastTypeNumFmtToken;
import com.clougence.detectrule.runtime.DataTimeValueParser;
import com.clougence.detectrule.runtime.DetectRuleRuntimeError;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

class CastUtils {

    public static final ValueObject NULL = new ValueObject(null, TypeType.Null);

    public static LangObject castAsBoolean(AstFragment inst, TypeType srcType, LangObject data) {
        switch (srcType) {
            case Null: {
                return new ValueObject(false, TypeType.Boolean);
            }
            case Boolean: {
                return data;
            }
            case Integer: {
                boolean bool = (Long) data.unwrap() != 0;
                return new ValueObject(bool, TypeType.Boolean);
            }
            case Float: {
                boolean bool = (Double) data.unwrap() != 0;
                return new ValueObject(bool, TypeType.Boolean);
            }
            case Decimal: {
                boolean bool = !(((BigDecimal) data.unwrap()).compareTo(BigDecimal.ZERO) == 0);
                return new ValueObject(bool, TypeType.Boolean);
            }
            case String: {
                String str = data.unwrap().toString();
                if (StringUtils.equalsIgnoreCase(str, "true")) {
                    return new ValueObject(true, TypeType.Boolean);
                } else if (StringUtils.equalsIgnoreCase(str, "false")) {
                    return new ValueObject(false, TypeType.Boolean);
                } else if (StringUtils.startsWith(str, "0x")) {
                    BigDecimal num = parseXx(inst, XxType.Hex, str);
                    return new ValueObject(!num.equals(BigDecimal.ZERO), TypeType.Boolean);
                } else if (StringUtils.startsWith(str, "0o")) {
                    BigDecimal num = parseXx(inst, XxType.Oct, str);
                    return new ValueObject(!num.equals(BigDecimal.ZERO), TypeType.Boolean);
                } else if (StringUtils.startsWith(str, "0b")) {
                    BigDecimal num = parseXx(inst, XxType.Bit, str);
                    return new ValueObject(!num.equals(BigDecimal.ZERO), TypeType.Boolean);
                } else if (NumberUtils.isNumber(str)) {
                    int num = NumberUtils.createNumber(str).intValue();
                    return new ValueObject(num > 0, TypeType.Boolean);
                } else {
                    throw new DetectRuleRuntimeError(inst, "the value '" + str + "' Cannot be cast to Boolean.");
                }
            }
            default:
                throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Boolean.");
        }
    }

    public static LangObject castAsInteger(AstFragment inst, TypeType srcType, LangObject data, CastTypeNumFmtToken fmt) {
        switch (srcType) {
            case Null: {
                return new ValueObject(0L, TypeType.Integer);
            }
            case Boolean: {
                boolean bool = (Boolean) data.unwrap();
                return new ValueObject(bool ? 1L : 0L, TypeType.Integer);
            }
            case Integer: {
                return data;
            }
            case Float: {
                Double val = (Double) data.unwrap();
                if (fmt == null) {
                    return new ValueObject(val.longValue(), TypeType.Integer);
                } else {
                    BigDecimal num = fmtNumberScale(inst, new BigDecimal(val), fmt);
                    return new ValueObject(num.longValue(), TypeType.Integer);
                }
            }
            case Decimal: {
                BigDecimal val = (BigDecimal) data.unwrap();
                if (fmt == null) {
                    return new ValueObject(val.longValue(), TypeType.Integer);
                } else {
                    BigDecimal num = fmtNumberScale(inst, val, fmt);
                    return new ValueObject(num.longValue(), TypeType.Integer);
                }
            }
            case String: {
                String str = data.unwrap().toString();
                long num;

                if (StringUtils.startsWith(str, "0x")) {
                    num = parseXx(inst, XxType.Hex, str).longValue();
                } else if (StringUtils.startsWith(str, "0o")) {
                    num = parseXx(inst, XxType.Oct, str).longValue();
                } else if (StringUtils.startsWith(str, "0b")) {
                    num = parseXx(inst, XxType.Bit, str).longValue();
                } else {
                    try {
                        num = Long.parseLong(str);
                    } catch (NumberFormatException e1) {
                        try {
                            num = new BigDecimal(str).longValue();
                        } catch (NumberFormatException e2) {
                            throw new DetectRuleRuntimeError(inst, "the value '" + str + "' Cannot be cast to Int.");
                        }
                    }
                }
                return new ValueObject(num, TypeType.Integer);
            }
            default:
                throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Boolean.");
        }
    }

    public static LangObject castAsFloat(AstFragment inst, TypeType srcType, LangObject data, CastTypeNumFmtToken fmt) {
        switch (srcType) {
            case Null: {
                return new ValueObject(0.0d, TypeType.Float);
            }
            case Boolean: {
                boolean bool = (Boolean) data.unwrap();
                return new ValueObject(bool ? 1.0d : 0.0d, TypeType.Float);
            }
            case Integer: {
                Long val = (Long) data.unwrap();
                return new ValueObject(val.doubleValue(), TypeType.Float);
            }
            case Float: {
                return data;
            }
            case Decimal: {
                BigDecimal val = (BigDecimal) data.unwrap();
                if (fmt == null) {
                    return new ValueObject(val.doubleValue(), TypeType.Float);
                } else {
                    BigDecimal num = fmtNumberScale(inst, val, fmt);
                    return new ValueObject(num.doubleValue(), TypeType.Float);
                }
            }
            case String: {
                String str = data.unwrap().toString();
                double num;

                if (StringUtils.startsWith(str, "0x")) {
                    num = parseXx(inst, XxType.Hex, str).longValue();
                } else if (StringUtils.startsWith(str, "0o")) {
                    num = parseXx(inst, XxType.Oct, str).longValue();
                } else if (StringUtils.startsWith(str, "0b")) {
                    num = parseXx(inst, XxType.Bit, str).longValue();
                } else {
                    try {
                        num = Double.parseDouble(str);
                    } catch (NumberFormatException e1) {
                        try {
                            num = new BigDecimal(str).doubleValue();
                        } catch (NumberFormatException e2) {
                            throw new DetectRuleRuntimeError(inst, "the value '" + str + "' Cannot be cast to Float.");
                        }
                    }
                }
                return new ValueObject(num, TypeType.Float);
            }
            default:
                throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Float.");
        }
    }

    public static LangObject castAsDecimal(AstFragment inst, TypeType srcType, LangObject data, CastTypeNumFmtToken fmt) {
        switch (srcType) {
            case Null: {
                return new ValueObject(BigDecimal.ZERO, TypeType.Decimal);
            }
            case Boolean: {
                boolean bool = (Boolean) data.unwrap();
                return new ValueObject(bool ? BigDecimal.ONE : BigDecimal.ZERO, TypeType.Decimal);
            }
            case Integer: {
                Long val = (Long) data.unwrap();
                return new ValueObject(new BigDecimal(val), TypeType.Decimal);
            }
            case Float: {
                Double val = (Double) data.unwrap();
                if (fmt == null) {
                    return new ValueObject(new BigDecimal(val), TypeType.Decimal);
                } else {
                    BigDecimal num = fmtNumberScale(inst, new BigDecimal(val), fmt);
                    return new ValueObject(num.doubleValue(), TypeType.Decimal);
                }
            }
            case Decimal: {
                return data;
            }
            case String: {
                String str = data.unwrap().toString();
                BigDecimal num;

                if (StringUtils.startsWith(str, "0x")) {
                    num = parseXx(inst, XxType.Hex, str);
                } else if (StringUtils.startsWith(str, "0o")) {
                    num = parseXx(inst, XxType.Oct, str);
                } else if (StringUtils.startsWith(str, "0b")) {
                    num = parseXx(inst, XxType.Bit, str);
                } else {
                    try {
                        num = new BigDecimal(str);
                    } catch (NumberFormatException e1) {
                        throw new DetectRuleRuntimeError(inst, "the value '" + str + "' Cannot be cast to Decimal.");
                    }
                }
                return new ValueObject(num, TypeType.Decimal);
            }
            default:
                throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Decimal.");
        }
    }

    private static BigDecimal fmtNumberScale(AstFragment inst, BigDecimal bigDecimal, CastTypeNumFmtToken fmt) {
        int digit = Integer.parseInt(fmt.getDigit().getValue());
        RoundingMode rounding;
        if (fmt.getRounding() == null) {
            rounding = RoundingMode.HALF_UP;
        } else {
            rounding = fmt.getRounding().getRounding();
        }

        try {
            return bigDecimal.setScale(digit, rounding);
        } catch (Exception e) {
            throw new DetectRuleRuntimeError(inst, "the number '" + bigDecimal.toPlainString() + "' cast as scale failed.", e);
        }
    }

    private static BigDecimal parseXx(AstFragment inst, XxType xxType, String value) {
        value = value.substring(2);
        value = StringUtils.trimStart(value, '0');

        int dataLen = value.length();
        if (dataLen == 0) {
            return BigDecimal.ZERO;
        }
        switch (xxType) {
            case Bit:
                return new BigDecimal(new BigInteger(value, 2));
            case Oct:
                return new BigDecimal(new BigInteger(value, 8));
            case Hex:
                return new BigDecimal(new BigInteger(value, 16));
            default:
                throw new DetectRuleRuntimeError(inst, "the value '" + value + "' is not Bit or Oct or Hex.");
        }
    }

    public static LangObject castAsDate(AstFragment inst, TypeType srcType, LangObject data) {
        switch (srcType) {
            case Null: {
                return new ValueObject(null, TypeType.Null);
            }
            case Date:
            case Time:
            case Datetime: {
                return (data.unwrap() instanceof java.time.LocalDate) ? data : RefUtils.toDateOrNull(inst, srcType, data.unwrap());
            }
            case String: {
                String val = (String) data.unwrap();
                int valLen = val.length();
                LocalDate resolve = null;
                if (resolve == null && valLen >= 19) {
                    resolve = tryParse(val, s -> OffsetDateTime.parse(s).toLocalDate());
                }
                if (resolve == null && valLen >= 19) {
                    resolve = tryParse(val, s -> LocalDateTime.parse(s).toLocalDate());
                }
                if (resolve == null && valLen >= 19) {
                    resolve = tryParse(val, s -> LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate());
                }
                if (resolve == null && valLen == 10) {
                    resolve = tryParse(val, LocalDate::parse);
                }
                if (resolve == null && valLen >= 10) {
                    int codeA = val.indexOf(":");
                    int codeB = val.indexOf("-");
                    int codeC = val.indexOf("+");
                    if ((codeA < codeB && codeB > 0) || (codeA < codeC && codeC > 0)) {
                        resolve = LocalDate.of(1970, 1, 1);
                    }
                }
                if (resolve == null && valLen == 8) {
                    resolve = tryParse(val, s -> {
                        LocalTime.parse(s);
                        return LocalDate.of(1970, 1, 1);
                    });
                }

                if (resolve == null) {
                    throw new DetectRuleRuntimeError(inst, "the value '" + val + "' Cannot be cast to Date.");
                } else {
                    return new ValueObject(resolve, TypeType.Date);
                }
            }
            default:
                throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Date.");
        }
    }

    public static LangObject castAsTime(AstFragment inst, TypeType srcType, LangObject data) {
        switch (srcType) {
            case Null: {
                return new ValueObject(null, TypeType.Null);
            }
            case Date:
            case Time:
            case Datetime: {
                return (data.unwrap() instanceof java.time.LocalTime) ? data : RefUtils.toTimeOrNull(inst, srcType, data.unwrap());
            }
            case String: {
                String val = (String) data.unwrap();
                int valLen = val.length();
                LocalTime resolve = null;
                if (resolve == null && valLen >= 19) {
                    resolve = tryParse(val, s -> OffsetDateTime.parse(s).toLocalTime());
                }
                if (resolve == null && valLen >= 19) {
                    resolve = tryParse(val, s -> LocalDateTime.parse(s).toLocalTime());
                }
                if (resolve == null && valLen >= 19) {
                    resolve = tryParse(val, s -> LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalTime());
                }
                if (resolve == null && valLen >= 10) {
                    resolve = tryParse(val, s -> OffsetTime.parse(s).toLocalTime());
                }
                if (resolve == null && valLen >= 10) {
                    resolve = tryParse(val, s -> {
                        LocalDate.parse(s);
                        return LocalTime.of(0, 0, 0);
                    });
                }
                if (resolve == null && valLen >= 8) {
                    resolve = tryParse(val, LocalTime::parse);
                }

                if (resolve == null) {
                    throw new DetectRuleRuntimeError(inst, "the value '" + val + "' Cannot be cast to Time.");
                } else {
                    return new ValueObject(resolve, TypeType.Time);
                }
            }
            default:
                throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Time.");
        }
    }

    public static LangObject castAsDateTime(AstFragment inst, TypeType srcType, LangObject data) {
        switch (srcType) {
            case Null: {
                return new ValueObject(null, TypeType.Null);
            }
            case Date:
            case Time:
            case Datetime: {
                return (data.unwrap() instanceof java.time.LocalDateTime) ? data : RefUtils.toDatetimeOrNull(inst, srcType, data.unwrap());
            }
            case String: {
                String val = (String) data.unwrap();
                int valLen = val.length();
                LocalDateTime resolve = null;
                if (resolve == null && valLen >= 19) {
                    resolve = tryParse(val, s -> OffsetDateTime.parse(s).toLocalDateTime());
                }
                if (resolve == null && valLen >= 19) {
                    resolve = tryParse(val, LocalDateTime::parse);
                }
                if (resolve == null && valLen >= 19) {
                    resolve = tryParse(val, s -> LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
                if (resolve == null && valLen == 10) {
                    resolve = tryParse(val, s -> LocalDateTime.of(LocalDate.parse(s), LocalTime.of(0, 0, 0)));
                }
                if (resolve == null && valLen >= 8) {
                    resolve = tryParse(val, s -> LocalDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.parse(s)));
                }
                if (resolve == null && valLen >= 8) {
                    int codeA = val.indexOf(":");
                    int codeB = val.indexOf("-");
                    int codeC = val.indexOf("+");
                    if ((codeA < codeB && codeB > 0) || (codeA < codeC && codeC > 0)) {
                        resolve = tryParse(val, s -> LocalDateTime.of(LocalDate.of(1970, 1, 1), OffsetTime.parse(s).toLocalTime()));
                    }
                }

                if (resolve == null) {
                    throw new DetectRuleRuntimeError(inst, "the value '" + val + "' Cannot be cast to Datetime.");
                } else {
                    return new ValueObject(resolve, TypeType.Datetime);
                }
            }
            default:
                throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Datetime.");
        }
    }

    public static LangObject castAsDateTimeFormat(AstFragment inst, TypeType srcType, LangObject data, CastTypeDtFmtToken fmt, DataTimeValueParser valueParser) {
        switch (srcType) {
            case Null: {
                return new ValueObject(null, TypeType.Null);
            }
            case Date:
            case Time:
            case Datetime: {
                return castAsDateTime(inst, srcType, data);
            }
            case String: {
                String fmtStr = fmt.getFormat().getValue();
                if (StringUtils.isBlank(fmtStr)) {
                    throw new DetectRuleRuntimeError(inst, "the '" + inst + "' data parsing failed, format is blank.");
                }
                if (valueParser == null) {
                    throw new DetectRuleRuntimeError(inst, "the '" + inst + "' data parsing failed, DataTimeValueParser provider is undefined.");
                }

                Object resolve = null;
                try {
                    resolve = valueParser.resolve(fmtStr, (String) data.unwrap());
                } catch (Exception e) {
                    throw new DetectRuleRuntimeError(inst, "the '" + inst + "' data parsing failed, " + e.getMessage(), e);
                }

                if (resolve == null) {
                    throw new DetectRuleRuntimeError(inst, "the '" + inst + "' data parsing failed.");
                }

                TypeType typeType = TypeType.valueOfType(resolve.getClass());
                if (typeType != TypeType.Date && typeType != TypeType.Time && typeType != TypeType.Datetime) {
                    throw new DetectRuleRuntimeError(inst, "the '" + inst + "' data parsing failed.");
                }

                return new ValueObject(resolve, typeType);
            }
            default:
                throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Datetime.");
        }
    }

    public static LangObject castAsString(AstFragment inst, TypeType srcType, LangObject data) {
        switch (srcType) {
            case Null:
                return NULL;
            case String:
                return data;
            case Boolean:
            case Integer:
            case Float:
                return new ValueObject(data.unwrap().toString(), TypeType.String);
            case Decimal:
                return new ValueObject(((BigDecimal) data.unwrap()).toPlainString(), TypeType.String);
            default:
                return new ValueObject(data.unwrap().toString(), TypeType.String);
        }
    }

    public static <T> T tryParse(String value, Function<String, T> func) {
        try {
            return func.apply(value);
        } catch (DateTimeParseException e1) {
            return null;
        }
    }
}
