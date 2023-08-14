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
    
    @Override
    public void addConfig(Config newConfig){
        String sql = "SELECT MAX(ID) FROM CONFIG";
        Integer id = jdbc.query(sql, new ResultSetExtractor<Integer>(){
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                if(rs.next()){
                    return rs.getInt(0) + 1;
                }else{
                    return 1;
                }
            }     
        });
        sql = "INSERT INTO CONFIG (ID, USER_ID, NAME, TOLERANCE, START_DATE, END_DATE) VALUES (:id, :userId, :name, :tolerance, :startDate, :endDate)";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("userId", newConfig.getUserId());
        parameters.put("name", newConfig.getName());
        parameters.put("tolerance", newConfig.getTolerance());
        parameters.put("startDate", newConfig.getStartDate());
        parameters.put("endDate", newConfig.getEndDate());
        jdbc.update(sql, parameters);
    }
    
}
