package com.clougence.dslpaser.ast.token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.HexadecimalUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * Basic ast token
 * @author zyc@hasor.net
 * @version : 2020-06-11
 */
@Getter
@Setter
public class StringToken extends AstFragment {

    private String wrap;
    private String value;

    public StringToken(String wrap, String value){
        this.wrap = Objects.requireNonNull(wrap, "wrap char is null.");
        this.value = Objects.requireNonNull(value, "string is null.");
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        String wrap = this.wrap;
        String value = this.value;

        if (wrap == null) {
            throw new IllegalStateException("wrap char is null.");
        }

        writer.write(wrap);
        writer.write(value);
        writer.write(wrap);
    }

    // https://www.rfc-editor.org/rfc/rfc4627.txt
    //
    //  string = quotation-mark *char quotation-mark
    //  char = unescaped /
    //         escape (
    //             %x22 /          ; "    quotation mark  U+0022
    //             %x5C /          ; \    reverse solidus U+005C
    //             %x2F /          ; /    solidus         U+002F
    //             %x72 /          ; r    carriage return U+000D
    //             %x6E /          ; n    line feed       U+000A
    //             %x74 /          ; t    tab             U+0009
    //             %x62 /          ; b    backspace       U+0008
    //             %x66 /          ; f    form feed       U+000C
    //             %x75 4HEXDIG )  ; uXXXX                U+XXXX
    //  escape = %x5C              ; \
    //  quotation-mark = %x22      ; "
    //  unescaped = %x20-21 / %x23-5B / %x5D-10FFFF
    //
    //
    // https://codepoints.net/basic_latin
    // https://www.htmlsymbols.xyz/unicode
    //
    //
    //

    public String getEscapeValue() {
        String strValue = this.getValue();
        int last = strValue.indexOf("\\");
        if (last == -1) {
            return strValue;
        }

        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < strValue.length(); i++) {
            if (strValue.charAt(i) != '\\') {
                strBuilder.append(strValue.charAt(i));
                continue;
            }

            char nextCode = strValue.charAt(i + 1);
            if (nextCode == 'u' || nextCode == 'U') {
                String ucode = strValue.substring(i + 2, i + 6);
                byte[] bytes = HexadecimalUtils.hex2bytes(ucode);
                strBuilder.append(new String(bytes, StandardCharsets.UTF_16).charAt(0));
                i = i + 5;
            } else if (nextCode == 'r' || nextCode == 'R') {
                strBuilder.append("\r");
                i++;
            } else if (nextCode == 'n' || nextCode == 'N') {
                strBuilder.append("\n");
                i++;
            } else if (nextCode == 't' || nextCode == 'T') {
                strBuilder.append("\t");
                i++;
            } else if (nextCode == 'b' || nextCode == 'B') {
                strBuilder.append("\b");
                i++;
            } else if (nextCode == 'd') {
                strBuilder.append("\\d");
                i++;
            } else if (nextCode == 'D') {
                strBuilder.append("\\D");
                i++;
            } else if (nextCode == 'w') {
                strBuilder.append("\\w");
                i++;
            } else if (nextCode == 'W') {
                strBuilder.append("\\W");
                i++;
            } else if (nextCode == 's') {
                strBuilder.append("\\s");
                i++;
            } else if (nextCode == 'S') {
                strBuilder.append("\\S");
                i++;
            } else if (nextCode == 'f' || nextCode == 'F') {
                strBuilder.append("\f");
                i++;
            } else if (nextCode == '\'') {
                strBuilder.append("'");
                i++;
            } else if (nextCode == '\"') {
                strBuilder.append("\"");
                i++;
            } else if (nextCode == '\\') {
                strBuilder.append("\\");
                i++;
            } else {
                throw new UnsupportedOperationException("Unsupported '\\" + nextCode + "' code.");
            }
        }

        return strBuilder.toString();
    }
}
