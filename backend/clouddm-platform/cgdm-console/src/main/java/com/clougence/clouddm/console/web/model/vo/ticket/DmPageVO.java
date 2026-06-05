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
package com.clougence.clouddm.console.web.model.vo.ticket;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmPageVO<T> {

    private long    current;
    private long    pages;
    private long    size;
    private long    total;
    private List<T> records = new ArrayList<T>();

    public DmPageVO(IPage page){
        this.current = page.getCurrent();
        this.pages = page.getPages();
        this.total = page.getTotal();
        this.size = page.getSize();
    }
}
