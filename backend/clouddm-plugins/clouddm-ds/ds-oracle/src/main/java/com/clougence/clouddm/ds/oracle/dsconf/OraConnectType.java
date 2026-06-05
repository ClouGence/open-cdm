/*
 * Copyright 2026 杭州开云集致科技有限公司
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
package com.clougence.clouddm.ds.oracle.dsconf;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;

import lombok.Getter;

/**
 * @author mode 2020/11/6 10:23
 */
@Getter
public enum OraConnectType {

    SID("sid", ConnectType.ORACLE_SID),
    SERVICE("service", ConnectType.ORACLE_SERVICE),
    TNS("tns", ConnectType.ORACLE_TNS),
    PDB("pdb", ConnectType.ORACLE_PDB),;

    private final String      driverTypeCode;
    private final ConnectType connectType;

    OraConnectType(String driverTypeCode, ConnectType connectType){
        this.driverTypeCode = driverTypeCode;
        this.connectType = connectType;
    }

    public static OraConnectType valueOfCode(ConnectType connectType) {
        if (connectType == null) {
            return null;
        }

        switch (connectType) {
            case DEFAULT:
            case ORACLE_SID:
                return SID;
            case ORACLE_SERVICE:
                return SERVICE;
            case ORACLE_TNS:
                return TNS;
            case ORACLE_PDB:
                return PDB;
            default:
                return null;
        }
    }
}
