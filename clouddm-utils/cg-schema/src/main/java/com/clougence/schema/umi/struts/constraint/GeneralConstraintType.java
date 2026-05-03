package com.clougence.schema.umi.struts.constraint;

import com.clougence.schema.umi.struts.UmiConstraintType;

/**
 * 约束类型
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum GeneralConstraintType implements UmiConstraintType {

    /** 非空的 */
    NonNull("NonNull"),
    /** 唯一的 */
    Unique("Unique"),
    /** 主要的 */
    Primary("Primary"),
    Check("Check"),
    Foreign("Foreign"),;

    private final String typeName;

    GeneralConstraintType(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String getCode() { return this.typeName; }

    public static GeneralConstraintType valueOfCode(String code) {
        for (GeneralConstraintType constraintType : GeneralConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
