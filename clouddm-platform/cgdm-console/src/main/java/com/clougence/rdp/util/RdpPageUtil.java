package com.clougence.rdp.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
* @author Ekko
* @date 2022/11/30 19:19
*/
public class RdpPageUtil {

    public static Page<?> startPage(RdpPageDO rdpPageDO) {
        Page<?> page = new Page<>();
        if (rdpPageDO == null) {
            return page;
        }
        page.setCurrent(rdpPageDO.getPageNum());
        page.setSize(rdpPageDO.getPageSize());
        return page;
    }
}
