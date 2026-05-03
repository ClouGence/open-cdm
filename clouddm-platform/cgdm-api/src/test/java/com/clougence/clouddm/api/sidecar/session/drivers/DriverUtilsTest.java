package com.clougence.clouddm.api.sidecar.session.drivers;

import org.junit.Assert;
import org.junit.Test;

public class DriverUtilsTest {

    @Test
    public void should_parse_driver_ref_from_json_array_format() {
        DriverRef driverRef = DriverUtils.parseDriverRef("[\"MySQL Connector/J\",\"/5.1.49\"]");

        Assert.assertEquals("MySQL Connector/J", driverRef.getDriverFamily());
        Assert.assertEquals("5.1.49", driverRef.getDriverVersion());
    }

    @Test
    public void should_parse_driver_ref_from_legacy_slash_format() {
        DriverRef driverRef = DriverUtils.parseDriverRef("MySQL Connector/J/5.1.49");

        Assert.assertEquals("MySQL Connector/J", driverRef.getDriverFamily());
        Assert.assertEquals("5.1.49", driverRef.getDriverVersion());
    }
}