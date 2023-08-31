package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.xer.beerfermcontrol.core.bean.Ulog;
import org.xer.beerfermcontrol.core.dao.UlogDao;

/**
 *
 * @author Achlys
 */
@Repository
public class UlogDaoImpl implements UlogDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    RowMapper<Ulog> rowMapper = new RowMapper<Ulog>() {
        @Override
        public Ulog mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ulog ulog = new Ulog();
            ulog.setMoment(rs.getTimestamp("MOMENT"));
            ulog.setConfigId(rs.getInt("CONFIG_ID"));
            ulog.setEvent(rs.getString("EVENT"));
            return ulog;
        }
    };

    @Override
    public void addEvent(Integer configId, String event) {
        String sql = "INSERT INTO ULOG (MOMENT, CONFIG_ID, EVENT) VALUES (CURRENT_TIMESTAMP, :configid, :event)";
        Map parameters = new HashMap();
        parameters.put("configid", configId);
        parameters.put("event", event);
        jdbc.update(sql, parameters);
    }

    @Override
    public List<Ulog> getEventList(Integer configId) {
        String sql = "SELECT * FROM ULOG WHERE CONFIG_ID IN (0, :configid) ORDER BY MOMENT DESC";
        return jdbc.query(sql, Collections.singletonMap("configid", configId), rowMapper);
    }

}
