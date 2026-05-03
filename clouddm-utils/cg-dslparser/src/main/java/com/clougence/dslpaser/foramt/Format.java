package com.clougence.dslpaser.foramt;

import java.io.IOException;

/**
 * format or toString print
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
public interface Format {

    void doFormat(FmtWriter writer) throws IOException;
}
