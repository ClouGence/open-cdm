/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.console.web.service.upload.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SqlFilePreviewReader {

    private static final int MAX_PREVIEW_LINE_CHARS = 65536;

    private SqlFilePreviewReader(){
    }

    public static SqlFilePreviewData read(Path file, int startLine, int lineCount) throws IOException {
        int requestedStartLine = Math.max(1, startLine);
        int requestedLineCount = Math.max(1, lineCount);
        PreviewContent preview = readLines(file, requestedStartLine, requestedLineCount);

        if (preview.content().isEmpty() && preview.totalLines() > 0) {
            int lastPageStart = Math.max(1, preview.totalLines() - requestedLineCount + 1);
            preview = readLines(file, lastPageStart, requestedLineCount);
            requestedStartLine = lastPageStart;
        }

        return new SqlFilePreviewData(requestedStartLine, preview.totalLines(), preview.content(), requestedStartLine + preview.returnedLines() > preview.totalLines());
    }

    private static PreviewContent readLines(Path file, int startLine, int lineCount) throws IOException {
        StringBuilder content = new StringBuilder();
        int totalLines = 0;
        int returnedLines = 0;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                if (totalLines < startLine || returnedLines >= lineCount) {
                    continue;
                }
                if (returnedLines > 0) {
                    content.append('\n');
                }
                if (line.length() > MAX_PREVIEW_LINE_CHARS) {
                    content.append(line, 0, MAX_PREVIEW_LINE_CHARS).append('…');
                } else {
                    content.append(line);
                }
                returnedLines++;
            }
        }
        return new PreviewContent(content.toString(), totalLines, returnedLines);
    }

    private record PreviewContent(String content, int totalLines, int returnedLines) {
    }
}
