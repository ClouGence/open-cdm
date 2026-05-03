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
