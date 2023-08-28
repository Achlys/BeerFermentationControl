package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
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

    private RowMapper<Hydrom> rowMapper = new RowMapper<Hydrom>() {
        @Override
        public Hydrom mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hydrom hydrom = new Hydrom();
            hydrom.setId(rs.getInt("ID"));
            hydrom.setConfigId(rs.getInt("CONFIG_ID"));
            hydrom.setName(rs.getString("NAME").trim());
            return hydrom;
        }
    };

    @Override
    public Hydrom getHydromByConfig(Integer configId) {
        String sql = "SELECT * FROM HYDROM WHERE CONFIG_ID = :configid";
        try {
            return jdbc.queryForObject(sql, Collections.singletonMap("configid", configId), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            // The Hydrom doesn't exist yet
            return null;
        }
    }

    @Override
    public void addHydrom(Hydrom hydrom) {
        String sql = "SELECT MAX(ID) FROM HYDROM";
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
        sql = "INSERT INTO HYDROM (ID, CONFIG_ID, NAME) VALUES (:id, :configId, :name)";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("configId", hydrom.getConfigId());
        parameters.put("name", hydrom.getName());
        jdbc.update(sql, parameters);
    }

    @Override
    public void removeHydrom(Integer id, Integer configId) {
        String sql = "DELETE FROM HYDROM WHERE ID = :id AND CONFIG_ID = :configId";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("configId", configId);
        if (jdbc.update(sql, parameters) == 0) {
            throw new RuntimeException("Ha intentado eliminar un Hydrom ID que no coincidia con el Config ID!!!");
        }
    }

    @Override
    public Hydrom getHydrom(Integer id, Integer configId) {
        String sql = "SELECT * FROM HYDROM WHERE ID = :id AND CONFIG_ID = :configId";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("configId", configId);
        try {
            return jdbc.queryForObject(sql, parameters, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            // The Hydrom doesn't exist yet
            return null;
        }
    }

    @Override
    public void updateHydrom(Hydrom hydrom) {
        String sql = "UPDATE HYDROM SET NAME = :name WHERE ID = :id AND CONFIG_ID = :configId";
        Map parameters = new HashMap();
        parameters.put("id", hydrom.getId());
        parameters.put("configId", hydrom.getConfigId());
        parameters.put("name", hydrom.getName());
        if(jdbc.update(sql, parameters) == 0){
            throw new RuntimeException("Ha intentado actualizar una Hydrom con ID que no coincidia con el Config ID!!!");
        }
    }

    @Override
    public List<Hydrom> getHydromsByName(String deviceName) {
        String sql = "SELECT * FROM HYDROM WHERE NAME = :name";
        return jdbc.query(sql, Collections.singletonMap("name", deviceName), rowMapper);
    }

}
