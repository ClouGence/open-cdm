package com.clougence.utils;

import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2024/12/6 15:51
 */
public class ArrayUtilsTest {

    @Test
    public void safeToString() {
        Object[][][] obj1 = new Object[][][] { { { new Integer("1") }, { new String("ggg"), new Double(2L) }, { new String[] { "asd" } }, { new String("gaga") }, {} } };
        String s = ArrayUtils.safeToString(obj1);
        System.out.println(s);
        assert s.equals("[[[1],[ggg,2.0],[[asd]],[gaga],[]]]");
        String s2 = ArrayUtils.safeToString("");
        System.out.println(s2);
        assert s2.isEmpty();
        String s3 = ArrayUtils.safeToString(null);
        System.out.println(s3);
        assert s3 == null;
    }
}
