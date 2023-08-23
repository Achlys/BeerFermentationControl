package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
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

    private RowMapper<Tplink> rowMapper = new RowMapper<Tplink>() {
        @Override
        public Tplink mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tplink tplink = new Tplink();
            tplink.setId(rs.getInt("ID"));
            tplink.setConfigId(rs.getInt("CONFIG_ID"));
            tplink.setName(rs.getString("NAME").trim());
            tplink.setType(rs.getString("TYPE"));
            tplink.setIp(rs.getString("IP"));
            return tplink;
        }
    };

    @Override
    public Tplink getTplinkByConfig(Integer configId, String type) {
        String sql = "SELECT * FROM TPLINK WHERE CONFIG_ID = :configid AND TYPE = :type";
        Map parameters = new HashMap();
        parameters.put("configid", configId);
        parameters.put("type", type);
        try {
            return jdbc.queryForObject(sql, parameters, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            // The Hydrom doesn't exist yet
            return null;
        }
    }

    @Override
    public void addTplink(Tplink tplink) {
        String sql = "SELECT MAX(ID) FROM TPLINK";
        Integer id = jdbc.query(sql, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getInt(1) + 1;
                } else {
                    return 1;
                }
            }
        });
        sql = "INSERT INTO TPLINK (ID, CONFIG_ID, NAME, TYPE, IP) VALUES (:id, :configId, :name, :type, :ip)";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("configId", tplink.getConfigId());
        parameters.put("name", tplink.getName());
        parameters.put("type", tplink.getType());
        parameters.put("ip", tplink.getIp());
        jdbc.update(sql, parameters);
    }

    @Override
    public void removeTplink(Integer id, Integer configId) {
        String sql = "DELETE FROM TPLINK WHERE ID = :id AND CONFIG_ID = :configId";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("configId", configId);
        if (jdbc.update(sql, parameters) == 0) {
            throw new RuntimeException("Ha intentado eliminar un TPLink ID que no coincidia con el Config ID!!!");
        }
    }

    @Override
    public Tplink getTplink(Integer id, Integer configId) {
        String sql = "SELECT * FROM TPLINK WHERE ID = :id AND CONFIG_ID = :configId";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("configId", configId);
        try {
            return jdbc.queryForObject(sql, parameters, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            // The TPLink doesn't exist yet
            return null;
        }
    }

    @Override
    public void updateTplink(Tplink tplink) {
        String sql = "UPDATE TPLINK SET NAME = :name, IP = :ip WHERE ID = :id AND CONFIG_ID = :configId";
        Map parameters = new HashMap();
        parameters.put("id", tplink.getId());
        parameters.put("configId", tplink.getConfigId());
        parameters.put("name", tplink.getName());
        parameters.put("ip", tplink.getIp());
        if(jdbc.update(sql, parameters) == 0){
            throw new RuntimeException("Ha intentado actualizar una TPLink con ID que no coincidia con el Config ID!!!");
        }
    }
    
}
