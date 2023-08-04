package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.xer.beerfermcontrol.core.bean.Tplink;
import org.xer.beerfermcontrol.core.dao.TplinkDao;

/**
 *
 * @author Achlys
 */
@Repository
public class TplinkDaoImpl implements TplinkDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public Tplink getTplinkByConfig(Integer configId, String type) {
        String sql = "SELECT * FROM TPLINK WHERE CONFIG_ID = :configid";
        // TODO: include type when de DDBB is corrected
        Map parameters = new HashMap();
        parameters.put("configid", configId);
        // parameters.put("type", type);
        try {
            return jdbc.queryForObject(sql, parameters, new RowMapper<Tplink>() {
                @Override
                public Tplink mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Tplink tplink = new Tplink();
                    tplink.setId(rs.getInt("ID"));
                    tplink.setConfigId(rs.getInt("CONFIG_ID"));
                    tplink.setName(rs.getString("NAME").trim());
                    //tplink.setType(rs.getString("TEMP_TYPE"));
                    tplink.setIp(rs.getString("IP"));
                    return tplink;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            // The Hydrom doesn't exist yet
            return null;
        }
    }
}
