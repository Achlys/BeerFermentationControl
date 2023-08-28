package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
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

    RowMapper<Config> rowMapper = new RowMapper<Config>() {
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
    };

    @Override
    public List<Config> getUsersConfigs(Integer userId) {
        String sql = "SELECT * FROM CONFIG WHERE USERS_ID = :userid";
        return jdbc.query(sql, Collections.singletonMap("userid", userId), rowMapper);
    }

    @Override
    public void addConfig(Config config) {
        String sql = "SELECT MAX(ID) FROM CONFIG";
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
        config.setId(id);
        sql = "INSERT INTO CONFIG (ID, USERS_ID, NAME, TOLERANCE, START_DATE, END_DATE) VALUES (:id, :userId, :name, :tolerance, :startDate, :endDate)";
        Map parameters = new HashMap();
        parameters.put("id", config.getId());
        parameters.put("userId", config.getUserId());
        parameters.put("name", config.getName());
        parameters.put("tolerance", config.getTolerance());
        parameters.put("startDate", config.getStartDate());
        parameters.put("endDate", config.getEndDate());
        jdbc.update(sql, parameters);
    }

    @Override
    public void removeConfig(Integer id, Integer userId) {
        String sql = "DELETE FROM CONFIG WHERE ID = :id AND USERS_ID = :userId";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("userId", userId);
        if (jdbc.update(sql, parameters) == 0) {
            throw new RuntimeException("Ha intentado eliminar un config ID que no coincidia con el user ID!!!");
        }
    }

    @Override
    public Config getConfig(Integer id) {
        String sql = "SELECT * FROM CONFIG WHERE ID = :id";
        Map parameters = new HashMap();
        parameters.put("id", id);
        return jdbc.queryForObject(sql, parameters, rowMapper);
    }

    @Override
    public Config getConfig(Integer id, Integer userId) {
        String sql = "SELECT * FROM CONFIG WHERE ID = :id AND USERS_ID = :userId";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("userId", userId);
        return jdbc.queryForObject(sql, parameters, rowMapper);
    }

    @Override
    public void updateConfig(Config config) {
        String sql = "UPDATE CONFIG SET NAME = :name, TOLERANCE = :tolerance, START_DATE = :startDate, END_DATE = :endDate WHERE ID = :id AND USERS_ID = :userId";
        Map parameters = new HashMap();
        parameters.put("id", config.getId());
        parameters.put("userId", config.getUserId());
        parameters.put("name", config.getName());
        parameters.put("tolerance", config.getTolerance());
        parameters.put("startDate", config.getStartDate());
        parameters.put("endDate", config.getEndDate());
        if (jdbc.update(sql, parameters) == 0) {
            throw new RuntimeException("Ha intentado actualizar una config con ID que no coincidia con el user ID!!!");
        }
    }

}
