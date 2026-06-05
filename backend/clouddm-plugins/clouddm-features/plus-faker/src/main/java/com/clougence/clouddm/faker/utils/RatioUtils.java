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
package com.clougence.clouddm.faker.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.utils.ArrayUtils;
import com.clougence.utils.StringUtils;

/**
 * 控制操作生成比率
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class RatioUtils {

    public static RandomRatio<OpsType> passerByConfig(String ratioConfig) {
        RandomRatio<OpsType> ratio = new RandomRatio<>();
        passerOpsRatio(ratioConfig).forEach(o -> ratio.addRatio(o.getRatio(), o.getOpsType()));
        return ratio;
    }

    public static void fillByConfig(String ratioConfig, RandomRatio<OpsType> ratio) {
        passerOpsRatio(ratioConfig).forEach(o -> ratio.addRatio(o.getRatio(), o.getOpsType()));
    }

    private static String[] split(final String str, final String[] separators) {
        if (str == null || str.equals("")) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        Stream<String> stream = Stream.of(str);
        for (String separator : separators) {
            stream = stream.flatMap(s -> Arrays.stream(s.split(separator)));
        }
        return stream.filter(s -> !StringUtils.isEmpty(s)).toArray(String[]::new);
    }

    private static List<InnerTwoObj> passerOpsRatio(String opsRatioStr) {
        String[] opsItem = split(opsRatioStr, new String[] { ";", ",", "\\|" });
        List<InnerTwoObj> opsRatio = new ArrayList<>();
        for (String pair : opsItem) {
            String[] item = pair.split("#");
            if (item.length == 2) {
                opsRatio.add(new InnerTwoObj(OpsType.valueOfCode(item[0].trim()), Integer.parseInt(item[1].trim())));
            } else {
                throw new IllegalArgumentException("opsRatio config format illegal(legal one,e.g,INSERT#30;UPDATE#30;DELETE#30).value:" + opsRatioStr);
            }
        }

        return opsRatio;
    }

    private static class InnerTwoObj {

        private final OpsType opsType;
        private final int     ratio;

        public InnerTwoObj(OpsType opsType, int ratio){
            this.opsType = opsType;
            this.ratio = ratio;
        }

        public OpsType getOpsType() { return opsType; }

        public int getRatio() { return ratio; }
    }
}
