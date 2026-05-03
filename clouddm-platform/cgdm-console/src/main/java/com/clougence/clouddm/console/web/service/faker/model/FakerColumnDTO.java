package com.clougence.clouddm.console.web.service.faker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakerColumnDTO {

    private String  seedType;
    private String  allowNullable;
    private String  nullableRatio;
    //array
    private String  minSize;
    private String  maxSize;
    //bytes & string
    private String  minLength;
    private String  maxLength;
    //string
    private String  allowEmpty;
    //number
    private String  minMax;
    private String  scale;
    private String  abs;
    private String  numberType;
    //date
    private String  genType;
    private String  rangeForm;
    private String  rangeTo;
    private String  zoneForm;
    private String  zoneTo;
    private String  startTime;
    private String  minInterval;
    private String  maxInterval;
    private String  intervalScope;
    //geometry
    private String  geometryType;
    private String  formatType;
    private String  minPointSize;
    private String  maxPointSize;
    //guid
    private String  dateType;
    //    number & date & geometry
    private String  precision;

    // ignore
    private Boolean ignoreColsUpdate;
    private Boolean ignoreColsUpdateWhere;
    private Boolean ignoreColsDeleteWhere;
    private Boolean ignoreColsInsert;
}
