package com.clougence.clouddm.file.convert.xlsx;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import com.clougence.utils.StringUtils;

import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.write.handler.CellWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteTableHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Slf4j
public class MsExcelCellWriteHandler implements CellWriteHandler {

    private final AtomicLong           lastRowId;
    private final List<MsExcelRowData> rowBatch;

    public MsExcelCellWriteHandler(AtomicLong lastRowId, List<MsExcelRowData> rowBatch){
        this.lastRowId = lastRowId;
        this.rowBatch = rowBatch;
    }

    private MsExcelRowData findRowData(long rowIndex) {
        for (MsExcelRowData rowData : this.rowBatch) {
            if (rowData.getRowNumber() == rowIndex) {
                return rowData;
            }
        }
        return null;
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, WriteCellData<?> cellData, Cell cell, Head head,
                                       Integer relativeRowIndex, Boolean isHead) {
        long rowIndex = this.lastRowId.get() + relativeRowIndex;
        int columnIndex = cell.getColumnIndex();
        MsExcelRowData rowData = findRowData(rowIndex);
        if (rowData == null) {
            return;
        }

        String common = rowData.getRowComment().get(columnIndex);
        if (StringUtils.isBlank(common)) {
            return;
        }

        Sheet sheet = writeSheetHolder.getSheet();
        Drawing<?> drawingPatriarch = sheet.createDrawingPatriarch();
        Comment comment = drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) columnIndex, ((int) rowIndex + 1), (short) 5, 6));
        comment.setString(new XSSFRichTextString(common));
        sheet.getRow(relativeRowIndex).getCell(columnIndex).setCellComment(comment);
    }
}
