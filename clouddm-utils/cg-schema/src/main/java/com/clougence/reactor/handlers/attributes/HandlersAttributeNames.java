package com.clougence.reactor.handlers.attributes;

import com.clougence.schema.umi.struts.AbstractAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class HandlersAttributeNames extends AbstractAttributeNames {

    private HandlersAttributeNames(String name){
        super(HandlersAttributeNames.class.getName(), name);
    }

    // time columns following the first one, is empty or '0000-00-00 00:00:00'
    //    see com.clougence.reactor.handlers.attributes.TimeDefaultStrategyEnum
    //  - nothing  : do nothing to follow the original
    //  - zero     : use 'zero', is kile "not null default '0000-00-00 00:00:00'"
    //  - is_null  : will be set empty or "null"
    //  - current  : use 'current', is like "not null default current_timestamp"
    //  - utc_zero : is '1970-01-01 00:00:00'
    public static final UmiAttributeNames MYSQL_ZERO_TIME_STRATEGY = check(new HandlersAttributeNames("mysql_zero_time"));
    public static final UmiAttributeNames MYSQL_IS_ALI_SQL         = check(new HandlersAttributeNames("mysql_is_alisql"));
    //
    public static final UmiAttributeNames MSSQL_DATAFORMAT_TYPE    = check(new HandlersAttributeNames("mssql_df_type"));
    public static final UmiAttributeNames MSSQL_DROP_IDENTITY      = check(new HandlersAttributeNames("mssql_drop_identity"));

    // time columns following the first one, is empty or '0000-00-00 00:00:00'
    //    see com.clougence.reactor.handlers.attributes.TimeDefaultStrategyEnum
    //  - is_null  : will be set empty or "null"
    //  - current  : use 'current', is like "not null default current_timestamp"
    //  - utc_zero : is '1970-01-01 00:00:00'
    //  - era_zero : is '0000-01-01 00:00:00'
    public static final UmiAttributeNames PG_ZERO_TIME_STRATEGY    = check(new HandlersAttributeNames("pg_zero_time"));

    public static final UmiAttributeNames PG_BIT1_2_BOOLEAN        = check(new HandlersAttributeNames("pg_bit1_2_boolean"));

    public static final UmiAttributeNames DUCK_ZERO_TIME_STRATEGY    = check(new HandlersAttributeNames("duck_zero_time"));

    public static final UmiAttributeNames DUCK_BIT1_2_BOOLEAN        = check(new HandlersAttributeNames("duck_bit1_2_boolean"));

    public static final UmiAttributeNames SR_KEEP_INDEX            = check(new HandlersAttributeNames("sr_keep_index"));
    public static final UmiAttributeNames DR_KEEP_INDEX            = check(new HandlersAttributeNames("DR_keep_index"));

}
