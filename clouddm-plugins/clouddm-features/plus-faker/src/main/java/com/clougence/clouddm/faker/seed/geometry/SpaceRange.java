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

/**
 * 通过一个矩形框来限制随机图形范围
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class SpaceRange {

    private Point pointA;
    private Point pointB;

    public SpaceRange(){
        this(new Point(0, 0), new Point(100, 100));
    }

    public SpaceRange(double axisXofA, double axisYofA, double axisXofB, double axisYofB){
        this(new Point(axisXofA, axisYofA), new Point(axisXofB, axisYofB));
    }

    public SpaceRange(Point pointA, Point pointB){
        this.setPointA(pointA);
        this.setPointB(pointB);
    }

    public Point getPointA() { return pointA; }

    public void setPointA(Point pointA) { this.pointA = pointA; }

    public Point getPointB() { return pointB; }

    public void setPointB(Point pointB) { this.pointB = pointB; }
}
