package com.clougence.detectrule.runtime;

import java.text.ParseException;

public interface DataTimeValueParser {

    Object resolve(String fmt, String value) throws ParseException;
}
