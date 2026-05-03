package com.clougence.dslpaser.foramt;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;

/**
 * format or toString print
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
public class FmtWriter extends Writer {

    // for print
    private String                           lineSeparator;
    private final Writer                     writer;
    private final Map<String, String>        defaultOption;
    private Map<String, String>              curOption;
    private final Stack<Map<String, String>> optionStack;

    // for status
    private int                              depth;
    private boolean                          lastIsBlank;
    private boolean                          lastIsNewLine;

    public FmtWriter(Writer writer){
        this(writer, Collections.emptyMap());
    }

    public FmtWriter(Writer writer, Map<String, String> defaultOption){
        this.writer = writer;
        this.defaultOption = defaultOption;
        this.curOption = defaultOption;
        this.optionStack = new Stack<>();
        this.configFormat(defaultOption);
    }

    protected void configFormat(Map<String, String> option) {
        String lineSeparator = FmtOptions.FILE_ENCODE_LINE_SEPARATOR.getString(option);
        if (StringUtils.equalsIgnoreCase(lineSeparator, "CRLF")) {
            this.lineSeparator = "\r\n";

        } else if (StringUtils.equalsIgnoreCase(lineSeparator, "LF")) {
            this.lineSeparator = "\n";
        } else {
            this.lineSeparator = SystemUtils.isWindows() ? "CRLF" : "LF";
        }
        this.depth = 0;
        this.lastIsBlank = true;
        this.lastIsNewLine = true;
    }

    protected Map<String, String> findOption(FmtOption opt) {
        if (this.curOption.containsKey(opt.getKey())) {
            return this.curOption;
        } else {
            for (Map<String, String> map : this.optionStack) {
                if (map.containsKey(opt.getKey())) {
                    return map;
                }
            }
            return Collections.unmodifiableMap(this.defaultOption);
        }
    }

    public String getStringOption(FmtOption opt) {
        return opt.getString(findOption(opt));
    }

    public int getIntOption(FmtOption opt) {
        return opt.getInteger(findOption(opt));
    }

    public boolean getBooleanOption(FmtOption opt) {
        return opt.getBoolean(findOption(opt));
    }

    private char getTabsChar() {
        FmtOption opt = FmtOptions.TABS_AND_INDENTS_USE_TAB_CHAR;
        boolean tabsChar = opt.getBoolean(findOption(opt));
        return tabsChar ? '\t' : ' ';
    }

    private void flashLast(char last) {
        if (last == ' ' || last == '\t') {
            this.lastIsBlank = true;
            this.lastIsNewLine = false;
        } else if (last == '\r' || last == '\n') {
            this.lastIsBlank = true;
            this.lastIsNewLine = true;
        } else {
            this.lastIsBlank = false;
            this.lastIsNewLine = false;
        }
    }

    @Override
    public void write(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return;
        }

        this.writer.write(str);
        this.flashLast(str.charAt(str.length() - 1));
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        if (cbuf == null || cbuf.length == 0) {
            return;
        }

        this.writer.write(cbuf);
        this.flashLast(cbuf[cbuf.length - 1]);
    }

    @Override
    public void write(char[] buf, int off, int len) throws IOException {
        if (len <= 0) {
            return;
        }

        this.writer.write(buf, off, len);
        this.flashLast(buf[off + len]);
    }

    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }

    public void writeSymbol(String symbol) throws IOException {
        String mode = this.getStringOption(FmtOptions.WRAPPING_WRAP_SYMBOL);
        if (StringUtils.equalsIgnoreCase(mode, "Before") || StringUtils.equalsIgnoreCase(mode, "Around")) {
            if (!this.lastIsBlank) {
                this.writer.write(" ");
            }
        }

        this.writer.write(symbol);

        if (StringUtils.equalsIgnoreCase(mode, "After") || StringUtils.equalsIgnoreCase(mode, "Around")) {
            this.writer.write(" ");
            this.lastIsBlank = true;
            this.lastIsNewLine = false;
        } else {
            this.lastIsBlank = false;
            this.lastIsNewLine = false;
        }
    }

    public void incrDepth() {
        this.incrDepth(1);
    }

    public void incrDepth(int depth) {
        this.depth += depth;
    }

    public void decrDepth() {
        this.decrDepth(1);
    }

    public void decrDepth(int depth) {
        this.depth -= depth;
    }

    public void updateOption(FmtOption opt, String newValue) {
        if (this.curOption == this.defaultOption) {
            throw new IllegalStateException("defaultOption not allowed to be modified, please use stashOption to create temp.");
        }

        this.curOption.put(opt.getKey(), newValue);
    }

    public void recoverOption(FmtOption opt) {
        if (this.curOption == this.defaultOption) {
            throw new IllegalStateException("defaultOption not allowed to be modified, please use stashOption to create temp.");
        }

        this.curOption.remove(opt.getKey());
    }

    public void stashOption() {
        this.optionStack.push(this.curOption);
        this.curOption = new HashMap<>(this.curOption);
    }

    public void unStashOption() {
        if (this.optionStack.isEmpty()) {
            this.curOption = this.defaultOption;
        } else {
            this.curOption = this.optionStack.pop();
        }
    }

    public boolean lastIsBlank() {
        return this.lastIsBlank;
    }

    public boolean lastIsNewLine() {
        return this.lastIsNewLine;
    }

    public void newLine() throws IOException {
        this.writer.write(this.lineSeparator);
        this.lastIsBlank = true;
        this.lastIsNewLine = true;
    }

    public void fillSpace() throws IOException {
        char tabsChar = this.getTabsChar();
        if (tabsChar == '\t') {
            this.writer.write(StringUtils.repeat(tabsChar, this.depth));
        } else {
            int indents = this.getIntOption(FmtOptions.TABS_AND_INDENTS_INDENTS_SIZE);
            this.writer.write(StringUtils.repeat(tabsChar, this.depth * indents));
        }
    }

    public void newLineAndIndents() throws IOException {
        this.newLine();
        this.fillSpace();
    }

}
