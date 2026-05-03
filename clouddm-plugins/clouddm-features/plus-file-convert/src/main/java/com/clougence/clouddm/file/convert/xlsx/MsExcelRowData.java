package com.clougence.clouddm.file.convert.xlsx;

import java.util.ArrayList;
import java.util.List;

import cn.idev.excel.write.handler.CellWriteHandler;
import lombok.Getter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Getter
public class MsExcelRowData implements CellWriteHandler {

    private final long         rowNumber;
    private final List<String> rowComment;
    private final List<String> rowData;

    public MsExcelRowData(long rowNumber, int colSize){
        this.rowNumber = rowNumber;
        this.rowComment = new ArrayList<>(colSize);
        this.rowData = new ArrayList<>(colSize);

        for (int i = 0; i < colSize; i++) {
            this.rowComment.add("");
            this.rowData.add("");
        }
    }
}
