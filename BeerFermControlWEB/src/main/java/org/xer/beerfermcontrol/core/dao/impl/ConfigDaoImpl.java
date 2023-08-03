package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.xer.beerfermcontrol.core.bean.Config;
import org.xer.beerfermcontrol.core.dao.ConfigDao;

/**
 *
 * @author Achlys
 */
@Repository
public class ConfigDaoImpl implements ConfigDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Config> getUsersConfigs(Integer userId) {
        String sql = "SELECT * FROM CONFIG WHERE USERS_ID = :userid";
        return jdbc.query(sql, Collections.singletonMap("userid", userId), new RowMapper<Config>() {
            @Override
            public Config mapRow(ResultSet rs, int rowNum) throws SQLException {
                Config config = new Config();
                config.setId(rs.getInt("ID"));
                config.setUserId(rs.getInt("USERS_ID"));
                config.setName(rs.getString("NAME").trim());
                config.setTolerance(rs.getDouble("TOLERANCE"));
                config.setStartDate(rs.getDate("START_DATE"));
                config.setEndDate(rs.getDate("END_DATE"));
                return config;
            }
        });
    }
}
