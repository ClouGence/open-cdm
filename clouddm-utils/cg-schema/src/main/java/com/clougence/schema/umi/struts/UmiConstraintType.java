package com.clougence.schema.umi.struts;

/**
 * 数据实际类型。
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
public interface UmiConstraintType {

    String getCode();

    default String getFullTypeCode() { return this.getClass().getName() + "," + this.getCode(); }

    default String getSimpleTypeCode() { return this.getClass().getSimpleName() + "," + this.getCode(); }
}
