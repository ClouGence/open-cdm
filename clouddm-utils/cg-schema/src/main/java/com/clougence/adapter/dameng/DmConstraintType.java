package com.clougence.adapter.dameng;

/**
 * 达梦 约束类型
 * @version : 2021-05-07
 * @author 赵永春 (zyc@hasor.net)
 */
public enum DmConstraintType {

    /** 主键 */
    PrimaryKey("P"),
    /** 唯一 */
    Unique("U"),
    /** 外建 */
    ForeignKey("R"),
    //    /** Supplemental logging */
    //    Supplemental("S"),
    //    /** 检查 */
    //    Check("C"),
    //    /** 视图检查 */
    //    CheckView("V"),
    //    //
    //    /** With read only, on a view */
    //    ReadOnly("O"),
    //    /** Hash expression */
    //    Hash("H"),
    //    /** Constraint that involves a REF column */
    //    RefColumn("F"),;
    ;

    private final String typeName;

    DmConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static DmConstraintType valueOfCode(String code) {
        for (DmConstraintType constraintType : DmConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
