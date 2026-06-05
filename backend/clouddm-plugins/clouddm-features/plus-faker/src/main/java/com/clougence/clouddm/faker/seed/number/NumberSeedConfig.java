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

import org.apache.ibatis.type.TypeHandler;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedType;
import com.clougence.clouddm.faker.types.TypeHandlerRegistryUtils;
import com.clougence.clouddm.faker.utils.RandomRatio;

/**
 * 数值类型的 SeedConfig
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class NumberSeedConfig extends SeedConfig {

    private NumberType                numberType;
    //随机方式
    private final RandomRatio<MinMax> minmax = new RandomRatio<>(); // 最小值/最大值
    private Integer                   precision;                    //数的长度
    //精度和选项
    private Integer                   scale;
    private boolean                   abs;

    public final SeedType getSeedType() { return SeedType.Number; }

    @Override
    protected TypeHandler<?> defaultTypeHandler() {
        return TypeHandlerRegistryUtils.getDefaultTypeHandler();
    }

    public void addMinMax(BigDecimal min, BigDecimal max) {
        this.addMinMax(50, min, max);
    }

    /** special
     * @param minmax like: 1234-5678;
     * */
    public void setMinMax(String minmax) {
        String[] split = minmax.split("-");
        //        if (split.length != 3){
        //            
        //        }
        this.addMinMax(50, new BigDecimal(split[0]), new BigDecimal(split[1]));
    }

    public void addMinMax(int ratio, BigDecimal min, BigDecimal max) {
        this.minmax.addRatio(ratio, new MinMax(min, max));
    }

    public void clearMinMax() {
        this.minmax.clearRatio();
    }

    public NumberType getNumberType() { return numberType; }

    public void setNumberType(NumberType numberType) {
        this.numberType = numberType;
        this.setTypeHandler(TypeHandlerRegistryUtils.getTypeHandler(numberType.getDateType()));
    }

    public RandomRatio<MinMax> getMinMax() { return this.minmax; }

    public Integer getPrecision() { return precision; }

    public void setPrecision(Integer precision) { this.precision = precision; }

    public Integer getScale() { return scale; }

    public void setScale(Integer scale) { this.scale = scale; }

    public boolean isAbs() { return this.abs; }

    public void setAbs(boolean abs) { this.abs = abs; }
}
