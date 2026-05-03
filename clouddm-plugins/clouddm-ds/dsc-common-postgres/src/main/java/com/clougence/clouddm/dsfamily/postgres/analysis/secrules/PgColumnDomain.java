package com.clougence.clouddm.dsfamily.postgres.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PgColumnDomain extends RdbColumnDomain {

    private boolean array;
}
