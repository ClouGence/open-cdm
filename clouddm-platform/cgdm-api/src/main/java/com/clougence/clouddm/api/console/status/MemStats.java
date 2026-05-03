package com.clougence.clouddm.api.console.status;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemStats {

    private BigDecimal totalMemoryGb    = new BigDecimal(0);
    private BigDecimal usedMemoryGb     = new BigDecimal(0);
    private BigDecimal freeMemoryGb     = new BigDecimal(0);
    private BigDecimal totalMemoryMb    = new BigDecimal(0);
    private BigDecimal usedMemoryMb     = new BigDecimal(0);
    private BigDecimal freeMemoryMb     = new BigDecimal(0);
    private BigDecimal memoryUsage      = new BigDecimal(0);

    private BigDecimal jvmTotalMemoryMb = new BigDecimal(0);
    private BigDecimal jvmUsedMemoryMb  = new BigDecimal(0);
    private BigDecimal jvmFreeMemoryMb  = new BigDecimal(0);
    private BigDecimal jvmMemoryUsage   = new BigDecimal(0);
}
