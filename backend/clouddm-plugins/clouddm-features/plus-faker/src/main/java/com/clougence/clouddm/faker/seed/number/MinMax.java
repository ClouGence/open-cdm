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
package com.clougence.clouddm.faker.seed.number;

import java.math.BigDecimal;

/**
 * 表示一组最大最小值
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class MinMax {

    private BigDecimal min;
    private BigDecimal max;

    public MinMax(){
        this(null, null);
    }

    public MinMax(BigDecimal min, BigDecimal max){
        this.setMin(min);
        this.setMax(max);
    }

    public BigDecimal getMin() { return this.min; }

    public void setMin(BigDecimal min) { this.min = min; }

    public BigDecimal getMax() { return this.max; }

    public void setMax(BigDecimal max) { this.max = max; }
}
