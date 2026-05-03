/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.clouddm.faker.seed.geometry;

import java.math.BigDecimal;

/**
 * 表示一个点
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class Point {

    private BigDecimal x;
    private BigDecimal y;

    public Point(){
        this(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public Point(double axisX, double axisY){
        this.setX(BigDecimal.valueOf(axisX));
        this.setY(BigDecimal.valueOf(axisY));
    }

    public Point(BigDecimal x, BigDecimal y){
        this.setX(x);
        this.setY(y);
    }

    public BigDecimal getX() { return x; }

    public void setX(BigDecimal x) { this.x = x; }

    public BigDecimal getY() { return y; }

    public void setY(BigDecimal y) { this.y = y; }
}
