package com.clougence.clouddm.api.console.status;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiskStats {

    private AtomicLong freeDiskGB  = new AtomicLong(0);

    private AtomicLong usedDiskGB  = new AtomicLong(0);

    private AtomicLong totalDiskGB = new AtomicLong(0);

    private BigDecimal diskUsage   = new BigDecimal(0);

}
