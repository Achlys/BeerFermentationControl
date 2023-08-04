package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.xer.beerfermcontrol.core.bean.Hydrom;
import org.xer.beerfermcontrol.core.dao.HydromDao;

/**
 *
 * @author Achlys
 */
@Repository
public class HydromDaoImpl implements HydromDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public Hydrom getHydromByConfig(Integer configId) {
        String sql = "SELECT * FROM HYDROM WHERE CONFIG_ID = :configid";
        try {
            return jdbc.queryForObject(sql, Collections.singletonMap("configid", configId), new RowMapper<Hydrom>() {
                @Override
                public Hydrom mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Hydrom hydrom = new Hydrom();
                    hydrom.setId(rs.getInt("ID"));
                    hydrom.setConfigId(rs.getInt("CONFIG_ID"));
                    hydrom.setName(rs.getString("NAME").trim());
                    return hydrom;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            // The Hydrom doesn't exist yet
            return null;
        }
    }
    
}
