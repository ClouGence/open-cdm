package com.clougence.rdp.util;

import java.util.Date;

/**
 * @author wanshao create time is 2021/1/27
 **/
public abstract class RdpTimeUtil {

    public static Date getDateTimeOfTimestamp(Long timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return new Date(timestamp);
        }
    }

}
