package com.clougence.clouddm.sdk.scm;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScmSaveTo {

    private File   saveToLocal;
    private File   tempPath;
    private String scriptPath;
}
