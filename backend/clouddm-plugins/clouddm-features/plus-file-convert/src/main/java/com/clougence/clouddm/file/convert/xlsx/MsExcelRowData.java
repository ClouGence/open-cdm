/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
