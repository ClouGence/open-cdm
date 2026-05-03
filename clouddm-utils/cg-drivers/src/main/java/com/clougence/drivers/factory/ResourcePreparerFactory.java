package com.clougence.drivers.factory;

import java.io.File;
import java.util.Properties;

public interface ResourcePreparerFactory {

    ResourcePreparer create(File localDir, Properties config);
}
