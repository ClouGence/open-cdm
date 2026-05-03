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
