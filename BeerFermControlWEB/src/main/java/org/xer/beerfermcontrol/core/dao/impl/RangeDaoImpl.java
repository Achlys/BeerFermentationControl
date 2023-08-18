package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.xer.beerfermcontrol.core.bean.Range;
import org.xer.beerfermcontrol.core.dao.RangeDao;

/**
 *
 * @author Achlys
 */
@Repository
public class RangeDaoImpl implements RangeDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Range> getRangesByConfig(Integer configId) {
        String sql = "SELECT * FROM TEMPRANGE WHERE CONFIG_ID = :configid";
        return jdbc.query(sql, Collections.singletonMap("configid", configId), new RowMapper<Range>() {
            @Override
            public Range mapRow(ResultSet rs, int rowNum) throws SQLException {
                Range range = new Range();
                range.setId(rs.getInt("ID"));
                range.setConfigId(rs.getInt("CONFIG_ID"));
                range.setTopGravity(rs.getDouble("TOP_GRAVITY"));
                range.setBottomGravity(rs.getDouble("BOTTOM_GRAVITY"));
                range.setAimedTemp(rs.getDouble("AIMED_TEMP"));
                return range;
            }
        });
    }

}
