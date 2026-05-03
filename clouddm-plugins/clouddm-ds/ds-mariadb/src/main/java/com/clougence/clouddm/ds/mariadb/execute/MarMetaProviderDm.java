package com.clougence.clouddm.ds.mariadb.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.umi.special.rdb.RdbTable;

import lombok.extern.slf4j.Slf4j;

/**
 * MySQL 元信息获取，参考资料：
 * <li>https://dev.mysql.com/doc/refman/8.0/en/information-schema.html</li>
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-01-22
 */
@Slf4j
public class MarMetaProviderDm extends MyMetaProviderDm {

    public MarMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new MarMetaProviderUtils();
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select t.`TABLE_CATALOG` TABLE_CATALOG,t.`TABLE_SCHEMA` TABLE_SCHEMA,t.`TABLE_NAME` TABLE_NAME,t.`TABLE_TYPE` TABLE_TYPE,t.`TABLE_COLLATION` TABLE_COLLATION,t.`CREATE_TIME` CREATE_TIME,v.`CHECK_OPTION` CHECK_OPTION,"
                     + "t.`UPDATE_TIME` UPDATE_TIME,t.`TABLE_COMMENT` TABLE_COMMENT,t.`ENGINE` ENGINE,t.`ROW_FORMAT` ROW_FORMAT,t.`AVG_ROW_LENGTH` AVG_ROW_LENGTH,t.`CREATE_OPTIONS` CREATE_OPTIONS,t.`AUTO_INCREMENT` AUTO_INCREMENT,"
                     + "v.VIEW_DEFINITION VIEW_DEFINITION "
                     + "from INFORMATION_SCHEMA.TABLES t left join INFORMATION_SCHEMA.VIEWS v on t.TABLE_NAME = v.TABLE_NAME where t.TABLE_SCHEMA = ? and t.TABLE_TYPE in ('VIEW','SYSTEM VIEW')"
                     + " and t.TABLE_NAME in " + buildWhereIn(tabs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return this.convertView(rs);
            }
        }
    }
}
