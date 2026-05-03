package com.clougence.clouddm.api.console.status;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CpuStats {

    private BigDecimal oneMinuteAvgLoad     = new BigDecimal(0);

    private BigDecimal fiveMinuteAvgLoad    = new BigDecimal(0);

    private BigDecimal fifteenMinuteAvgLoad = new BigDecimal(0);

    private int        physicalCoreCount    = 0;

    private int        logicalCoreCount     = 0;

    private BigDecimal userRatio            = new BigDecimal(0);

    private BigDecimal niceRatio            = new BigDecimal(0);

    private BigDecimal sysRatio             = new BigDecimal(0);

    private BigDecimal idleRatio            = new BigDecimal(0);

    private BigDecimal ioWaitRatio          = new BigDecimal(0);

    private BigDecimal irqRatio             = new BigDecimal(0);

    private BigDecimal softIrqRatio         = new BigDecimal(0);

    private BigDecimal stealRatio           = new BigDecimal(0);

    private BigDecimal avgUsageRatio        = new BigDecimal(0);

    private BigDecimal avgLoadRatio         = new BigDecimal(0);

}
