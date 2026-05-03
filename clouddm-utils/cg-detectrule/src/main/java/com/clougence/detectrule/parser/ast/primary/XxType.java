package com.clougence.detectrule.parser.ast.primary;

import lombok.Getter;

/**
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
public enum XxType {
    /* use `0b01010101100` or `0B01010101100` */
    Bit,
    /* use `0o1234567` or `0O1234567` */
    Oct,
    /* use `0x12345` or `0X12345` */
    Hex,
}
