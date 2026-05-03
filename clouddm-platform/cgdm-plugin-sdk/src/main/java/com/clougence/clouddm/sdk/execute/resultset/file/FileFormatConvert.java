package com.clougence.clouddm.sdk.execute.resultset.file;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;

import com.clougence.clouddm.sdk.Spi;

public interface FileFormatConvert extends Spi {

    // MIME, https://mime.wcode.net/zh-hans/
    String name();

    String extension();

    String descriptionI18n();

    String iconName();

    long convert(String exportId, DmFileType srcType, File srcFile, File dstFile, Logger logger, FileFormatConvertReport report, String option) throws IOException;

    default Map<String, Object> getOption() { return Collections.emptyMap(); }
}
