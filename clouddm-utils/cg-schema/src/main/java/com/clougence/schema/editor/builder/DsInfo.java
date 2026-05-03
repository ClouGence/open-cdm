package com.clougence.schema.editor.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.DsType;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.TypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DsInfo {

    private final SqlBuilder      provider;
    private final DsType          dsType;
    private final MainVersion     mainVersion;

    @Setter
    private List<TypeInfo>        typeInfoList;

    @Setter
    private CharsetsAndCollations charsetAndCollation;

    public DsInfo(SqlBuilder provider, DsType dsType, MainVersion mainVersion){
        this.provider = provider;
        this.dsType = dsType;
        this.mainVersion = mainVersion;
        this.typeInfoList = new ArrayList<>();
        this.charsetAndCollation = new CharsetsAndCollations();
    }
}
